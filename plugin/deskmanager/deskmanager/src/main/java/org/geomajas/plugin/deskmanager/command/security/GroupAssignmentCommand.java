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
import org.geomajas.plugin.deskmanager.command.security.dto.GroupAssignmentRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GroupAssignmentResponse;
import org.geomajas.plugin.deskmanager.domain.security.User;
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
 * Command for (un)assigning UserDto+Role to a group.
 * 
 * @author Jan Venstermans
 * 
 */
@Transactional
@Component(GroupAssignmentRequest.COMMAND)
public class GroupAssignmentCommand implements Command<GroupAssignmentRequest, GroupAssignmentResponse> {

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private DtoConverterService converterService;

	private final Logger log = LoggerFactory.getLogger(GroupAssignmentCommand.class);

	@Override
	public GroupAssignmentResponse getEmptyCommandResponse() {
		return new GroupAssignmentResponse();
	}

	@Override
	public void execute(GroupAssignmentRequest request, GroupAssignmentResponse response) throws Exception {
		profileService.updateGroupAssignment(request.getTerritoryDto().getId(),
				request.getAddedProfiles(), request.getRemovedProfiles());
		// reload users
		List<User> users = userService.findAll(true);
		for (User user : users) {
			response.getUsers().add(converterService.toDto(user, true));
		}
		log.info("Updated the profiles of a number of users, for group " + request.getTerritoryDto());
	}

}
