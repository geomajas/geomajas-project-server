/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.springsecurity.command;

import org.geomajas.command.Command;
import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.springsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.springsecurity.configuration.SecurityServiceInfo;
import org.geomajas.plugin.springsecurity.configuration.UserInfo;
import org.geomajas.plugin.springsecurity.security.AuthenticationTokenService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to obtain a login token for a user/password combination.
 *
 * @author Joachim Van der Auwera
 */
public class LoginCommand implements Command<LoginRequest, LoginResponse> {

	@Autowired
	private SecurityServiceInfo securityServiceInfo;

	@Autowired
	private AuthenticationTokenService tokenService;

	public LoginResponse getEmptyCommandResponse() {
		return new LoginResponse();
	}

	public void execute(LoginRequest request, LoginResponse response) throws Exception {
		String userName = request.getUserName();
		String password = request.getPassword();

		if (null == userName || null == password) {
			// need both user name and password to login
			return;
		}

		// @todo apply pwgen on the password for proper comparing

		for (UserInfo user : securityServiceInfo.getUsers()) {
			if (userName.equals(user.getUserId()) && password.equals(user.getPassword())) {
				Authentication authentication = new Authentication();
				authentication.setUserId(userName);
				authentication.setUserName(user.getUserName());
				authentication.setUserLocale(user.getUserLocale());
				authentication.setUserOrganization(user.getUserOrganization());
				authentication.setUserDivision(user.getUserDivision());
				authentication.setAuthorizations(getAuthorizations(user));
				response.setToken(tokenService.login(authentication));
			}
		}
	}

	private BaseAuthorization[] getAuthorizations(UserInfo user) {
		List<BaseAuthorization> res = new ArrayList<BaseAuthorization>();
		List<AuthorizationInfo> ua = user.getAuthorizations();
		if (null != ua) {
			for (AuthorizationInfo ai : ua) {
				res.add(ai.getAuthorization());
			}
		}
		return res.toArray(new BaseAuthorization[res.size()]);
	}
}
