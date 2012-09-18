package org.geomajas.plugin.staticsecurity.security;

import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;

public class CustomAuthenticationService extends StaticAuthenticationService {

	@Override
	public boolean evaluate(UserFilter filter, UserInfo userInfo) {
		CustomFilter cf = (CustomFilter) filter;
		return userInfo.getUserName().startsWith(cf.getUserNamePref());
	}

}
