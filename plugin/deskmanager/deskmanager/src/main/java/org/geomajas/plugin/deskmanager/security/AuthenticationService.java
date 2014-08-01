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
package org.geomajas.plugin.deskmanager.security;

import org.geomajas.security.GeomajasSecurityException;

/**
 * Contains methods for authentication of users via token.
 *
 * @author Jan Venstermans
 */
public interface AuthenticationService {

	/**
	 * Check username/password authentication and return authentication token.
	 * If authenticated, the profiles of the user will be available in the
	 * {@link org.geomajas.plugin.deskmanager.security.ProfileService}.
	 *
	 * @param username user's identification
	 * @param password password of the user
	 * @return authenticationToken token for the login session of the user;
	 * will throw GeomajasSecurityException if username and password do not match.
	 */
	String authenticateUsernamePassword(String username, String password)
			throws GeomajasSecurityException;

	/**
	 * Returns the username for a specific authentication session token.
	 *
	 * @param authenticationSessionToken
	 * @return the username associated with the authentication session
	 * @throws GeomajasSecurityException when authenticationSessionToken does not exist
	 */
	String getUsernameFromToken(String authenticationSessionToken) throws GeomajasSecurityException;

	/**
	 * Remove the session from db linked with the provided token.
	 * Will also remove profiles from the {@link org.geomajas.plugin.deskmanager.security.ProfileService}
	 * that are linked to the {@paramref authenticationSessionToken}.
	 *
	 * @param authenticationSessionToken
	 */
	void removeAuthenticationSession(String authenticationSessionToken);

}
