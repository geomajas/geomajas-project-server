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
package org.geomajas.plugin.deskmanager.command.security.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.plugin.deskmanager.command.security.AuthenticateUserCommand}
 * and {@link org.geomajas.plugin.deskmanager.command.security.AuthenticateTokenCommand}.
 * 
 * @author Jan Venstermans
 * 
 */
public class AuthenticationResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private String username;

	private String authenticationToken;

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
