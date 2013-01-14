/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Simple runner for starting a web application. Intended to fix problems when running GWT applications in eclipse.
 *
 * @author Jan De Moerloose
 */
public final class JettyRunner {

	private JettyRunner() {
		// hide constructor
	}

	public static void main(String[] args) throws Exception {
		Server server = startServer();
		server.join();
	}

	public static Server startServer() throws Exception {
		Server server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		int port = 8888;
		String portStr = System.getProperty("jetty.port");
		if (portStr != null) {
			port = Integer.parseInt(portStr);
		}
		connector.setPort(port);
		server.addConnector(connector);

		WebAppContext webApp = new WebAppContext();
		webApp.setContextPath("/");
		webApp.setWar("src/main/webapp");
		server.setHandler(webApp);
		server.start();
		return server;
	}

}
