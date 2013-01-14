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

import org.geomajas.annotation.Api;

/**
 * The security manager tries to find the authorization objects for an authentication token.
 * It can be used to create or clear the security context for the current thread.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface SecurityManager {

	/**
	 * Create security context for this thread, given the authentication token.
	 *
	 * @param authenticationToken authentication token which should set the security context
	 * @return true when a valid context was created, false when the token is not authenticated
	 */
	boolean createSecurityContext(String authenticationToken);

	/**
	 * Clear the security context for this thread.
	 */
	void clearSecurityContext();

	/**
	 * Restores the security context from saved authorizations.
	 *
	 * @param authorizations saved authorizations
	 * @since 1.9.0
	 */
	void restoreSecurityContext(SavedAuthorization authorizations);

}
