package org.test;

import java.io.IOException;
import java.net.URI;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.servlet.WebappContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;

public class GrizzlyWebServer {
	private static final Logger logger = LoggerFactory
			.getLogger(GrizzlyWebServer.class);
	private static final String HOST = "localhost";
	private static final int PORT = 8080;

	public static final void main(String[] args)
			throws IllegalArgumentException, IOException {
		HttpServer server = startWebServer();
		System.out.println("Jersey started");
		if (logger.isInfoEnabled()) {
			logger.info("Jersey web app started with Grizzly web container");
		}

		System.in.read();
		server.shutdown();
		System.exit(0);
	}

	/**
	 * Start the grizzly web container as described at
	 * http://blog.msbbc.co.uk/2008/11/java-using-jersey-and-grizzly-to-create.html
	 * 
	 * @return
	 * @throws IOException
	 */
	private static HttpServer startWebServer() throws IOException {
//		 SLF4JBridgeHandler.install();

		URI u = baseURI();

		HttpServer server = createHttpServer();

		WebappContext context = createWebappContext(u.getPath());
		context.deploy(server);
		server.start();
		return server;
	}

	private static URI baseURI() {
		return UriBuilder.fromUri("http://" + HOST).port(PORT).build();
	}

	private static final WebappContext createWebappContext(String path) {
		if (path == null)
			throw new NullPointerException(
					"Can not create WebappContext with a null context path");
		if ("/".equals(path))
			path = ""; // In order for Guice to work ok

		WebappContext context = new WebappContext(
				GrizzlyWebServer.class.getSimpleName() + "Context", path);

		// context.addListener(MediacdnServletContextListener.class);

		context.addFilter(GuiceFilter.class.getName(), GuiceFilter.class)
				.addMappingForUrlPatterns(null, "/*");

		return context;
	}

	// As per https://blogs.oracle.com/oleksiys/entry/grizzly_2_0_httpserver_api
	private static HttpServer createHttpServer() throws IOException {
		HttpServer server = new HttpServer();
		NetworkListener listener = new NetworkListener("grizzly-listener",
				HOST, PORT);
		listener.getFileCache().setEnabled(false);
		server.addListener(listener);

		server.getServerConfiguration().addHttpHandler(
				new StaticHttpHandler("WebContent/test/"), "/", "/test");

		return server;
	}
}
