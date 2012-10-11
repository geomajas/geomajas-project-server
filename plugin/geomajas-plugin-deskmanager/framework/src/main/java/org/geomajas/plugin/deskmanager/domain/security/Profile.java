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
package org.geomajas.plugin.deskmanager.domain.security;

import java.io.Serializable;
import java.util.List;

import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

/**
 * A user profile for the deskmanager, consisting of a role and territory. The role defines <b>what</b> a user can do,
 * the territory defines <b>where</b>. It also contains extra information about the user such as name and first name.
 * 
 * @author Oliver May
 * 
 */
public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	private Role role;

	private Territory territory;

	private transient String surname;

	private transient String name;

	private transient String id;

	private transient List<String> geodesks;

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstName() {
		return name;
	}

	public void setFirstName(String voornaam) {
		this.name = voornaam;
	}

	public String getId() {
		return id;
	}

	public void setId(String idmId) {
		this.id = idmId;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Return the territory. Note that Profile is most probably retrieved from a serialized state, thus some properties
	 * of Territory might not be loaded (lazy load). Use DeskmanagerSecurityContext.getGroup to retrieve the territory
	 * rebound to the database session.
	 * 
	 * @return the territory
	 */
	public Territory getTerritory() {
		return territory;
	}

	/**
	 * @param territory
	 *            the territory to set
	 */
	public void setTerritory(Territory group) {
		this.territory = group;
	}

	/**
	 * @return
	 */
	public String getRoleDescription() {
		return role.getDescription()/* + " " + territory.getBeschrijving() */;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Profile [role=" + role + ", territory=" + territory + ", surname=" + surname + ", name=" + name
				+ ", id=" + id + "]";
	}

	/**
	 * @param geodesks
	 *            the geodesks to set
	 */
	public void setGeodesks(List<String> loketten) {
		this.geodesks = loketten;
	}

	/**
	 * @return the geodesks
	 */
	public List<String> getGeodesks() {
		return geodesks;
	}

}
