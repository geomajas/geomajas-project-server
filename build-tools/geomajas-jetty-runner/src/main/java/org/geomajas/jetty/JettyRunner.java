/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
