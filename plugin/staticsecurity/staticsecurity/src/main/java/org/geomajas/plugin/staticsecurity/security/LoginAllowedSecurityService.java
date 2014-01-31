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

	@Override
	public String getId() {
		return "staticsecurity.LoginAllowed";
	}

	@Override
	public Authentication getAuthentication(String authenticationToken) {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[]{new LoginAuthorization()});
		auth.setUserId("anonymous");
		return auth;
	}

	/**
	 * Authorization which only allows the login and logout commands.
	 */
	private static final class LoginAuthorization implements BaseAuthorization {

		@Override
		public String getId() {
			return "staticsecurity.LoginAuthorization";
		}

		@Override
		public boolean isToolAuthorized(String toolId) {
			return false;
		}

		@Override
		public boolean isCommandAuthorized(String commandName) {
			return LoginRequest.COMMAND.equals(commandName) ||
					LogoutRequest.COMMAND.equals(commandName) ||
					LogRequest.COMMAND.equals(commandName);
		}

		@Override
		public boolean isLayerVisible(String layerId) {
			return false;
		}

		@Override
		public boolean isLayerUpdateAuthorized(String layerId) {
			return false;
		}

		@Override
		public boolean isLayerCreateAuthorized(String layerId) {
			return false;
		}

		@Override
		public boolean isLayerDeleteAuthorized(String layerId) {
			return false;
		}
	}
}
