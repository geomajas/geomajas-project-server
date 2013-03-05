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
package org.geomajas.plugin.deskmanager.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.geomajas.plugin.deskmanager.security.role.DeskmanagerAuthentication;
import org.geomajas.security.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Create and manage/cache authentication tokens.
 * 
 * @author Oliver May
 */
@Component
public class AuthenticationTokenService {

	private final Logger log = LoggerFactory.getLogger(AuthenticationTokenService.class);

	private Map<String, DeskmanagerAuthentication> tokens = 
			new ConcurrentHashMap<String, DeskmanagerAuthentication>();

	public Authentication getAuthentication(String token) {
		log.debug("Getting authentication for token {}", token);
		return tokens.get(token);
	}

	public void logout(String token) {
		tokens.remove(token);
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
		tokens.put(token, authentication);
		return token;
	}

	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 3600000)
	private void cleanUp() {
		List<String> toBeRemoved = new ArrayList<String>();
		for (Entry<String, DeskmanagerAuthentication> token : tokens.entrySet()) {
			log.debug("Checking validity of authentication token {} {}", token.getKey(), token.getValue());
			if (token.getValue().getInvalidAfter().before(new Date())) {
				log.info("Authentication token {} expired, removing. {}", token.getKey(), token.getValue());
				toBeRemoved.add(token.getKey());
			}
		}
		for (String key : toBeRemoved) {
			logout(key);
		}
	}

	public String getToken() {
		return "gdm." + UUID.randomUUID().toString();
	}

}
