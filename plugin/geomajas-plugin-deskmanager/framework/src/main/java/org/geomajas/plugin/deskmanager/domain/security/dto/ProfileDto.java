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
 * Een gebruikersprofiel voor magdageo, bestaande uit een rol en een groep.
 * 
 * @author Oliver May
 * 
 */
public class ProfileDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Role role;

	private TerritoryDto group;

	private String familienaam;

	private String voornaam;

	private String idmId;

	public String getFamilienaam() {
		return familienaam;
	}

	public void setFamilienaam(String familienaam) {
		this.familienaam = familienaam;
	}

	public String getVoornaam() {
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
	 * @return the group
	 */
	public TerritoryDto getTerritory() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setTerritory(TerritoryDto group) {
		this.group = group;
	}

	/**
	 * @return
	 */
	public String getRolBeschrijving() {
		if (group != null) {
			return "<b>" + role.getDescription() + "</b> (" + group.getName() + ")";
		} else {
			return "<b>" + role.getDescription() + "</b>";
		}
	}
}
