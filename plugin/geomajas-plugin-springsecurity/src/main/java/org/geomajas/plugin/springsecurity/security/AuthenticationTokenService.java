/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.springsecurity.security;

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
