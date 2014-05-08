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

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.security.UserInfo;

/**
 * Deskmanager user has a role and territory.
 * 
 * @author Jan De Moerloose
 * 
 * @since 1.15.0
 */
@Api(allMethods = true)
@UserImplemented
public interface DeskmanagerUserInfo extends UserInfo {

	/**
	 * Get the territory of this user.
	 * 
	 * @return the user's territory
	 */
	Territory getTerritory();

	/**
	 * Get the role of this user.
	 * 
	 * @return the user's role
	 */
	Role getRole();

	/**
	 * Get the full name of this user.
	 * 
	 * @return the full name
	 */
	String getFullName();

}
