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
package org.geomajas.plugin.staticsecurity.security;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;

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
	 * @param roles set of roles or (null or empty means all roles)
	 * @param parameters a custom set of query parameters (may be null)
	 * @return the list of users
	 */
	List<UserInfo> getUsers(Set<String> roles, Map<String, String> parameters);
}
