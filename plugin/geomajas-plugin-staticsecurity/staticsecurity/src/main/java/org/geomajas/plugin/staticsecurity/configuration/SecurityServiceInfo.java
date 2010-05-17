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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.global.Api;

import java.util.List;

/**
 * Base configuration data for the springsecurity security service.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SecurityServiceInfo {

	private List<UserInfo> users;

	/**
	 * Get information about existing users.
	 *
	 * @return list of {@link UserInfo} objects
	 */
	public List<UserInfo> getUsers() {
		return users;
	}

	/**
	 * Set the list of users which can be used by springsecurity.
	 *
	 * @param users list of {@link UserInfo} objects
	 */
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}
}
