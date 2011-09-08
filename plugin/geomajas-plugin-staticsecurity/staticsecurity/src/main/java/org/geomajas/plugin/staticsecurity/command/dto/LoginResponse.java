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

package org.geomajas.plugin.staticsecurity.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.annotation.Api;

/**
 * Response object for the login command.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LoginResponse extends CommandResponse {

	private static final long serialVersionUID = 160L;

	private String token;

	/**
	 * Get the token or null when the login failed.
	 *
	 * @return token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the token.
	 *
	 * @param token token
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
