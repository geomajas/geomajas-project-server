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

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * This SavedAuthentication class is a placeholder for the {@link Authorization}s and the providing
 * {@link SecurityService}. It does not contains actual authentication or user information.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public interface SavedAuthentication extends Serializable {

	/**
	 * Get the id of the {@link org.geomajas.security.SecurityService} which created this authentication.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself.
	 *
	 * @return id of the security service which created this authentication
	 */
	String getSecurityServiceId();

	/**
	 * Set the id of the {@link org.geomajas.security.SecurityService} which created this authentication.
	 * <p/>
	 * It is not required that this is set by the authentication service. The authentication manager will set this
	 * itself.
	 *
	 * @param securityServiceId id of the security service which created this authentication
	 */
	void setSecurityServiceId(String securityServiceId);

	/**
	 * Get authorizations which apply for the user. In many cases there is one object for each role.
	 * A union is used to combine these authorizations (and any other which may apply for the authentication token).
	 *
	 * @return array of {@link org.geomajas.security.BaseAuthorization} objects
	 */
	BaseAuthorization[] getAuthorizations();

	/**
	 * Set the {@link org.geomajas.security.Authorization}s which apply for this authentication.
	 *
	 * @param authorizations array of authentications
	 */
	void setAuthorizations(BaseAuthorization[] authorizations);
}
