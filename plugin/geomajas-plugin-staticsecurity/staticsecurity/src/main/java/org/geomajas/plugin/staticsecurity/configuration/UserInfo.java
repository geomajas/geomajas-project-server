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

package org.geomajas.plugin.staticsecurity.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.geomajas.annotation.Api;

/**
 * User security configuration information.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class UserInfo extends RoleInfo implements org.geomajas.security.UserInfo {

	private String userId;
	private String userName;
	private Locale userLocale;
	private String userOrganization;
	private String userDivision;
	private String password;
	private List<NamedRoleInfo> roles = new ArrayList<NamedRoleInfo>();

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
	 * Get encoded password.
	 * <p/>
	 * To encoded calculate a base64 encoded MD5 hash of "Geomajas is a wonderful framework" + userId + password.
	 * You can easily calculate this at http://progs.be/md5.html.
	 *
	 * @return encoded password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set encoded password.
	 * <p/>
	 * To encoded calculate a base64 encoded MD5 hash of "Geomajas is a wonderful framework" + userId + password.
	 * You can easily calculate this at http://progs.be/md5.html.
	 *
	 * @param password encoded password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override // need for @Api
	public List<AuthorizationInfo> getAuthorizations() {
		return super.getAuthorizations();
	}

	@Override // need for @Api
	public void setAuthorizations(List<AuthorizationInfo> authorizations) {
		super.setAuthorizations(authorizations);
	}
	
	/**
	 * Get role info for this user. If present this will be a partitioning of the user's authorizations in roles.
	 * 
	 * @return list of roles
	 * @since 1.10.0
	 */
	public List<NamedRoleInfo> getRoles() {
		return roles;
	}
	
	/**
	 * Set the role info for this user. If set, this will be used to set the user's authorizations.
	 * 
	 * @param roles the roles
	 * @since 1.10.0
	 */
	public void setRoles(List<NamedRoleInfo> roles) {
		this.roles = roles;
		List<AuthorizationInfo> authorizations = new ArrayList<AuthorizationInfo>();
		for (NamedRoleInfo role : roles) {
			authorizations.addAll(role.getAuthorizations());
		}
		super.setAuthorizations(authorizations);
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
		setUserLocale(null == locale ? null : new Locale(locale));
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
