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

package org.geomajas.security;

import org.geomajas.global.Api;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This SavedAuthentication class is a placeholder for the {@link Authorization}s and the providing
 * {@link SecurityService}. It does not contains actual authentication or user information.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public class SavedAuthentication implements Serializable {

	private static final long serialVersionUID = 190L;

	private String securityServiceId;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SavedAuthentication)) {
			return false;
		}

		SavedAuthentication that = (SavedAuthentication) o;

		return Arrays.equals(authorizations, that.authorizations) &&
				!(securityServiceId != null ? !securityServiceId.equals(that.securityServiceId) :
						that.securityServiceId != null);

	}

	@Override
	public int hashCode() {
		int result = securityServiceId != null ? securityServiceId.hashCode() : 0;
		result = 31 * result + (authorizations != null ? Arrays.hashCode(authorizations) : 0);
		return result;
	}
}
