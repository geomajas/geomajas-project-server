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

package org.geomajas.layer.tms.configuration;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class TileSetsInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private String profile;

	private List<TileSetInfo> tileSets;

	public TileSetsInfo() {
	}

	@XmlAttribute(name = "profile")
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@XmlElement(name = "TileSet")
	public List<TileSetInfo> getTileSets() {
		return tileSets;
	}

	public void setTileSets(List<TileSetInfo> tileSets) {
		this.tileSets = tileSets;
	}
}