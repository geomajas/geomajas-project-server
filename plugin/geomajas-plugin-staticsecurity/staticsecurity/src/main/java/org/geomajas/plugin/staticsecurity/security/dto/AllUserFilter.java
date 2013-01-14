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

package org.geomajas.plugin.staticsecurity.security.dto;

import org.geomajas.annotation.Api;

/**
 * {@link UserFilter} that allows all users.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public class AllUserFilter extends AbstractUserFilter {

	private static final long serialVersionUID = 1100L;

	@Override
	public Object accept(UserFilterVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public UserFilter and(UserFilter filter) {
		return filter;
	}

	@Override
	public UserFilter or(UserFilter filter) {
		return this;
	}
	
	
	
}
