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
 * Een gebruikersprofiel voor magdageo, bestaande uit een rol en een groep.
 * 
 * @author Oliver May
 * 
 */
public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	private Role role;

	private Territory group;

	private transient String familienaam;

	private transient String voornaam;

	private transient String idmId;

	private transient List<String> loketten;

	public String getSurname() {
		return familienaam;
	}

	public void setFamilienaam(String familienaam) {
		this.familienaam = familienaam;
	}

	public String getFirstName() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getIdmId() {
		return idmId;
	}

	public void setIdmId(String idmId) {
		this.idmId = idmId;
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
	 * Return the group. 
	 * Note that Profile is most probably retrieved from a serialized state, thus some properties of
	 * Territory might not be loaded (lazy load). Use DeskmanagerSecurityContext.getGroup to retrieve the group rebound
	 * to the database session.
	 * 
	 * @return the group
	 */
	public Territory getTerritory() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setTerritory(Territory group) {
		this.group = group;
	}

	/**
	 * @return
	 */
	public String getRolBeschrijving() {
		return role.getDescription()/* + " " + group.getBeschrijving() */;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Profile [role=" + role + ", group=" + group + ", familienaam=" + familienaam + ", voornaam=" + voornaam
				+ ", idmId=" + idmId + "]";
	}

	/**
	 * @param loketten the loketten to set
	 */
	public void setLoketten(List<String> loketten) {
		this.loketten = loketten;
	}

	/**
	 * @return the loketten
	 */
	public List<String> getLoketten() {
		return loketten;
	}

}
