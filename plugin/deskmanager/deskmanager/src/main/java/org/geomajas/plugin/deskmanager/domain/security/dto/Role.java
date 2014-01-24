/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
	UNASSIGNED("Unassigned", "Unassigned"),
	/**
	 * Guest.
	 */
	GUEST("Guest", "Guest"),
	/**
	 * Geodesk manager, manages his own geodesks.
	 */
	DESK_MANAGER("Deskmanager", "Deskmanager"),
	/**
	 * Administrator of the system, manages all geodesks and blueprints.
	 */
	ADMINISTRATOR("Administrator", "Administrator"),
	/**
	 * User with read rights on geodesks.
	 */
	CONSULTING_USER("Geodesk user", "ConsultingUser"),
	/**
	 * User with read/write rights on geodesks.
	 */
	EDITING_USER("Geodesk user with editing permissions", "Editor");

	private final String description;
	private final String key;

	private Role(String description, String key) {
		this.description = description;
		this.key = key;
	}

	/**
	 * The description of the role.
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public String getKey() {
		return key;
	}

}
