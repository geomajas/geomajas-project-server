/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.security.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Create and manage/cache authentication tokens.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class AuthenticationTokenService {

	@Autowired
	private AuthenticationTokenGeneratorService generatorService;

	private Map<String, Authentication> tokens = new HashMap<String, Authentication>();

	public Authentication getAuthentication(String token) {
		return tokens.get(token);
	}

	public void logout(String token) {
		tokens.remove(token);
	}

	public String login(Authentication authentication) {
		String token = getToken();
		return login(token, authentication);
	}

	public String login(String token, Authentication authentication) {
		if (null == token) {
			return login(authentication);
		}
		tokens.put(token, authentication);
		return token;
	}

	public String getToken() {
		return "ss." + generatorService.get();
	}
}
