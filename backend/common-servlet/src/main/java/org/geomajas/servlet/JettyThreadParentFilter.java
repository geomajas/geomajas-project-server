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
		if (ExtendedJettyClassLoader.isGwtJettyClassLoader(cl)) {
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
