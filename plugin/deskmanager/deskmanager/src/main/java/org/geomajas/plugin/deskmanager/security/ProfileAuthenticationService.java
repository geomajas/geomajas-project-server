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

import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

/**
 * Authentication service for the first authentication stage. This service will return a list of user profiles.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface ProfileAuthenticationService {

	/**
	 * Check whether the user is authenticated and return user and his profiles if authentication succeeded.
	 * 
	 * @param user user id
	 * @param password (hashed) password
	 * @return user info (includes profiles) if login was successful
	 */
	UserDto isAuthenticated(String user, String password);

}
