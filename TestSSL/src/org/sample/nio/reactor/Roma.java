package org.sample.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.sample.nio.reactor.Reactor.RegisterChannelWorkerTask;
import org.sample.nio.util.NamedThreadFactory;

public class Roma {

	private ExecutorService kernals;
	private ExecutorService workers;
	private SelectorProvider provider;
	private int next = 0;
	private boolean dedicateAcceptor = false;
	private Reactor[] reactors;
	private List<ServerSocketChannel> servers;
	public Roma() {
		servers = new ArrayList<ServerSocketChannel>();
		provider = SelectorProvider.provider();
	}
	private void createSelectors() {
		reactors = new Reactor[Runtime.getRuntime().availableProcessors()];
		for (int pos = 0; pos < reactors.length; pos++) {
			try {
				reactors[pos] = new Reactor(createSelector(), workers);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private Selector createSelector() throws IOException {
		try {
			return provider.openSelector();
		} catch (NullPointerException e) {
			for (int i = 0; i < 5; i++) {
				try {
					return createSelector();
				} catch (NullPointerException ignore) {
				}
			}
		}
		throw new IOException("cannot open selector due to NPEx");
	}
	private void startSelector() {
		for (Reactor reactor : reactors) {
			kernals.execute(reactor);
		}
	}
	public Reactor nextReactor() {
		return (next < reactors.length ? reactors[next++] : reactors[next = (dedicateAcceptor ? 1 : 0)]);
	}
	private void startServer() {
		for (final ServerSocketChannel ssChannel : servers) {
			final ConnectionAcceptor connectionAcceptor = new ConnectionAcceptor(ssChannel, this);
			reactors[0].addPendingTask(new RegisterChannelWorkerTask(connectionAcceptor, reactors[0].getSelector(), SelectionKey.OP_ACCEPT));
		}
	}
	
	private void init() {
		kernals = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadFactory("roma-kernal", false));
		workers = Executors.newCachedThreadPool(new NamedThreadFactory("roma-worker", true));
		createSelectors();
	}
	/**
	 * Create a non-blocking server socket channel and bind to to the Reactor
	 * port
	 */
	public ServerSocketChannel bind(String hostname, int port) throws IOException {
		try {
			ServerSocketChannel ssChannel = provider.openServerSocketChannel();
			ssChannel.configureBlocking(false);			
			ssChannel.socket().bind(new InetSocketAddress(hostname, port));
			servers.add(ssChannel);
			dedicateAcceptor = true;
			return ssChannel;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	public ConnectionHandler connect(String hostname, int port) throws IOException {
		try {
			final SocketChannel sChannel = provider.openSocketChannel();
			sChannel.configureBlocking(false);
			final boolean isConnect = sChannel.connect(new InetSocketAddress(hostname, port));

			final Reactor reactor = nextReactor();
			final ConnectionHandler handler = ConnectionHandler.create(sChannel, reactor.getSelector(), null);
			if (isConnect) {
				reactor.addPendingTask(new RegisterChannelWorkerTask(handler, reactor.getSelector(), 0));
			} else {
				reactor.addPendingTask(new RegisterChannelWorkerTask(handler, reactor.getSelector(), SelectionKey.OP_CONNECT));
			}
			
			return handler;
		} catch (IOException e) {
			throw e;
		}
	}

	public void start() {
		init();
		
		startSelector();
		
		startServer();
	}
	public void shutdown() {
		for (final ServerSocketChannel ssChannel : servers) {
			try {
				ssChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		for (Reactor reactor : reactors) {
			reactor.stopReactor();
		}
		try {
			workers.shutdown();
			workers.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		} finally {
			workers.shutdownNow();
		}
		try {
			kernals.shutdown();
			kernals.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		} finally {
			kernals.shutdownNow();
		}
		
	}

}
