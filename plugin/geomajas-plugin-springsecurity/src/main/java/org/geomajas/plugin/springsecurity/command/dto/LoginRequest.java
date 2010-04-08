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

package org.geomajas.plugin.springsecurity.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.global.Api;

/**
 * Request object for the login command.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class LoginRequest implements CommandRequest {

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
