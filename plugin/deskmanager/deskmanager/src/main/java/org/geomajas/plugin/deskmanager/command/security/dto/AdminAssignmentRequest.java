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

import java.util.ArrayList;
import java.util.List;

/**
 * Request for {@link org.geomajas.plugin.deskmanager.command.security.AdminAssignmentCommand}.
 * 
 * @author Jan Venstermans
 *
 */
public class AdminAssignmentRequest implements CommandRequest {

	private static final long serialVersionUID = 115L;

	public static final String COMMAND = "AdminAssignment";

	private List<UserDto> addedAdmins = new ArrayList<UserDto>();

	private List<UserDto> removedAdmins = new ArrayList<UserDto>();

	public List<UserDto> getAddedAdmins() {
		return addedAdmins;
	}

	public void setAddedAdmins(List<UserDto> addedAdmins) {
		this.addedAdmins = addedAdmins;
	}

	public List<UserDto> getRemovedAdmins() {
		return removedAdmins;
	}

	public void setRemovedAdmins(List<UserDto> removedAdmins) {
		this.removedAdmins = removedAdmins;
	}
}
