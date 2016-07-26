/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.LoginCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LoginResponse extends CommandResponse {

	private static final long serialVersionUID = 160L;

	private String token;
	private String userId;
	private String userName;
	private String userLocale;
	private String userOrganization;
	private String userDivision;

	/**
	 * Get the token or null when the login failed.
	 *
	 * @return token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the token.
	 *
	 * @param token token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Get user name.
	 *
	 * @return user name
	 * @since 1.9.0
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set user name.
	 *
	 * @param userId user name
	 * @since 1.9.0
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get the users name if known.
	 *
	 * @return name of user or null when not known
	 * @since 1.9.0
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set the user (full) name.
	 *
	 * @param userName user full name
	 * @since 1.9.0
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get the users locale if known.
	 *
	 * @return locale for the user or null when not known
	 * @since 1.9.0
	 */
	public String getUserLocale() {
		return userLocale;
	}

	/**
	 * Set the user (default) locale.
	 *
	 * @param locale locale code as string
	 * @since 1.9.0
	 */
	public void setUserLocale(String locale) {
		userLocale = locale;
	}

	/**
	 * Set the organization for the user. This value is optional and may be null.
	 *
	 * @return organization for the user or null when not known
	 * @since 1.9.0
	 */
	public String getUserOrganization() {
		return userOrganization;
	}

	/**
	 * Set organization for the user.
	 *
	 * @param userOrganization organization
	 * @since 1.9.0
	 */
	public void setUserOrganization(String userOrganization) {
		this.userOrganization = userOrganization;
	}

	/**
	 * Get the organization's division for the user. This value is optional and may be null.
	 *
	 * @return organizational division for the user or null when not known
	 * @since 1.9.0
	 */
	public String getUserDivision() {
		return userDivision;
	}

	/**
	 * Set user division.
	 *
	 * @param userDivision user division
	 * @since 1.9.0
	 */
	public void setUserDivision(String userDivision) {
		this.userDivision = userDivision;
	}

}
