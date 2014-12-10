package org.sample.nio.reactor;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class Reactor implements Runnable {

	private volatile boolean run = true;
	private Selector selector;
	private final ExecutorService workers;
	private ConcurrentLinkedQueue<WorkerTask> queue;
	public Reactor(Selector selector, ExecutorService workers) {
		this.selector = selector;
		this.workers = workers;
		queue = new ConcurrentLinkedQueue<WorkerTask>();
	}
	
	/**
	 * Main operation of the Reactor:
	 * <UL>
	 * <LI>Uses the <CODE>Selector.select()</CODE> method to find new
	 * requests from clients
	 * <LI>For each request in the selection set:
	 * <UL>
	 * If it is <B>acceptable</B>, use the ConnectionAcceptor to accept it,
	 * create a new ConnectionHandler for it register it to the Selector
	 * <LI>If it is <B>readable</B>, use the ConnectionHandler to read it,
	 * extract messages and insert them to the ThreadPool
	 * </UL>
	 */
	public void run() {
		while (run && selector.isOpen()) {
			preSelect();
			boolean hasKey = false;
			// Wait for an event
			try {
				hasKey = selector.selectNow() > 0;
				if (!hasKey) {
					hasKey = selector.select(30000) > 0;
				}
			} catch (IOException e) {
				continue;
			}
			if (!hasKey || !selector.isOpen()) {
				continue;
			}
			// Get list of selection keys with pending events
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			// Process each key
			while (it.hasNext()) {
				// Get the selection key
				SelectionKey selKey = (SelectionKey) it.next();

				// Remove it from the list to indicate that it is being
				// processed. it.remove removes the last item returned by next.
				it.remove();

				// Check if it's a connection request
				if (selKey.isValid() && selKey.isAcceptable()) {
					ConnectionAcceptor acceptor = (ConnectionAcceptor) selKey.attachment();
					try {
						acceptor.accept();
					} catch (IOException e) {
					}
					continue;
				}
				// Check if connect
				if (selKey.isValid() && selKey.isConnectable()) {
					ConnectionHandler handler = (ConnectionHandler) selKey.attachment();
					unregisterKey(selKey, SelectionKey.OP_CONNECT);
					workers.execute(new Worker(new OnConnectWorkerTask(handler, SelectionKey.OP_CONNECT)));
				}
				// Check if a message has been sent
				if (selKey.isValid() && selKey.isReadable()) {
					ConnectionHandler handler = (ConnectionHandler) selKey.attachment();
					unregisterKey(selKey, SelectionKey.OP_READ);
					workers.execute(new Worker(new IOWorkerTask(handler, SelectionKey.OP_READ)));
				}
				// Check if there are messages to send
				if (selKey.isValid() && selKey.isWritable()) {
					ConnectionHandler handler = (ConnectionHandler) selKey.attachment();
					unregisterKey(selKey, SelectionKey.OP_WRITE);
					workers.execute(new Worker(new IOWorkerTask(handler, SelectionKey.OP_WRITE)));
				}
			}
		}
		stopReactor();
	}
	private void unregisterKey(SelectionKey skey, int interest) {
		if (skey != null && skey.isValid()) {
			int currentOps = skey.interestOps();
			if ((currentOps & interest) != 0) {
				skey.interestOps(currentOps & (~interest));
			}
		}
	}
	private void preSelect() {
		WorkerTask task = queue.poll();
		if (null != task) {
			task.execute();
		}
	}
	public synchronized void stopReactor() {
		if (!run)
			return;
		run = false;
		selector.wakeup();
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();

		// Process each key
		while (it.hasNext()) {
			it.remove();
			// Get the selection key
			SelectionKey selKey = (SelectionKey) it.next();
			Object attach = selKey.attachment();
			if (attach instanceof ConnectionHandler) {
				((ConnectionHandler) attach).closeConnection();
			}
		}
		try {
			selector.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static interface WorkerTask {
		void execute();
		void fail();
		void addWorkerName(Thread currentThread);
		void removeWorkerName(Thread currentThread);
	}
	public static abstract class AbstractWorkerTask implements WorkerTask {
		public abstract String getSuffix();
		public void addWorkerName(Thread currentThread) {
			final String name = currentThread.getName();
			if (!name.endsWith(getSuffix())) {
				currentThread.setName(name + getSuffix());
			}
		}
		public void removeWorkerName(Thread currentThread) {
			final String name = currentThread.getName();
			if (name.endsWith(getSuffix())) {
				currentThread.setName(name.substring(0, name.length() - getSuffix().length()));
			}
		}
	}
	private class OnConnectWorkerTask extends AbstractWorkerTask {
		final ConnectionHandler handler;
		final int interest;
		OnConnectWorkerTask(ConnectionHandler handler, int interest) {
			this.handler = handler;
			this.interest = interest;
		}
		public void execute() {
			switch (interest) {
			case SelectionKey.OP_CONNECT:
				handler.onConnect();
				break;
			}
		}

		public void fail() {
			handler.closeConnection();
		}
		public String getSuffix() {
			return "-onConnect";
		}
		
	}
	private class IOWorkerTask extends AbstractWorkerTask {
		final ConnectionHandler handler;
		final int interest;
		IOWorkerTask(ConnectionHandler handler, int interest) {
			this.handler = handler;
			this.interest = interest;
		}
		public void execute() {
			switch (interest) {
			case SelectionKey.OP_READ:
				handler.read();
				break;
			case SelectionKey.OP_WRITE:
				handler.write();
				break;
			}
		}

		public void fail() {
			handler.closeConnection();
		}
		public String getSuffix() {
			return "-io-" + handler.getName();
		}
		
	}
	public static class RegisterAcceptChannelWorkerTask extends AbstractWorkerTask {
		final ConnectionHandler handler;
		final Selector selector;
		final int interest;
		RegisterAcceptChannelWorkerTask(ConnectionHandler container, Selector selector, int interest) {
			this.handler = container;
			this.selector = selector;
			this.interest = interest;
		}
		public void execute() {
			SelectionKey key;
			try {
				key = handler.getChannel().register(selector, interest, handler);
				handler.notifyChannelRegister(key);
				handler.switchToReadOnlyMode();
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}

		public void fail() {
		}
		@Override
		public String getSuffix() {
			return "-register-channel-" + handler.getName();
		}
		
	}
	public static class RegisterChannelWorkerTask extends AbstractWorkerTask {
		final ChannelContainer container;
		final Selector selector;
		final int interest;
		RegisterChannelWorkerTask(ChannelContainer container, Selector selector, int interest) {
			this.container = container;
			this.selector = selector;
			this.interest = interest;
		}
		public void execute() {
			SelectionKey key;
			try {
				key = container.getChannel().register(selector, interest, container);
				container.notifyChannelRegister(key);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}

		public void fail() {
		}
		@Override
		public String getSuffix() {
			return "-register-channel-" + container.getName();
		}
		
	}
	private class Worker implements Runnable {
		final WorkerTask task;
		Worker(WorkerTask task) {
			this.task = task;
		}
		public void run() {
			final Thread currentThread = Thread.currentThread();
			task.addWorkerName(currentThread);
			try {
				task.execute();
			} catch (Exception e) {
				task.fail();
			} finally {
				task.removeWorkerName(currentThread);
			}
		}
		
	}
	public void addPendingTask(WorkerTask task) {
		if (!run)
			return;
		queue.offer(task);
		selector.wakeup();
	}

	public Selector getSelector() {
		return selector;
	}
}
