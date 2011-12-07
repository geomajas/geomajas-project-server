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

package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.plugin.staticsecurity.command.staticsecurity.Base64;
import org.geomajas.plugin.staticsecurity.command.staticsecurity.LoginCommand;
import org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Authentication service which authenticates the users which are configured in
 * {@link org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo}.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class StaticAuthenticationService implements AuthenticationService {

	private final Logger log = LoggerFactory.getLogger(LoginCommand.class);

	private static final String PREFIX = "Geomajas is a wonderful framework";
	private static final String PADDING = "==";

	@Autowired
	private SecurityServiceInfo securityServiceInfo;

	/** {@inheritDoc} */
	public String convertPassword(final String user, final String password) {
		String converted = encode(PREFIX + user + password);
		if (converted.endsWith(PADDING)) {
			converted = converted.substring(0, converted.length() - 2);
		}
		return converted;
	}

	/** {@inheritDoc} */
	public UserInfo isAuthenticated(String login, String convertedPassword) {
		List<UserInfo> users = securityServiceInfo.getUsers();
		if (null != users) {
		for (UserInfo user : users) {
			String userPassword = user.getPassword();
			if (null != userPassword && userPassword.endsWith(PADDING)) {
				userPassword = userPassword.substring(0, userPassword.length() - 2);
			}
			if (login.equals(user.getUserId()) && convertedPassword.equals(userPassword)) {
				return user;
			}
		}
		}
		return null;
	}

	private String encode(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes("UTF-8"));
			return Base64.encodeBytes(md.digest());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
}
