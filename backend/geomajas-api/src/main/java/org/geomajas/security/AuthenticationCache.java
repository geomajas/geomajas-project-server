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

package org.geomajas.security;

import org.geomajas.global.Api;

/**
 * Authentication cache service.
 * <p/>
 * Used to cache authentication results.
 *
 * @author Joachim Van der Auwera
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
