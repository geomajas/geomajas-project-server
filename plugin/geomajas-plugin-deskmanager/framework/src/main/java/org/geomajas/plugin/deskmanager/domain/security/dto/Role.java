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
package org.geomajas.plugin.deskmanager.domain.security.dto;

import org.geomajas.annotation.Api;

/**
 * TODO ...
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public enum Role {
	/**
	 * Guest.
	 */
	GUEST("Gast"),
	/**
	 * Geodesk manager, manages his own geodesks.
	 */
	DESK_MANAGER("Loketbeheerder"),
	/**
	 * Administrator of the system, manages all geodesks and blueprints.
	 */
	ADMINISTRATOR("Beheerder"),
	/**
	 * User with read rights on geodesks.
	 */
	CONSULTING_USER("Consulterend gebruiker"),
	/**
	 * User with read/write rights on geodesks.
	 */
	EDITING_USER("Editerend gebruiker");

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
