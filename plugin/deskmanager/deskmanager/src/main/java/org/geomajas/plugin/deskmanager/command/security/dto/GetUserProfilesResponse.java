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

import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Response for {@link org.geomajas.plugin.deskmanager.command.security.GetUserProfilesCommand}.
 * 
 * @author Jan Venstermans
 * 
 */
public class GetUserProfilesResponse extends CommandResponse {

	private static final long serialVersionUID = 115L;

	private List<ProfileDto> profiles = new ArrayList<ProfileDto>();

	public List<ProfileDto> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<ProfileDto> profiles) {
		this.profiles = profiles;
	}
}