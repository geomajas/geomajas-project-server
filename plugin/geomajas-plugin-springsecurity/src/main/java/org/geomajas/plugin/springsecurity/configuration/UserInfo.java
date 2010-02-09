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

package org.geomajas.plugin.springsecurity.configuration;

import java.util.List;
import java.util.Locale;

/**
 * User security configuration information.
 *
 * @author Joachim Van der Auwera
 */
public class UserInfo implements org.geomajas.security.UserInfo {

	private String userId;
	private String userName;
	private Locale userLocale;
	private String userOrganization;
	private String userDivision;
	private String password;
	private List<AuthorizationInfo> authorizations;

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
	 * Get password as encoded using "pwgen".
	 *
	 * @return encoded password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set password as encoded using the "pwgen" tool.
	 *
	 * @param password encoded password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get authorization info for user.
	 *
	 * @return authorization info
	 */
	public List<AuthorizationInfo> getAuthorizations() {
		return authorizations;
	}

	/**
	 * Set authorization info.
	 *
	 * @param authorizations authorization info
	 */
	public void setAuthorizations(List<AuthorizationInfo> authorizations) {
		this.authorizations = authorizations;
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
	public Locale getUserLocale() {
		return userLocale;
	}

	/**
	 * Set the user (default) locale.
	 *
	 * @param locale locale code as string
	 */
	public void setUserLocale(String locale) {
		setUserLocale(new Locale(locale));
	}

	/**
	 * Ser the user (default) locale.
	 *
	 * @param locale locale
	 */
	public void setUserLocale(Locale locale) {
		this.userLocale = locale;
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
