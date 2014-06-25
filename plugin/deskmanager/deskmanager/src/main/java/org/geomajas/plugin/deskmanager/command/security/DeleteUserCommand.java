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
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteUserResponse;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for creating a user.
 * 
 * @author Jan De Moerloose
 * 
 */
@Transactional
@Component(DeleteUserRequest.COMMAND)
public class DeleteUserCommand implements Command<DeleteUserRequest, DeleteUserResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteUserCommand.class);

	@Autowired
	private UserService userService;

	@Override
	public DeleteUserResponse getEmptyCommandResponse() {
		return new DeleteUserResponse();
	}

	@Override
	public void execute(DeleteUserRequest request, DeleteUserResponse response) throws Exception {
		User user = userService.findById(request.getId());
		if (user != null) {
			userService.deleteUser(request.getId());
			log.info("Deleted user " + user.getEmail());
		} else {
			log.info("User with id " + request.getId() + " does not exist");
		}
	}

}
