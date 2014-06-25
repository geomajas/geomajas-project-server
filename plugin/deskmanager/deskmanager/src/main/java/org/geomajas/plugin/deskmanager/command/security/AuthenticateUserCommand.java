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
import org.geomajas.plugin.deskmanager.command.security.dto.AuthenticateUserRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.AuthenticateUserResponse;
import org.geomajas.plugin.deskmanager.security.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that will retrieve the profiles of a user for a specific geodesk.
 *
 * @author Jan Venstermans
 */
@Component(AuthenticateUserRequest.COMMAND)
public class AuthenticateUserCommand implements Command<AuthenticateUserRequest, AuthenticateUserResponse> {

	private final Logger log = LoggerFactory.getLogger(AuthenticateUserCommand.class);

	@Autowired
	private LoginService loginService;

	public AuthenticateUserResponse getEmptyCommandResponse() {
		return new AuthenticateUserResponse();
	}

	public void execute(AuthenticateUserRequest request, AuthenticateUserResponse response) throws Exception {
		try {
			String token = loginService.checkLogin(request.getUserName(), request.getPassword());
			response.setSecurityToken(token);
			log.info("Autentication Request for " + request.getUserName() + " successfully executed.");
		} catch (Exception ex) {
			log.error("Autentication exception for username " + request.getUserName(), ex);
			throw ex;
		}
	}
}