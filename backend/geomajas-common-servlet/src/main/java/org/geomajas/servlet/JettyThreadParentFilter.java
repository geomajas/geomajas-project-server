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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This filters replaces the Jetty web application classloader by a wrapper that returns a parent. Some libraries like
 * Geotools depend on the parent to prune the classloader tree.
 * 
 * @author Jan De Moerloose
 */
public class JettyThreadParentFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl instanceof URLClassLoader) {
			Thread.currentThread().setContextClassLoader(
					new ClassLoaderWrapper((URLClassLoader) cl, ClassLoader.getSystemClassLoader()));
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * A class loader with a parent for Geotools.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class ClassLoaderWrapper extends URLClassLoader {

		private URLClassLoader delegate;

		public ClassLoaderWrapper(URLClassLoader delegate, ClassLoader parent) {
			super(delegate.getURLs(), parent);
			this.delegate = (URLClassLoader) delegate;
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
