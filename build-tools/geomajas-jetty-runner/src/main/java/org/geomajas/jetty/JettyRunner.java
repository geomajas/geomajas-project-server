package org.geomajas.jetty;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyRunner {
	
	public static void main(String[] args) throws Exception {
		Server server = startServer();
		server.join();
	}

	public static Server startServer() throws Exception {
		Server server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8888);
		server.addConnector(connector);

		WebAppContext webApp = new WebAppContext();
		webApp.setContextPath("/");
		webApp.setWar("src/main/webapp");
		server.setHandler(webApp);
		server.start();
		return server;
	}

}
