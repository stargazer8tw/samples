package org.sample.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Vector;

/**
 * Handles messages from clients
 */
public class ConnectionHandler implements ChannelContainer {

	private static final int BUFFER_SIZE = 1024;

	protected final SocketChannel sChannel;

	protected final Selector selector;

	protected Vector<ByteBuffer> _outData = new Vector<ByteBuffer>();

	protected SelectionKey skey;

	private String name;
	/**
	 * Creates a new ConnectionHandler object
	 * 
	 * @param sChannel
	 *            the SocketChannel of the client
	 * @param data
	 *            a reference to a ReactorData object
	 */
	private ConnectionHandler(SocketChannel sChannel, Selector selector, SelectionKey key) {
		this.sChannel = sChannel;
		this.selector = selector;
		this.skey = key;
	}

	// make sure 'this' does not escape b4 the object is fully constructed!
	private void initialize() {
		if (skey != null) {
			skey.attach(this);
		}
	}

	public static ConnectionHandler create(SocketChannel sChannel, Selector selector, SelectionKey key) {
		ConnectionHandler h = new ConnectionHandler(sChannel, selector, key);
		h.initialize();
		return h;
	}

	public synchronized void addOutData(ByteBuffer buf) {
		_outData.add(buf);
		switchToReadWriteMode();
	}

	public void closeConnection() {
		// remove from the selector.
		skey.cancel();
		try {
			sChannel.close();
			System.out.println(getName() + " closed");
		} catch (IOException ignored) {
			ignored = null;
		}
	}

	/**
	 * Reads incoming data from the client:
	 * <UL>
	 * <LI>Reads some bytes from the SocketChannel
	 * <LI>create a protocolTask, to process this data, possibly generating an
	 * answer
	 * <LI>Inserts the Task to the ThreadPool
	 * </UL>
	 * 
	 * @throws
	 * 
	 * @throws IOException
	 *             in case of an IOException during reading
	 */
	public void read() {
		// do not read if protocol has terminated. only write of pending data is
		// allowed

		ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
		int numBytesRead = 0;
		try {
			numBytesRead = sChannel.read(buf);
		} catch (IOException e) {
			numBytesRead = -1;
		}
		// is the channel closed??
		if (numBytesRead == -1) {
			// No more bytes can be read from the channel
			closeConnection();
			// tell the protocol that the connection terminated.
			return;
		}

		//add the buffer to the protocol task
		buf.flip();
		// add the protocol task to the reactor
	}

	/**
	 * attempts to send data to the client<br/>
	 * if all the data has been successfully sent, the ConnectionHandler will
	 * automatically switch to read only mode, otherwise it'll stay in it's
	 * current mode (which is read / write).
	 * 
	 * @throws IOException
	 *             if the write operation fails
	 * @throws ClosedChannelException
	 *             if the channel have been closed while registering to the Selector
	 */
	public synchronized void write() {
		if (_outData.size() == 0) {
			// if nothing left in the output string, go back to read mode
			switchToReadOnlyMode();
			return;
		}
		// if there is something to send
		ByteBuffer buf = _outData.remove(0);
		if (buf.remaining() != 0) {
			try {
				sChannel.write(buf);
			} catch (IOException e) {
				// this should never happen.
				e.printStackTrace();
			}
			// check if the buffer contains more data
			if (buf.remaining() != 0) {
				_outData.add(0, buf);
			}
		}
		// check if the protocol indicated close.
	}

	/**
	 * switches the handler to read / write 
	 *  
	 * @throws ClosedChannelException
	 *             if the channel is closed
	 */
	public void switchToReadWriteMode() {
		if (skey.isValid()) {
			skey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
		selector.wakeup();
	}

	/**
	 * switches the handler to read only mode
	 * 
	 * @throws ClosedChannelException
	 *             if the channel is closed
	 */
	public void switchToReadOnlyMode() {
		if (skey.isValid()) {
			skey.interestOps(SelectionKey.OP_READ);
		}
		selector.wakeup();
	}

	/**
	 * switches the handler to write only mode
	 * 
	 * @throws ClosedChannelException
	 *             if the channel is closed
	 */
	public void switchToWriteOnlyMode() {
		if (skey.isValid()) {
			skey.interestOps(SelectionKey.OP_WRITE);
		}
		selector.wakeup();
	}

	public void onConnect() {
		try {
			if (!sChannel.isConnected()) {
				sChannel.finishConnect();
			}
			System.out.println("connect to: " + getName());
			switchToWriteOnlyMode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyChannelRegister(SelectionKey key) {
		skey = key;
	}

	public String getName() {
		if (name == null && sChannel.isConnected()) {
			name = this.sChannel.socket().toString();
		}
		return name;
	}

	public SelectableChannel getChannel() {
		return sChannel;
	}

}
