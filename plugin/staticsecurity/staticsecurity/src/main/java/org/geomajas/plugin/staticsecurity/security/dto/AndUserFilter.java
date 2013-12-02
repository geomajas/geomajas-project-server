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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * {@link UserFilter} that represents a logical AND-operation of filters.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public class AndUserFilter extends AbstractUserFilter {

	private static final long serialVersionUID = 1100L;

	private List<UserFilter> children = new ArrayList<UserFilter>();

	@Override
	public Object accept(UserFilterVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	/**
	 * Get the child filters.
	 * @return children
	 */
	public List<UserFilter> getChildren() {
		return children;
	}

	/**
	 * Set the child filters.
	 * @param children the children
	 */
	public void setChildren(List<UserFilter> children) {
		this.children = children;
	}

	@Override
	public UserFilter and(UserFilter filter) {
		children.add(filter);
		return this;
	}

}
