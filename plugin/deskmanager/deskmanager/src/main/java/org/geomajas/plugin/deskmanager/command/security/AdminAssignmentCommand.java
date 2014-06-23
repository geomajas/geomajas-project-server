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
import org.geomajas.plugin.deskmanager.command.security.dto.AdminAssignmentRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.AdminAssignmentResponse;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;
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
 * Command for (un)assigning the administrator role to al ist of users.
 * 
 * @author Jan Venstermans
 * 
 */
@Transactional
@Component(AdminAssignmentRequest.COMMAND)
public class AdminAssignmentCommand implements Command<AdminAssignmentRequest, AdminAssignmentResponse> {

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private DtoConverterService converterService;

	private final Logger log = LoggerFactory.getLogger(AdminAssignmentCommand.class);

	@Override
	public AdminAssignmentResponse getEmptyCommandResponse() {
		return new AdminAssignmentResponse();
	}

	@Override
	public void execute(AdminAssignmentRequest request, AdminAssignmentResponse response) throws Exception {
		try {
			profileService.updateAdmins(converterService.getIds(request.getAddedAdmins()),
					converterService.getIds(request.getRemovedAdmins()));
			log.info("Added ADMINISTRATOR role to " + request.getAddedAdmins().size() +
				"users {" + usersToString(request.getAddedAdmins()) + "}," +
					" removed ADMINISTRATOR role from  " + request.getRemovedAdmins().size() +
					" users {" + usersToString(request.getRemovedAdmins()) + "}.");
		} catch (Exception ex) {
			// something wrong
		}
		// reload users
		List<User> users = userService.findAll(true);
		for (User user : users) {
			response.getUsers().add(converterService.toDto(user, true));
		}
	}

	private String usersToString(List<UserDto> users) {
		StringBuilder result = new StringBuilder();
		for (UserDto user : users) {
			result.append(user.getEmail() + " ");
		}
		return result.toString();
	}

}
