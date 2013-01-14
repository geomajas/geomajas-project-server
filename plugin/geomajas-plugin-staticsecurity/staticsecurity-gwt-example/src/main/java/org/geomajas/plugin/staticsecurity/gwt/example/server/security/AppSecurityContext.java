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

package org.geomajas.plugin.staticsecurity.gwt.example.server.security;

import org.geomajas.internal.security.DefaultSecurityContext;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Custom security context for this application.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start AppSecurityContext, Custom security context, combine the authorizations
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AppSecurityContext extends DefaultSecurityContext implements AppAuthorization {

	// new authorization

	public boolean isBlablaButtonAllowed() {
		boolean allowed = false;
		for (Authentication authentication : getSecurityServiceResults()) {
			for (BaseAuthorization authorization : authentication.getAuthorizations()) {
				if (authorization instanceof AppAuthorization) {
					allowed |= ((AppAuthorization) authorization).isBlablaButtonAllowed();
				}
			}
		}
		return allowed;
	}

}
// @extract-end
