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

package org.geomajas.security;

import org.geomajas.annotation.Api;

/**
 * Authentication cache service.
 * <p/>
 * Used to cache authentication results.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface AuthenticationCache {

	/**
	 * Get the authentication information for an authentication token. Returns null when no valid cached information
	 * is found.
	 * <p/>
	 * This should also handle the update of the validUntil field in the {@link org.geomajas.security.Authentication}
	 * objects.
	 * <p/>
	 * When one the the authentication objects is no longer valid, none should be returned (and data purged from cache).
	 *
	 * @param token authentication token
	 * @return authentication details for this token or null when no valid cached data found
	 */
	Authentication[] getAuthentication(String token);

	/**
	 * Put authentication information in the cache for a authentication token.
	 *
	 * @param token authentication token
	 * @param authentications authentication details or null (which purges the data for the token from the cache)
	 */
	void putAuthentication(String token, Authentication[] authentications);
}
