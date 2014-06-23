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

import org.geomajas.command.CommandRequest;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Request for {@link org.geomajas.plugin.deskmanager.command.security.UpdateUserCommand}.
 * 
 * @author Jan De Moerloose
 *
 */
public class UpdateUserRequest implements CommandRequest {

	private static final long serialVersionUID = 115L;

	public static final String COMMAND = "UpdateUser";

	private UserDto userDto;

	private String password;

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
