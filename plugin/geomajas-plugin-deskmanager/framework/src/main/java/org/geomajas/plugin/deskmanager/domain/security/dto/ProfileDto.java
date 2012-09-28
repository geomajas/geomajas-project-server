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

import java.io.Serializable;

/**
 * A user profile for the deskmanager, consisting of a role and territory.
 * 
 * @author Oliver May
 * 
 */
public class ProfileDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Role role;

	private TerritoryDto territory;

	private String surname;

	private String name;

	private String id;

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the territory
	 */
	public TerritoryDto getTerritory() {
		return territory;
	}

	/**
	 * @param territory the territory to set
	 */
	public void setTerritory(TerritoryDto group) {
		this.territory = group;
	}

	/**
	 * @return
	 */
	public String getRoleDescription() {
		if (territory != null) {
			return "<b>" + role.getDescription() + "</b> (" + territory.getName() + ")";
		} else {
			return "<b>" + role.getDescription() + "</b>";
		}
	}
}
