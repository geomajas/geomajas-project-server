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
 * Abstract implementation of {@link UserFilter}.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public abstract class AbstractUserFilter implements UserFilter {

	@Override
	public UserFilter and(UserFilter filter) {
		AndUserFilter and = new AndUserFilter();
		and.and(this);
		and.and(filter);
		return and;
	}

	@Override
	public UserFilter or(UserFilter filter) {
		OrUserFilter or = new OrUserFilter();
		or.or(this);
		or.or(filter);
		return or;
	}

}
