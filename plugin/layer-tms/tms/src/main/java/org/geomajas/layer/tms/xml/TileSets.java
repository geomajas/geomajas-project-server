/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Represents the TileSets tag in the TMS description/capabilities XML file.
 * 
 * @author Pieter De Graef
 */
public class TileSets implements Serializable {

	private static final long serialVersionUID = 100L;

	private String profile;

	private List<TileSet> tileSets; // NOSONAR yes the name is the same, that is the point

	public TileSets() {
	}

	@XmlAttribute(name = "profile")
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@XmlElement(name = "TileSet")
	public List<TileSet> getTileSets() {
		return tileSets;
	}

	public void setTileSets(List<TileSet> tileSets) {
		this.tileSets = tileSets;
	}
}