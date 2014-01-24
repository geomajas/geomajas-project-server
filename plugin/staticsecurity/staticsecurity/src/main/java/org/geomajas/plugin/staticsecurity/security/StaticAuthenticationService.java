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

package org.geomajas.plugin.staticsecurity.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.command.staticsecurity.Base64;
import org.geomajas.plugin.staticsecurity.configuration.NamedRoleInfo;
import org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.dto.AllUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.AndUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.OrUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.RoleUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilterVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Authentication service which authenticates the users which are configured in
 * {@link org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo}.
 * 
 * @author Joachim Van der Auwera
 * @author Jand De Moerloose
 * @since 1.10.0
 */
@Component
@Api(allMethods = true)
public class StaticAuthenticationService implements AuthenticationService, UserDirectoryService {

	private final Logger log = LoggerFactory.getLogger(StaticAuthenticationService.class);

	private static final String PREFIX = "Geomajas is a wonderful framework";

	private static final String PADDING = "==";

	@Autowired
	private SecurityServiceInfo securityServiceInfo;

	@Override
	public String convertPassword(final String user, final String password) {
		String converted = encode(PREFIX + user + password);
		if (converted.endsWith(PADDING)) {
			converted = converted.substring(0, converted.length() - 2);
		}
		return converted;
	}

	@Override
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

	@Override
	public List<org.geomajas.security.UserInfo> getUsers(UserFilter userFilter) {
		List<org.geomajas.security.UserInfo> userInfos = new ArrayList<org.geomajas.security.UserInfo>();
		// evaluate the filter for each user
		FilterEvaluator evaluator = new FilterEvaluator();
		for (UserInfo userInfo : securityServiceInfo.getUsers()) {
			if ((Boolean) userFilter.accept(evaluator, userInfo)) {
				userInfos.add(userInfo);
			}
		}
		return userInfos;
	}
	
	/**
	 * Check if the specified filter is valid for the specified user. This method is called when this
	 * {@link UserDirectoryService} is extended with a custom filter class. Override this method to return the correct
	 * value.
	 * 
	 * @param filter the custom user filter
	 * @param userInfo the user info
	 * @return true if the filter allows the user
	 */
	public boolean evaluate(UserFilter filter, UserInfo userInfo) {
		log.warn("You should override the evaluate() method to support custom filtering!");
		return false;
	}
	
	/**
	 * {@link UserFilterVisitor} that evaluates the filter for a specific user.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class FilterEvaluator implements UserFilterVisitor {

		@Override
		public Object visit(UserFilter filter, Object extraData) {
			return evaluate(filter, (UserInfo) extraData);
		}

		@Override
		public Object visit(AndUserFilter and, Object extraData) {
			boolean result = true;
			for (UserFilter filter : and.getChildren()) {
				result &= (Boolean) filter.accept(this, extraData);
			}
			return result;
		}

		@Override
		public Object visit(OrUserFilter or, Object extraData) {
			boolean result = false;
			for (UserFilter filter : or.getChildren()) {
				result |= (Boolean) filter.accept(this, extraData);
			}
			return result;
		}

		@Override
		public Object visit(AllUserFilter all, Object extraData) {
			return true;
		}

		@Override
		public Object visit(RoleUserFilter roleFilter, Object extraData) {
			UserInfo info = (UserInfo) extraData;
			for (NamedRoleInfo role : info.getRoles()) {
				if (role.getName().equals(roleFilter.getName())) {
					return true;
				}
			}
			return false;
		}
		
	}
}
