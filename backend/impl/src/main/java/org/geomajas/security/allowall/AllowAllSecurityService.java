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

package org.geomajas.security.allowall;

import org.geomajas.annotation.Api;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityService;

/**
 * Security Service which allows all access to everybody.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
public class AllowAllSecurityService implements SecurityService {

	private static final BaseAuthorization[] AUTHORIZATIONS = new BaseAuthorization[] { new AllowAllAuthorization() };

	public String getId() {
		return "AllowAll";
	}

	public Authentication getAuthentication(String authenticationToken) {
		Authentication authentication = new Authentication();
		authentication.setUserId("anonymous");
		authentication.setAuthorizations(AUTHORIZATIONS);
		return authentication;
	}
}
