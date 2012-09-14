/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.security;

import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.role.DeskmanagerAuthentication;
import org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerAuthorization;
import org.geomajas.plugin.deskmanager.service.common.GeodeskIdService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Deelt authenticatie objecten uit op basis van een authenticationToken.
 * 
 * @author Oliver May
 * 
 */
@Component("DeskmanagerSecurityService")
public class DeskmanagerSecurityService implements SecurityService {

	public static final String SERVICE_ID = "DM_SECURITY_SERVICE";

	@Autowired
	private AuthenticationTokenService tokenService;

	@Autowired
	private GeodeskIdService loketIdService;

	@Autowired
	private ApplicationContext applicationContext;

	public String getId() {
		return this.getClass().getName();
	}

	public Authentication getAuthentication(String authenticationToken) {
		if (authenticationToken == null || ("null").equals(authenticationToken)) {
			DeskmanagerAuthentication auth = new DeskmanagerAuthentication(createGastProfile());
			auth.setAuthorizations(new BaseAuthorization[] { new DeskmanagerAuthorization(auth.getProfile(),
					loketIdService.getGeodeskIdentifier(), applicationContext) });
			return auth;
		}
		return tokenService.getAuthentication(authenticationToken);
	}

	public String registerRole(Profile profile) {
		DeskmanagerAuthentication auth = new DeskmanagerAuthentication(profile);
		auth.setAuthorizations(new BaseAuthorization[] { new DeskmanagerAuthorization(auth.getProfile(),
				loketIdService.getGeodeskIdentifier(), applicationContext) });
		String token = tokenService.login(auth);
		return token;
	}

	public static Profile createGastProfile() {
		Profile profile = new Profile();
		profile.setRole(Role.GUEST);

		return profile;
	}
}
