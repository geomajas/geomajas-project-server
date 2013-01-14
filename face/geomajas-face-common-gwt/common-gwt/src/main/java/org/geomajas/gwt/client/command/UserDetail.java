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

package org.geomajas.gwt.client.command;

import org.geomajas.annotation.Api;

/**
 * Details about the current user (if any). This is mostly similar to the {@link org.geomajas.security.UserInfo}
 * definition, except that it doesn't use {@link java.util.Locale} as this is not allowed by GWT.
 *
 * @author Joachim Van der Auwera
 * @since 0.0.0
 */
@Api(allMethods = true)
public class UserDetail {

	private String userId;
	private String userName;
	private String userLocale;
	private String userOrganization;
	private String userDivision;

	/**
	 * Get user name.
	 *
	 * @return user name
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set user name.
	 *
	 * @param userId user name
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get the users name if known.
	 *
	 * @return name of user or null when not known
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set the user (full) name.
	 *
	 * @param userName user full name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Get the users locale if known.
	 *
	 * @return locale for the user or null when not known
	 */
	public String getUserLocale() {
		return userLocale;
	}

	/**
	 * Set the user (default) locale.
	 *
	 * @param locale locale code as string
	 */
	public void setUserLocale(String locale) {
		userLocale = locale;
	}

	/**
	 * Set the organization for the user. This value is optional and may be null.
	 *
	 * @return organization for the user or null when not known
	 */
	public String getUserOrganization() {
		return userOrganization;
	}

	/**
	 * Set organization for the user.
	 *
	 * @param userOrganization organization
	 */
	public void setUserOrganization(String userOrganization) {
		this.userOrganization = userOrganization;
	}

	/**
	 * Get the organization's division for the user. This value is optional and may be null.
	 *
	 * @return organizational division for the user or null when not known
	 */
	public String getUserDivision() {
		return userDivision;
	}

	/**
	 * Set user division.
	 *
	 * @param userDivision user division
	 */
	public void setUserDivision(String userDivision) {
		this.userDivision = userDivision;
	}
}