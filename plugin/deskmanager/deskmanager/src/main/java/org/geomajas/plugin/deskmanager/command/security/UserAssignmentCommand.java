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

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.security.dto.UserAssignmentRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.UserAssignmentResponse;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Command for (un)assigning profiles (TerritoryDto+Role) to a user.
 * 
 * @author Jan Venstermans
 * 
 */
@Transactional
@Component(UserAssignmentRequest.COMMAND)
public class UserAssignmentCommand implements Command<UserAssignmentRequest, UserAssignmentResponse> {

	@Autowired
	private ProfileService profileService;

	@Autowired
	private UserService userService;

	@Autowired
	private DtoConverterService converterService;

	private final Logger log = LoggerFactory.getLogger(UserAssignmentCommand.class);

	@Override
	public UserAssignmentResponse getEmptyCommandResponse() {
		return new UserAssignmentResponse();
	}

	@Override
	public void execute(UserAssignmentRequest request, UserAssignmentResponse response) throws Exception {
		profileService.updateUserProfileList(request.getUserId(), request.getAddedProfiles(),
				request.getRemovedProfiles());
		// reload profiles
		List<GroupMember> groupMemberList = userService.findGroupsOfUser(request.getUserId());
		for (GroupMember groupMember : groupMemberList) {
			response.getProfiles().add(converterService.toProfileDto(groupMember));
		}
		response.setUserId(request.getUserId());
		log.info("Updated the profiles of a user " + request.getUserId() +
				": Added " + request.getAddedProfiles().size() + " profiles, removed " +
				request.getRemovedProfiles().size() + " profiles, user now has " +
				response.getProfiles().size() + " profiles.");
	}

}
