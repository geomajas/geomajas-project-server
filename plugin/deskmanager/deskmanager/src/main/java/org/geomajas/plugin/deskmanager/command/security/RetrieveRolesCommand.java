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
package org.geomajas.plugin.deskmanager.command.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesResponse;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerAuthorization;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Oliver May
 * 
 *         Command that will retrieve current roles. The command only returns the roles that have access to the geodesk
 *         or manager interface from where the command is requested.
 * 
 */
@Component(RetrieveRolesRequest.COMMAND)
public class RetrieveRolesCommand implements Command<RetrieveRolesRequest, RetrieveRolesResponse> {

	private final Logger log = LoggerFactory.getLogger(RetrieveRolesCommand.class);

	@Autowired
	private DeskmanagerSecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private DtoConverterService dtoService;

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private ApplicationContext applicationContext;

	public RetrieveRolesResponse getEmptyCommandResponse() {
		return new RetrieveRolesResponse();
	}

	public void execute(RetrieveRolesRequest request, RetrieveRolesResponse response) throws Exception {

		HashMap<String, ProfileDto> profiles = new LinkedHashMap<String, ProfileDto>();

		String geodeskId = request.getGeodeskId();
		if (geodeskId == null) {
			Exception e = new IllegalArgumentException("Error retrieving roles: geodesk id is required.");
			log.error(e.getLocalizedMessage());
			throw e;
		// non-manager geodesk
		} else if (!RetrieveRolesRequest.MANAGER_ID.equals(geodeskId)) {
			response.setPublicGeodesk(geodeskService.isGeodeskPublic(geodeskId));
			List<Profile> profilesOfToken = profileService.getProfiles(request.getSecurityToken());
			if (profilesOfToken.size() == 0 && response.isPublicGeodesk()) {
				profilesOfToken = Arrays.asList(profileService.createGuestProfile());
			}
			for (Profile profile : profilesOfToken) {
				DeskmanagerAuthorization auth = new DeskmanagerAuthorization(profile, geodeskId, applicationContext);
				if (auth.isGeodeskUseAllowed(geodeskId)) {
					String token = securityService.registerRole(request.getGeodeskId(), profile);
					profiles.put(token, dtoService.toDto(profile));
				}
			}
		// manager geodesk
		} else if (RetrieveRolesRequest.MANAGER_ID.equals(geodeskId)) { // manager interface: ignore guest role
			for (Profile profile : profileService.getProfiles(request.getSecurityToken())) {
				if (!Role.GUEST.equals(profile.getRole())) {
					String token = securityService.registerRole(request.getGeodeskId(), profile);
					profiles.put(token, dtoService.toDto(profile));
				}
			}
		}
		response.setRoles(profiles);
	}
}
