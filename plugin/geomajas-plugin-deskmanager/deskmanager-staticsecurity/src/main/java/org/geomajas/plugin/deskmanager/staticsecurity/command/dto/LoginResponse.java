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
package org.geomajas.plugin.deskmanager.staticsecurity.command.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response of the LoginCommand. Contains the token of the logged in user, or null if the credentials where invalid.
 *
 * @author Oliver May
 */
public class LoginResponse extends CommandResponse {

	private String token;

	/**
	 * The token of the logged in user.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
