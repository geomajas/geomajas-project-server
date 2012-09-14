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
package org.geomajas.plugin.deskmanager.domain.dto;

import java.io.Serializable;

/**
 * 
 * @author Oliver May
 *
 */
public class NaceActiviteitDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String nacebelCode;

	private int versie;

	private String omschrijving;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNacebelCode() {
		return nacebelCode;
	}

	public void setNacebelCode(String nacebelCode) {
		this.nacebelCode = nacebelCode;
	}

	public int getVersie() {
		return versie;
	}

	public void setVersie(int versie) {
		this.versie = versie;
	}

	public String getOmschrijving() {
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}
}
