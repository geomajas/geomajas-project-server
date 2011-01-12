/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command.staticsecurity;

import org.geomajas.command.Command;
import org.geomajas.global.Api;
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
 * @since 1.7.1
 */
@Api
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
