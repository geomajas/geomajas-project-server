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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

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
		String[] preloadClasses = param.split("[\\s,;]+");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl instanceof URLClassLoader) {
			Thread.currentThread().setContextClassLoader(
					new ClassLoaderWrapper((URLClassLoader) cl, ClassLoader.getSystemClassLoader()));
		}
		for (String className : preloadClasses) {
			try {
				Class<?> c = Class.forName(className);
			} catch (ClassNotFoundException e) {
				LoggerFactory.getLogger(PrepareScanningContextListener.class).warn("could not preload class", e);
				// ignore
			}
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

	/**
	 * A class loader with a parent for Geotools.
	 * 
	 * @author Jan De Moerloose
	 */
	public class ClassLoaderWrapper extends URLClassLoader {

		private URLClassLoader delegate;

		public ClassLoaderWrapper(URLClassLoader delegate, ClassLoader parent) {
			super(delegate.getURLs(), parent);
			this.delegate = delegate;
		}

		public void clearAssertionStatus() {
			delegate.clearAssertionStatus();
		}

		public URL getResource(String name) {
			return delegate.getResource(name);
		}

		public InputStream getResourceAsStream(String name) {
			return delegate.getResourceAsStream(name);
		}

		public Enumeration<URL> getResources(String name) throws IOException {
			return delegate.getResources(name);
		}

		public Class<?> loadClass(String name) throws ClassNotFoundException {
			return delegate.loadClass(name);
		}

		public void setClassAssertionStatus(String className, boolean enabled) {
			delegate.setClassAssertionStatus(className, enabled);
		}

		public void setDefaultAssertionStatus(boolean enabled) {
			delegate.setDefaultAssertionStatus(enabled);
		}

		public void setPackageAssertionStatus(String packageName, boolean enabled) {
			delegate.setPackageAssertionStatus(packageName, enabled);
		}

		public URL findResource(String name) {
			return delegate.findResource(name);
		}

		public Enumeration<URL> findResources(String name) throws IOException {
			return delegate.findResources(name);
		}

		public URL[] getURLs() {
			return delegate.getURLs();
		}

	}

}
