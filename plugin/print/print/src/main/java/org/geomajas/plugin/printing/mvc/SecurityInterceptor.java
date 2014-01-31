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
package org.geomajas.plugin.printing.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor establishes a Geomajas security context based on the user token parameter.
 * 
 * @author Jan De Moerloose
 * @deprecated use {@link org.geomajas.servlet.mvc.SecurityInterceptor} instead
 */
@Deprecated
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	private static final String USER_TOKEN_NAME = "userToken";

	@Autowired
	private SecurityManager securityManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		String userToken = request.getParameter(USER_TOKEN_NAME);
		// stop if invalid token
		return securityManager.createSecurityContext(userToken);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		securityManager.clearSecurityContext();
	}
}
