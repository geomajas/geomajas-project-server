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

package org.geomajas.security;

import java.util.Date;
import java.util.Locale;

import org.geomajas.annotation.Api;

/**
 * Success object which is returned on successful authentication by the {@link SecurityManager}.
 * It contains some information about the authenticated user (all optional), and the authorization objects.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class Authentication implements UserInfo {

	private static final long serialVersionUID = 152L;

	private String securityServiceId;

	private Date validUntil;
	private Date invalidAfter;
	private long extendValid;

	private String userId;
	private String userName;
	private Locale userLocale;
	private String userOrganization;
	private String userDivision;

	private BaseAuthorization[] authorizations;

	/**
	 * Get the id of the {@link org.geomajas.security.SecurityService} which created this authentication.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself.
	 *
	 * @return id of the security service which created this authentication
	 */
	public String getSecurityServiceId() {
		return securityServiceId;
	}

	/**
	 * Set the id of the {@link org.geomajas.security.SecurityService} which created this authentication.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself.
	 *
	 * @param securityServiceId id of the security service which created this authentication
	 */
	public void setSecurityServiceId(String securityServiceId) {
		this.securityServiceId = securityServiceId;
	}

	/**
	 * Get deadline for validity of the authentication.
	 * <p/>
	 * This is used for possible caching of the authentication result.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself if "extendValid" is set. If neither is set, the authentication is rechecked on each attempt to create
	 * the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @return timestamp until when the authtication may be cached
	 */
	public Date getValidUntil() {
		return validUntil;
	}

	/**
	 * Set deadline for validity of the authentication.
	 * <p/>
	 * This can be used by the security manager for possible caching of the authentication result.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself if "extendValid" is set. If neither is set, the authentication is rechecked on each attempt to create
	 * the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @param validUntil deadline until which the authentication is valid
	 */
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	/**
	 * Get final deadline for the validity of this authentication.
	 * <p/>
	 * On each attempt to check the validity of the authentication, the cached valued is searched first. This is used
	 * as long as validUntil is not expired. At that time, validUntil is also update to
	 * "min(max(xalidUntil, now()+extendValid), invalidAfter)".
	 *
	 * @return final timestamp, validUntil can not be extended beyond this
	 */
	public Date getInvalidAfter() {
		return invalidAfter;
	}

	/**
	 * Set the final deadline for the validity of the authentication.
	 * <p/>
	 * On each attempt to check the validity of the authentication, the cached valued is searched first. This is used
	 * as long as validUntil is not expired. At that time, validUntil is also update to
	 * "min(max(xalidUntil, now()+extendValid), invalidAfter)".
	 *
	 * @param invalidAfter final deadline for the validity of this authentication
	 */
	public void setInvalidAfter(Date invalidAfter) {
		this.invalidAfter = invalidAfter;
	}

	/**
	 * Get the period (in ms) by which the validity of the authentication is extended when it is accessed.
	 * <p/>
	 * This can never extend the validity beyond the timestamp in invalidAfter.
	 *
	 * @return number of ms to extend validUntil when authentication is accessed
	 */
	public long getExtendValid() {
		return extendValid;
	}

	/**
	 * Set the period (in ms) by which the validity of the authentication is extended when it is accessed.
	 * <p/>
	 * This can never extend the validity beyond the timestamp in invalidAfter.
	 *
	 * @param extendValid the period (in ms) by which the validity of the authentication is extended when it is accessed
	 */
	public void setExtendValid(long extendValid) {
		this.extendValid = extendValid;
	}

	/**
	 * Get the user id if known.
	 *
	 * @return user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the user id. This value is optional and may be null.
	 *
	 * @param userId user id or null when not known
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
	 * Set the user name. This value is optional and may be null.
	 *
	 * @param userName full name of user or null when not known
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
	 * Set the locale for the user. This value is optional and may be null.
	 *
	 * @param userLocale locale for the user or null when not known
	 */
	public void setUserLocale(Locale userLocale) {
		this.userLocale = userLocale;
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
	 * Get the organization for the user if known.
	 *
	 * @param userOrganization organization for the user or null when not known
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
	 * Get the division of the users organization if known.
	 *
	 * @param userDivision organizational division for the user or null when not known
	 */
	public void setUserDivision(String userDivision) {
		this.userDivision = userDivision;
	}

	/**
	 * Get array of authorizations which apply for the user. In many cases there is one object for each role.
	 * A union is used to combine these authorizations (and any other which may apply for the authentication token).
	 *
	 * @return array of {@link org.geomajas.security.BaseAuthorization} objects
	 */
	public BaseAuthorization[] getAuthorizations() {
		return authorizations;
	}

	/**
	 * Set the {@link org.geomajas.security.Authorization}s which apply for this authentication.
	 *
	 * @param authorizations array of authentications
	 */
	public void setAuthorizations(BaseAuthorization[] authorizations) {
		this.authorizations = authorizations;
	}
}
