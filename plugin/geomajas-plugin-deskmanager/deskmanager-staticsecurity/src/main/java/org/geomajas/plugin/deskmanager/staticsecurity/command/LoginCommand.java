/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.staticsecurity.command;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.staticsecurity.LoginService;
import org.geomajas.plugin.deskmanager.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.deskmanager.staticsecurity.command.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that logs in a user based on the given credentials.
 *
 * @author Oliver May
 */
@Component(LoginRequest.COMMAND)
public class LoginCommand implements Command<LoginRequest, LoginResponse> {

	@Autowired
	private LoginService loginService;

	@Override
	public LoginResponse getEmptyCommandResponse() {
		return new LoginResponse();
	}

	@Override
	public void execute(LoginRequest request, LoginResponse response) throws Exception {
		String token = loginService.login(request.getUsername(), request.getPassword());
		response.setToken(token);
	}
}
