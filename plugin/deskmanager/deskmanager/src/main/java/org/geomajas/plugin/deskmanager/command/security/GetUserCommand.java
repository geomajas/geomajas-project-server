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
import org.geomajas.plugin.deskmanager.command.security.dto.GetUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GetUserResponse;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command for finding a user by it's id.
 * 
 * @author Jan Venstermans
 * 
 */
@Component(GetUserRequest.COMMAND)
public class GetUserCommand implements Command<GetUserRequest, GetUserResponse> {

	private final Logger log = LoggerFactory.getLogger(GetUserCommand.class);

	@Autowired
	private UserService userService;

	@Autowired
	private DtoConverterService converterService;

	@Override
	public GetUserResponse getEmptyCommandResponse() {
		return new GetUserResponse();
	}

	@Override
	public void execute(GetUserRequest request, GetUserResponse response) throws Exception {
		try {
			User user = userService.findById(request.getId());
			response.setUserDto(converterService.toDto(user, false));
			log.info("found user " + user);
		} catch (Exception e) {
			log.error("Unexpected error", e);
			throw e;
		}
	}

}
