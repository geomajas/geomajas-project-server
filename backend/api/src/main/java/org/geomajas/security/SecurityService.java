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
import org.geomajas.annotation.UserImplemented;

/**
 * Handle the mapping between an individual authentication store and policy store.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface SecurityService {

	/**
	 * Get the id of this security service. This can be used to check which security service authenticated a token,
	 * which may be useful when additional queries need to be made to backing (authentication or policy) services.
	 *
	 * @return id for this service
	 */
	String getId();

	/**
	 * Check whether the authentication token is valid. If so, get the authorization info and (optionally) some user
	 * information.
	 *
	 * @param authenticationToken authentication token
	 * @return {@link org.geomajas.security.Authentication} object or null if the token is invalid
	 */
	Authentication getAuthentication(String authenticationToken);
}
