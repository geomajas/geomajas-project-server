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

import org.geomajas.geometry.Geometry;

/**
 * 
 * @author Oliver May
 *
 */
public class OndernemingsInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private Geometry geometry;

	private String vestigingOrOnderneming = "";

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVestigingOrOnderneming() {
		return vestigingOrOnderneming;
	}

	public void setVestigingOrOnderneming(String vestigingOrOnderneming) {
		this.vestigingOrOnderneming = vestigingOrOnderneming;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}
