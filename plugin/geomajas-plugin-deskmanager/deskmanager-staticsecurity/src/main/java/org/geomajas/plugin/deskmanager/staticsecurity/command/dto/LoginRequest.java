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

import org.geomajas.command.CommandRequest;

/**
 * Request the login of a user, by the given username and password.
 *
 * @author Oliver May
 */
public class LoginRequest implements CommandRequest {

	public static final String COMMAND = "command.deskmanager.staticsecurity.LoginCommand";

	private String username;
	private String password;

	/**
	 * Get the username of the user.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username of the user.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the password of the user.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password of the user.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
