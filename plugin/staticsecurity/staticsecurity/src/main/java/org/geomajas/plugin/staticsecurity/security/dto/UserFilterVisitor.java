/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.security.dto;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Visitor pattern for a tree of {@link UserFilter} implementations.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
@UserImplemented
public interface UserFilterVisitor {

	/**
	 * Called when accept() is called on a custom {@link UserFilter}.
	 * 
	 * @param filter the filter
	 * @param extraData the data
	 * @return the data or a custom result object
	 */
	Object visit(UserFilter filter, Object extraData);

	/**
	 * Called when accept() is called on a custom {@link AndUserFilter}.
	 * 
	 * @param filter the filter
	 * @param extraData the data
	 * @return the data or a custom result object
	 */
	Object visit(AndUserFilter and, Object extraData);

	/**
	 * Called when accept() is called on a custom {@link OrUserFilter}.
	 * 
	 * @param filter the filter
	 * @param extraData the data
	 * @return the data or a custom result object
	 */
	Object visit(OrUserFilter or, Object extraData);

	/**
	 * Called when accept() is called on a custom {@link RoleUserFilter}.
	 * 
	 * @param filter the filter
	 * @param extraData the data
	 * @return the data or a custom result object
	 */
	Object visit(RoleUserFilter role, Object extraData);

	/**
	 * Called when accept() is called on a custom {@link AllUserFilter}.
	 * 
	 * @param filter the filter
	 * @param extraData the data
	 * @return the data or a custom result object
	 */
	Object visit(AllUserFilter all, Object extraData);
}
