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

package org.geomajas.plugin.staticsecurity.command.staticsecurity;

import org.geomajas.command.Command;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.AuthenticationTokenService;
import org.geomajas.security.Authentication;
import org.geomajas.security.BaseAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to obtain a login token for a user/password combination.
 * <p/>
 * When comparing passwords, it assures that the base64 encoding padding is not required to be used.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class LoginCommand implements Command<LoginRequest, LoginResponse> {

	private final Logger log = LoggerFactory.getLogger(LoginCommand.class);

	private static final String PREFIX = "Geomajas is a wonderful framework";
	private static final String PADDING = "==";

	@Autowired
	private SecurityServiceInfo securityServiceInfo;

	@Autowired
	private AuthenticationTokenService tokenService;

	public LoginResponse getEmptyCommandResponse() {
		return new LoginResponse();
	}

	public void execute(LoginRequest request, LoginResponse response) throws Exception {
		String login = request.getLogin();
		String password = request.getPassword();

		if (null == login || null == password || "".equals(login) || "".equals(password)) {
			// need both user name and password to login
			return;
		}

		password = encode(PREFIX + login + password);
		if (password.endsWith(PADDING)) {
			password = password.substring(0, password.length() - 2);
		}

		for (UserInfo user : securityServiceInfo.getUsers()) {
			String userpw = user.getPassword();
			if (null != userpw && userpw.endsWith(PADDING)) {
				userpw = userpw.substring(0, userpw.length() - 2);
			}
			if (login.equals(user.getUserId()) && password.equals(userpw)) {
				Authentication authentication = new Authentication();
				authentication.setUserId(login);
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

	private String encode(String plaintext) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plaintext.getBytes("UTF-8"));
			return Base64.encodeBytes(md.digest());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
}
