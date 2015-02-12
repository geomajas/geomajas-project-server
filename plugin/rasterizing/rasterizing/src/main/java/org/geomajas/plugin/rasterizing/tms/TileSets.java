/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.tms;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root element of TMS grid set.
 * 
 * @author Jan De Moerloose
 *
 */
@XmlRootElement(name = "TileSets")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileSets {

	@XmlAttribute
	private String profile;

	@XmlElement(name = "TileSet")
	private List<TileSet> tileSets = new ArrayList<TileSet>();

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public List<TileSet> getTileSets() {
		return tileSets;
	}

	public void setTileSets(List<TileSet> tileSets) {
		this.tileSets = tileSets;
	}

}
