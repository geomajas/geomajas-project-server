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
package org.geomajas.plugin.deskmanager.domain.security.dto;

import org.geomajas.annotation.Api;

/**
 * Definition of a user role in the geomajas deskmanager. The role defines <b>what</b> a user can do.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public enum Role {
	/**
	 * User that is not assigned a role, only useful for the first commands.
	 */
	UNASSIGNED("Unassigned"),
	/**
	 * Guest.
	 */
	GUEST("Gast"),
	/**
	 * Geodesk manager, manages his own geodesks.
	 */
	DESK_MANAGER("Deskmanager"),
	/**
	 * Administrator of the system, manages all geodesks and blueprints.
	 */
	ADMINISTRATOR("Administrator"),
	/**
	 * User with read rights on geodesks.
	 */
	CONSULTING_USER("Geodesk user"),
	/**
	 * User with read/write rights on geodesks.
	 */
	EDITING_USER("Geodesk user with editing permissions");

	private final String description;

	private Role(String description) {
		this.description = description;
	}

	/**
	 * The description of the role.
	 * @return
	 */
	public String getDescription() {
		return description;
	}

}
