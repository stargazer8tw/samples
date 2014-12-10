package org.sample.nio.reactor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.sample.nio.reactor.Reactor.RegisterAcceptChannelWorkerTask;

/**
 * Handles new client connections. An Acceptor is bound on a ServerSocketChannel
 * objects, which can produce new SocketChannels for new clients using its
 * <CODE>accept</CODE> method.
 */
public class ConnectionAcceptor implements ChannelContainer {
	protected final ServerSocketChannel ssChannel;

	private final Roma roma;
	private String name;
	/**
	 * Creates a new ConnectionAcceptor
	 * 
	 * @param ssChannel
	 *            the ServerSocketChannel which can accept new connections
	 * @param data
	 *            a reference to ReactorData object
	 */
	public ConnectionAcceptor(ServerSocketChannel ssChannel, Roma roma) {
		this.ssChannel = ssChannel;
		this.roma = roma;
	}
	/**
	 * Accepts a connection:
	 * <UL>
	 * <LI>Creates a SocketChannel for it
	 * <LI>Creates a ConnectionHandler for it
	 * <LI>Registers the SocketChannel and the ConnectionHandler to the
	 * Selector
	 * </UL>
	 * 
	 * @throws IOException
	 *             in case of an IOException during the acceptance of a new
	 *             connection
	 */
	public void accept() throws IOException {
		// Get a new channel for the connection request
		SocketChannel sChannel = ssChannel.accept();

		// If serverSocketChannel is non-blocking, sChannel may be null
		if (sChannel != null) {
			SocketAddress address = sChannel.socket().getRemoteSocketAddress();

			System.out.println("Accepting connection from " + address);
			sChannel.configureBlocking(false);
			Reactor reactor = roma.nextReactor();			
			ConnectionHandler handler = ConnectionHandler.create(sChannel, reactor.getSelector(), null);
			reactor.addPendingTask(new RegisterAcceptChannelWorkerTask(handler, reactor.getSelector(), 0));
		}
	}
	public SelectableChannel getChannel() {
		return ssChannel;
	}
	public void notifyChannelRegister(SelectionKey key) {
		System.out.println("listen to: " + ssChannel.socket().toString());
	}
	public String getName() {
		if (name == null) {
			name = ssChannel.socket().toString();
		}
		return name;
	}
}
