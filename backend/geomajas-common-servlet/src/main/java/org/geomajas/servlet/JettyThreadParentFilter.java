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
import java.net.URLClassLoader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
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

	private boolean includeSystemclasses;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl instanceof URLClassLoader) {
			Thread.currentThread().setContextClassLoader(
					new ExtendedJettyClassLoader((URLClassLoader) cl, ClassLoader.getSystemClassLoader(),
							includeSystemclasses));
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		String param = servletContext.getInitParameter(PrepareScanningContextListener.PRELOAD_CLASSES_PARAMETER);
		includeSystemclasses = (param == null);
	}

}
