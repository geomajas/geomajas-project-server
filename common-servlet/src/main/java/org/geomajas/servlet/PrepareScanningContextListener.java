/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.servlet;

import java.net.URLClassLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

/**
 * Listener to prepare the class loader for Spring component scanning and circumvent GeoTools problem.
 * 
 * @author Jan De Moerloose
 */
public class PrepareScanningContextListener implements ServletContextListener {

	/** Name of servlet context parameter with a space, comma or semi-comma separated list of classes to preload. */
	public static final String PRELOAD_CLASSES_PARAMETER = "preloadClasses";

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext servletContext = servletContextEvent.getServletContext();
		String param = servletContext.getInitParameter(PRELOAD_CLASSES_PARAMETER);
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (ExtendedJettyClassLoader.isGwtJettyClassLoader(cl)) {
			Thread.currentThread()
					.setContextClassLoader(
							new ExtendedJettyClassLoader((URLClassLoader) cl, ClassLoader.getSystemClassLoader(),
									param == null));
		}
		if (param != null) {
			String[] preloadClasses = param.split("[\\s,;]+");
			for (String className : preloadClasses) {
				try {
					Class.forName(className);
				} catch (ClassNotFoundException e) {
					LoggerFactory.getLogger(PrepareScanningContextListener.class).warn("could not preload class", e);
					// ignore
				}
			}
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
