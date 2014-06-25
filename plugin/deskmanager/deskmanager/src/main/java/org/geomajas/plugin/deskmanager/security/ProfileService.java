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

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.domain.security.Profile;

/**
 * Provides a list of profiles that are valid for the current logged in user. These profiles can come from HTTP headers
 * or another database like single sign on.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ProfileService {

	/**
	 * Get a list of profiles that are available for the security token.
	 *
	 * @param securityToken securityToken for which profiles should be retrieved.
	 *
	 * @return the list of profiles, must not be null.
	 */
	List<Profile> getProfiles(String securityToken);

	/**
	 * Register profiles in a session and return a token.
	 *
	 * @param session
	 * @return token
	 */
	String registerProfilesForUser(LoginSession session);

	/**
	 * Create a defualt guest profile.
	 *
	 * @return the guest profile
	 */
	Profile createGuestProfile();
}
