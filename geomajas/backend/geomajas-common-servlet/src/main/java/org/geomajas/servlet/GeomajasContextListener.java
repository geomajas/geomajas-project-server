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
package org.geomajas.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.geomajas.spring.ReconfigurableClassPathApplicationContext;

/**
 * Initialise the servlet context. This assures the Spring application context is created and stored in the servlet
 * context. The Geomajas configuration is also read and also stored in the servlet context.
 * <p/>
 * The spring application context can be extended by adding locations (on the classpath) where addition configuration
 * files are found. They should be defined in the "contextConfigLocation" context-param to the context instance and the
 * entry can contain multiple files, separated by commas or whitespace.
 * <p/>
 * Note: In case of multiple config locations, later bean definitions will override ones defined in earlier loaded
 * files. This can be leveraged to deliberately override certain bean definitions via an extra XML file.
 * 
 * @author Joachim Van der Auwera
 */
public class GeomajasContextListener implements ServletContextListener {

	// private final Logger log = LoggerFactory.getLogger(GeomajasContextListener.class);

	/** Name of servlet context parameter that can specify additional config locations for the spring context. */
	public static final String CONFIG_LOCATION_PARAMETER = "contextConfigLocation";

	public void contextInitialized(ServletContextEvent servletContextEvent) {

		ServletContext servletContext = servletContextEvent.getServletContext();

		// create Spring context
		String configLocation = "org/geomajas/spring/geomajasContext.xml";
		String additionalLocations = servletContext.getInitParameter(CONFIG_LOCATION_PARAMETER);
		if (additionalLocations != null) {
			configLocation += "," + additionalLocations;
		}
		ReconfigurableClassPathApplicationContext applicationContext = new ReconfigurableClassPathApplicationContext(
				configLocation);
		ApplicationContextUtils.setApplicationContext(servletContext, applicationContext);
	}

	/**
	 * Close the root web application context.
	 * 
	 * @param servletContextEvent
	 *            servlet context event
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// nothing to do
	}

}
