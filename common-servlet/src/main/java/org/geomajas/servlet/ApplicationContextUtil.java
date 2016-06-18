/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.geomajas.annotation.Api;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Utility class to obtain the (Spring) applicationContext from the servlet.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 * @deprecated use {@link org.springframework.web.context.ContextLoaderListener}
 */
@Api(allMethods = true)
@Deprecated
public final class ApplicationContextUtil {

	private static final String GEOMAJAS_SPRING_CONTEXT = "Geomajas-Spring-context";

	private ApplicationContextUtil() {
	}

	/**
	 * Get the Geomajas application context.
	 *
	 * @param servletConfig servlet configuration
	 * @return application config
	 */
	public static ApplicationContext getApplicationContext(ServletConfig servletConfig) {
		return getApplicationContext(servletConfig.getServletContext());
	}

	/**
	 * Get the Geomajas application context.
	 *
	 * @param servletContext servlet context
	 * @return application config
	 */
	public static ApplicationContext getApplicationContext(ServletContext servletContext) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	/**
	 * Set the Geomajas application context.
	 *
	 * @param servletContext servlet context
	 * @param applicationContext application context
	 */
	public static void setApplicationContext(ServletContext servletContext, ApplicationContext applicationContext) {
		servletContext.setAttribute(GEOMAJAS_SPRING_CONTEXT, applicationContext);
	}
}
