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
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Request for {@link org.geomajas.plugin.deskmanager.command.security.UserAssignmentCommand}.
 * 
 * @author Jan Venstermans
 *
 */
public class UserAssignmentRequest implements CommandRequest {

	private static final long serialVersionUID = 115L;

	public static final String COMMAND = "UserAssignment";

	private long userId;

	private List<ProfileDto> addedProfiles = new ArrayList<ProfileDto>();

	private List<ProfileDto> removedProfiles = new ArrayList<ProfileDto>();

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<ProfileDto> getAddedProfiles() {
		return addedProfiles;
	}

	public void setAddedProfiles(List<ProfileDto> addedProfiles) {
		this.addedProfiles = addedProfiles;
	}

	public List<ProfileDto> getRemovedProfiles() {
		return removedProfiles;
	}

	public void setRemovedProfiles(List<ProfileDto> removedProfiles) {
		this.removedProfiles = removedProfiles;
	}
}
