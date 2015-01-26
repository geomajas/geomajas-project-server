/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo;
import org.geomajas.security.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create and manage/cache authentication tokens.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class AuthenticationTokenService {

	private static final long MS_IN_S = 1000;

	@Autowired
	private AuthenticationTokenGeneratorService generatorService;

	@Autowired
	private SecurityServiceInfo securityServiceInfo;

	private final Map<String, TokenContainer> tokens = new ConcurrentHashMap<String, TokenContainer>();

	/**
	 * Get the authentication for a specific token.
	 *
	 * @param token token
	 * @return authentication if any
	 */
	public Authentication getAuthentication(String token) {
		if (null != token) {
			TokenContainer container = tokens.get(token);
			if (null != container) {
				if (container.isValid()) {
					return container.getAuthentication();
				} else {
					logout(token);
				}
			}
		}
		return null;
	}

	/**
	 * Remove the token from the list of possible tokens.
	 *
	 * @param token token to remove
	 */
	public void logout(String token) {
		tokens.remove(token);
	}

	/**
	 * Login for a specific authentication, creating a new token.
	 *
	 * @param authentication authentication to assign to token
	 * @return token
	 */
	public String login(Authentication authentication) {
		String token = getToken();
		return login(token, authentication);
	}

	/**
	 * Login for a specific authentication, creating a specific token if given.
	 *
	 * @param token token to use
	 * @param authentication authentication to assign to token
	 * @return token
	 */
	public String login(String token, Authentication authentication) {
		if (null == token) {
			return login(authentication);
		}
		tokens.put(token, new TokenContainer(authentication));
		return token;
	}

	private String getToken() {
		return "ss." + generatorService.get();
	}

	/**
	 * Invalidate tokens which have passed their lifetime. Note that tokens are also checked when the authentication is
	 * fetched in {@link #getAuthentication(String)}.
	 */
	@Scheduled(fixedRate = 60000) // run every minute
	public void invalidateOldTokens() {
		List<String> invalidTokens = new ArrayList<String>(); // tokens to invalidate
		for (Map.Entry<String, TokenContainer> entry : tokens.entrySet()) {
			if (!entry.getValue().isValid()) {
				invalidTokens.add(entry.getKey());
			}
		}
		for (String token : invalidTokens) {
			logout(token);
		}
	}

	/**
	 * Container for the data related with a token. Specifically the associated authentication and validity timestamp.
	 *
	 * @author Joachim Van der Auwera
	 */
	private final class TokenContainer {
		private final long validUntil;
		private final Authentication authentication;

		/**
		 * Create a token container.
		 *
		 * @param authentication authentication object
		 */
		public TokenContainer(Authentication authentication) {
			validUntil = System.currentTimeMillis() + MS_IN_S * securityServiceInfo.getTokenLifetime();
			this.authentication = authentication;
		}

		/**
		 * Determine whether this authentication is still valid.
		 *
		 * @return true when token is still valid
		 */
		public boolean isValid() {
			return System.currentTimeMillis() <= validUntil;
		}

		/**
		 * Get the {@link Authentication} object.
		 *
		 * @return authentication
		 */
		public Authentication getAuthentication() {
			return authentication;
		}
	}

}
