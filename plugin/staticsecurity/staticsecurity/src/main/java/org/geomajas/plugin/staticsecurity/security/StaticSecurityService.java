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

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.security.Authentication;
import org.geomajas.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Security services which is uses user and policy data which is contained in the spring configuration files.
 *
 * @author Joachim Van der Auwera
 */
public class StaticSecurityService implements SecurityService {

	public static final String SECURITY_SERVICE_ID = "StaticSecurity";

	@Autowired
	private AuthenticationTokenService authenticationTokenService;

	@Override
	public String getId() {
		return SECURITY_SERVICE_ID; 
	}

	@Override
	public Authentication getAuthentication(String token) {
		return authenticationTokenService.getAuthentication(token);
	}
}
