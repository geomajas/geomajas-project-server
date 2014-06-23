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
import org.geomajas.plugin.deskmanager.command.security.dto.CreateUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.CreateUserResponse;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
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
@Component(CreateUserRequest.COMMAND)
public class CreateUserCommand implements Command<CreateUserRequest, CreateUserResponse> {

	private final Logger log = LoggerFactory.getLogger(CreateUserCommand.class);

	@Autowired
	private UserService userService;

	@Autowired
	private DtoConverterService converterService;

	@Override
	public CreateUserResponse getEmptyCommandResponse() {
		return new CreateUserResponse();
	}

	@Override
	public void execute(CreateUserRequest request, CreateUserResponse response) throws Exception {
		User user = userService.createUser(request.getName(), request.getSurname(), request.getEmail(),
				request.getPassword());
		log.info("Created user " + request.getEmail());
		response.setUser(converterService.toDto(user, false));
	}

}
