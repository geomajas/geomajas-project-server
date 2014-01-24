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
package org.geomajas.plugin.deskmanager.security;

import java.util.UUID;

import org.geomajas.plugin.deskmanager.security.role.DeskmanagerAuthentication;
import org.geomajas.security.Authentication;
import org.geomajas.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Create and manage/cache authentication tokens.
 * 
 * @author Oliver May
 */
@Component
public class AuthenticationTokenService {
	
	@Autowired
	private CacheService cacheService;

	private final Logger log = LoggerFactory.getLogger(AuthenticationTokenService.class);

	public Authentication getAuthentication(String token) {
		log.debug("Getting authentication for token {}", token);
		return cacheService.get(AuthenticationTokenService.class.toString(), token, Authentication.class);
	}

	public void logout(String token) {
		cacheService.remove(AuthenticationTokenService.class.toString(), token);
	}

	public String login(DeskmanagerAuthentication authentication) {
		String token = getToken();
		return login(token, authentication);
	}

	private String login(String token, DeskmanagerAuthentication authentication) {
		log.debug("Registering token {} for authentication {}", token, authentication);
		if (null == token) {
			return login(authentication);
		}
		cacheService.put(AuthenticationTokenService.class.toString(), token, authentication);
		return token;
	}

	public String getToken() {
		return "gdm." + UUID.randomUUID().toString();
	}

}
