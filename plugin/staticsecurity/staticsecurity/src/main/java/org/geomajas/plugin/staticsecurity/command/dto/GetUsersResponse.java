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

package org.geomajas.plugin.staticsecurity.command.dto;

import org.geomajas.command.CommandResponse;

import java.util.Set;

/**
 * Response object for {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.GetUsersCommand}.
 *
 * @author Joachim Van der Auwera
 */
public class GetUsersResponse extends CommandResponse {

	private static final long serialVersionUID = 190L;

	private Set<String> users;

	/**
	 * Get the set of user ids.
	 *
	 * @return user ids
	 */
	public Set<String> getUsers() {
		return users;
	}

	/**
	 * Set the set with user ids.
	 *
	 * @param users user ids
	 */
	public void setUsers(Set<String> users) {
		this.users = users;
	}
}
