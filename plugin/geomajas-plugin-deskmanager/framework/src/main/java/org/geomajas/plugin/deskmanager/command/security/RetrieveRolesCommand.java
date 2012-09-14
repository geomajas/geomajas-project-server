/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.security;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.geomajas.command.Command;
import org.geomajas.command.CommandRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesResponse;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerAuthorization;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Oliver May
 * 
 * Command that will retrieve current roles. The command only returns the roles that have access to the geodesk or
 * manager interface from where the command is requested.
 * 
 */
@Component(RetrieveRolesResponse.COMMAND)
public class RetrieveRolesCommand implements Command<CommandRequest, RetrieveRolesResponse> {

	@Autowired
	private DeskmanagerSecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private DtoConverterService dtoService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private GeodeskIdService loketIdService;

	public RetrieveRolesResponse getEmptyCommandResponse() {
		return new RetrieveRolesResponse();
	}

	public void execute(CommandRequest request, RetrieveRolesResponse response) throws Exception {

		HashMap<String, ProfileDto> profiles = new LinkedHashMap<String, ProfileDto>();

		String loketId = loketIdService.getGeodeskIdentifier();

		if (loketId != null) {
			for (Profile profile : profileService.getProfiles()) {
				DeskmanagerAuthorization auth = new DeskmanagerAuthorization(profile, loketId, applicationContext);
				if (auth.isLoketUseAllowed(loketId)) {
					String token = securityService.registerRole(profile);
					profiles.put(token, dtoService.toDto(profile));
				}
			}
		} else { //manager interface: ignore guest role
			for (Profile profile : profileService.getProfiles()) {
				if (!Role.GUEST.equals(profile.getRole())) {
					String token = securityService.registerRole(profile);
					profiles.put(token, dtoService.toDto(profile));
				}
			}
		}
		response.setRoles(profiles);
	}

}
