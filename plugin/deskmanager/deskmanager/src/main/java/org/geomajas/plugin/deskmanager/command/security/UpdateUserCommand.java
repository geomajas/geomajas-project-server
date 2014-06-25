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
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateUserResponse;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for updating a user.
 * 
 * @author Jan De Moerloose
 * 
 */
@Transactional
@Component(UpdateUserRequest.COMMAND)
public class UpdateUserCommand implements Command<UpdateUserRequest, UpdateUserResponse> {

	@Autowired
	private UserService userService;

	@Autowired
	private DtoConverterService converterService;

	private final Logger log = LoggerFactory.getLogger(UpdateUserCommand.class);

	@Override
	public UpdateUserResponse getEmptyCommandResponse() {
		return new UpdateUserResponse();
	}

	@Override
	public void execute(UpdateUserRequest request, UpdateUserResponse response) throws Exception {
		userService.updateUser(converterService.fromDto(request.getUserDto(), false));
		// reload
		User user = userService.findById(request.getUserDto().getId());
		log.info("Updated user " + request.getUserDto().getEmail());
		if (request.getPassword() != null) {
			userService.changePassword(request.getUserDto().getId(), request.getPassword());
			log.info("Changed password of user " + request.getUserDto().getEmail());
		}
		response.setUser(converterService.toDto(user, false));
	}

}
