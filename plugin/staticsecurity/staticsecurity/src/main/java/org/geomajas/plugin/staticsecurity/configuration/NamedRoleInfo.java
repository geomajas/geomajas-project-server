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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;

/**
 * An authority is a named role.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 * 
 */
@Api(allMethods = true)
public class NamedRoleInfo extends RoleInfo {

	private String name;

	/**
	 * Get the name of the authority.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the authority.
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
