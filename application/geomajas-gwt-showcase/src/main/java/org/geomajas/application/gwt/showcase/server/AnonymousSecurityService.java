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

package org.geomajas.application.gwt.showcase.server;

import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.LogRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.geomajas.security.SecurityService;

/**
 * Security service that defines the authorizations for an anonymous user. It allows logging in and out, sending log
 * commands and getting the map configuration without any layers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AnonymousSecurityService implements SecurityService {

	/** {@inheritDoc} */
	public String getId() {
		return "showcase.AnonymousSecurityService";
	}

	/** {@inheritDoc} */
	public Authentication getAuthentication(String authenticationToken) {
		Authentication auth = new Authentication();
		auth.setAuthorizations(new BaseAuthorization[] { new AnonmyousAuthorization() });
		auth.setUserId("anonymous");
		return auth;
	}

	/**
	 * Authorization which only allows the login and logout commands and fetching an empty map.
	 */
	private static final class AnonmyousAuthorization implements BaseAuthorization {

		/** {@inheritDoc} */
		public String getId() {
			return "showcase.AnonmyousAuthorization";
		}

		/** {@inheritDoc} */
		public boolean isToolAuthorized(String toolId) {
			return false;
		}

		/** {@inheritDoc} */
		public boolean isCommandAuthorized(String commandName) {
			return LoginRequest.COMMAND.equals(commandName) || LogoutRequest.COMMAND.equals(commandName)
					|| LogRequest.COMMAND.equals(commandName) || GetConfigurationRequest.COMMAND.equals(commandName);
		}

		/** {@inheritDoc} */
		public boolean isLayerVisible(String layerId) {
			return false;
		}

		/** {@inheritDoc} */
		public boolean isLayerUpdateAuthorized(String layerId) {
			return false;
		}

		/** {@inheritDoc} */
		public boolean isLayerCreateAuthorized(String layerId) {
			return false;
		}

		/** {@inheritDoc} */
		public boolean isLayerDeleteAuthorized(String layerId) {
			return false;
		}
	}
}
