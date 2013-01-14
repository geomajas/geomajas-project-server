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

package org.geomajas.plugin.staticsecurity.general;

import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.AuthenticationService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link AuthenticationService} for testing.
 *
 * @author Joachim Van der Auwera
 */
public class CustomAuthenticationService implements AuthenticationService {

	@NotNull
	private String userId;

	@NotNull
	private String password;

	@NotNull
	private AuthorizationInfo authorizationInfo;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
		this.authorizationInfo = authorizationInfo;
	}

	public String convertPassword(String user, String password) {
		return password;
	}

	public UserInfo isAuthenticated(String user, String convertedPassword) {
		if (userId.equals(user) && password.equals(convertedPassword)) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(userId);
			List<AuthorizationInfo> authorizations = new ArrayList<AuthorizationInfo>();
			authorizations.add(authorizationInfo);
			userInfo.setAuthorizations(authorizations);
			return userInfo;
		}
		return null;
	}
}
