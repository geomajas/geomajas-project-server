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
package org.geomajas.plugin.deskmanager.command.manager;

import org.geomajas.command.Command;
import org.geomajas.global.ExceptionCode;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetManagerUserProfileRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetManagerUserProfileResponse;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO.
 * 
 * FIXME: merge with RetrieveRolesCommand
 * @author Jan De Moerloose
 * 
 */
@Component(GetManagerUserProfileRequest.COMMAND)
public class GetManagerUserProfileCommand implements
		Command<GetManagerUserProfileRequest, GetManagerUserProfileResponse> {

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private DtoConverterService dtoService;

	public void execute(GetManagerUserProfileRequest request, GetManagerUserProfileResponse response) throws Exception {
		Role role = (getProfile() != null ? getProfile().getRole() : null);
		if (Role.DESK_MANAGER.equals(role) || Role.ADMINISTRATOR.equals(role)) {
			response.setProfile(dtoService.toDto(getProfile()));
		} else {
			throw new GeomajasSecurityException(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID);
		}
	}

	public GetManagerUserProfileResponse getEmptyCommandResponse() {
		return new GetManagerUserProfileResponse();
	}

	// ----------------------------------------------------------

	private Profile getProfile() {
		return ((DeskmanagerSecurityContext) securityContext).getProfile();
	}

}
