/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.staticsecurity.security;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;
import org.geomajas.security.UserInfo;

/**
 * Implemented by {@link AuthenticationService} instances that provide access to their user directory.
 * 
 * @author Jan De Moerloose
 * @since 1.10
 * 
 */
@Api(allMethods = true)
public interface UserDirectoryService {
	
	/**
	 * Get the users in the specified roles, subject to the specified parameters.
	 * 
	 * @param filter filter to apply
	 * @return the list of users
	 */
	List<UserInfo> getUsers(UserFilter userFilter);
}
