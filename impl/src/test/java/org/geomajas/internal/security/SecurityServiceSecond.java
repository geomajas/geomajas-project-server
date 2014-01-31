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

package org.geomajas.internal.security;

import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityService;
import org.geomajas.security.allowall.AllowAllAuthorization;

/**
 * Security service which accepts "TEST" and "SECOND" as token, used for testing the security manager service.
 *
 * @author Joachim Van der Auwera
 */
public class SecurityServiceSecond implements SecurityService {
	public String getId() {
		return "FIRST";
	}

	public Authentication getAuthentication(String authenticationToken) {
		if ("TEST".equals(authenticationToken)) {
			Authentication authentication = new Authentication();
			authentication.setUserId("test");
			authentication.setAuthorizations(new BaseAuthorization[]{ new AllowAllAuthorization()});
			return authentication;
		}
		if ("SECOND".equals(authenticationToken)) {
			Authentication authentication = new Authentication();
			authentication.setUserId("second");
			authentication.setAuthorizations(new BaseAuthorization[]{ new AllowAllAuthorization()});
			return authentication;
		}
		return null;
	}
}
