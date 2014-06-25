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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Response for {@link org.geomajas.plugin.deskmanager.command.security.FindUsersCommand}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class FindUsersResponse extends CommandResponse {

	private static final long serialVersionUID = 115L;

	private List<UserDto> users = new ArrayList<UserDto>();

	public List<UserDto> getUsers() {
		return users;
	}

	public void setUsers(List<UserDto> users) {
		this.users = users;
	}

}
