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

/**
 * {@link UserFilter} that allows users in a certain role (as interpreted by the authentication service).
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
public class RoleUserFilter extends AbstractUserFilter {

	private static final long serialVersionUID = 1100L;

	private String name;

	public RoleUserFilter() {
	}

	public RoleUserFilter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Object accept(UserFilterVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

}
