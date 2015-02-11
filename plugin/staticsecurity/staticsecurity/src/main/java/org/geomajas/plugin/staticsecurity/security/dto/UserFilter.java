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

package org.geomajas.plugin.staticsecurity.security.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * A filter that can be used to query users from a
 * {@link org.geomajas.plugin.staticsecurity.security.UserDirectoryService}.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public interface UserFilter extends Serializable {

	/**
	 * Accept a visitor.
	 * 
	 * @param filterVisitor the visitor
	 * @param extraData some extra data
	 * @return the extra data or a result object (depending on the visitor implementation).
	 */
	Object accept(UserFilterVisitor filterVisitor, Object extraData);

	/**
	 * Get a filter that is the logical AND of this filter and another filter.
	 * @param filter another filter
	 * @return the combined AND filter
	 */
	UserFilter and(UserFilter filter);

	/**
	 * Get a filter that is the logical OR of this filter and another filter.
	 * @param filter another filter
	 * @return the combined OR filter
	 */
	UserFilter or(UserFilter filter);
}
