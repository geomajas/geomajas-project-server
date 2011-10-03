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

import org.geomajas.command.dto.LogRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityService;

/**
 * Special security service which always allows the login/logout commands, and nothing else.
 *
 * @author Joachim Van der Auwera
 */
public class LoginAllowedSecurityService implements SecurityService {

	public String getId() {
		return "staticsecurity.LoginAllowed";
	}

	public Authentication getAuthentication(String authenticationToken) {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new LoginAuthorization()});
		auth.setUserId("anynomous");
		return auth;
	}

	/**
	 * Authorization which only allows the login and logout commands.
	 */
	private static final class LoginAuthorization implements BaseAuthorization {

		public String getId() {
			return "staticsecurity.LoginAuthorization";
		}

		public boolean isToolAuthorized(String toolId) {
			return false;
		}

		public boolean isCommandAuthorized(String commandName) {
			return LoginRequest.COMMAND.equals(commandName) ||
					LogoutRequest.COMMAND.equals(commandName) ||
					LogRequest.COMMAND.equals(commandName);
		}

		public boolean isLayerVisible(String layerId) {
			return false;
		}

		public boolean isLayerUpdateAuthorized(String layerId) {
			return false;
		}

		public boolean isLayerCreateAuthorized(String layerId) {
			return false;
		}

		public boolean isLayerDeleteAuthorized(String layerId) {
			return false;
		}
	}
}
