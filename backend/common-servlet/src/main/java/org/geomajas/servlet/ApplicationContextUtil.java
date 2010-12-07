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

import org.geomajas.global.Api;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

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

	public static ApplicationContext getApplicationContext(ServletConfig servletConfig) {
		return getApplicationContext(servletConfig.getServletContext());
	}

	public static ApplicationContext getApplicationContext(ServletContext servletContext) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	public static void setApplicationContext(ServletContext servletContext, ApplicationContext applicationContext) {
		servletContext.setAttribute(GEOMAJAS_SPRING_CONTEXT, applicationContext);
	}
}
