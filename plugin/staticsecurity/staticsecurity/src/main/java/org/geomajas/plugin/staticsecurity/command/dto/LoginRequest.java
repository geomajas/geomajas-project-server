/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.annotation.Api;

/**
 * Request object for the login command.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LoginRequest implements CommandRequest {

	private static final long serialVersionUID = 160L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.8.0
	 */
	public static final String COMMAND = "command.staticsecurity.Login";

	private String login;
	private String password;

	/**
	 * Get login (userId).
	 *
	 * @return user name
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Set login (userId).
	 *
	 * @param login login (userId)
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Get password for the user.
	 *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set password for the user.
	 *
	 * @param password password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
