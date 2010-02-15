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
package org.geomajas.gwt.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener to prepare the jetty class loader for Spring component scanning.
 * 
 * @author Jan De Moerloose
 * 
 */
public class JettyListener implements ServletContextListener {

	/** Name of servlet context parameter with a space, comma or semi-comma separated list of classes to preload. */
	public static final String PRELOAD_CLASSES_PARAMETER = "preloadClasses";

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext servletContext = servletContextEvent.getServletContext();
		String param = servletContext.getInitParameter(PRELOAD_CLASSES_PARAMETER);
		String[] preloadClasses = param.split("[\\s,;]+");
		for (String className : preloadClasses) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// ignore
			}
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
