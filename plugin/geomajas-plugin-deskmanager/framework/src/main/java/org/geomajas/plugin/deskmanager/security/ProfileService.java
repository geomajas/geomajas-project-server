/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.security;

import java.util.List;

import org.geomajas.plugin.deskmanager.domain.security.Profile;

/**
 * @author Oliver May
 * 
 */
public interface ProfileService {

	/**
	 * Get a list of profiles that are available for the current user.
	 * 
	 * @return the list of profiles.
	 */
	List<Profile> getProfiles();
}
