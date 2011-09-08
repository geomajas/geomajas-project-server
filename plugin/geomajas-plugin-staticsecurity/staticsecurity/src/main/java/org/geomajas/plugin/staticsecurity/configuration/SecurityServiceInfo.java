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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;

import java.util.List;

/**
 * Base configuration data for the staticsecurity security service.
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
	 * Set the list of users which can be used by staticsecurity.
	 *
	 * @param users list of {@link UserInfo} objects
	 */
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}
}
