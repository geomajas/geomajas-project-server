/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root element of TMS service definition.
 * 
 * @author Jan De Moerloose
 *
 */
@XmlRootElement(name = "TileMapService")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileMapService {

	@XmlElement(name = "Title")
	private String title;

	@XmlElement(name = "Abstract")
	private String abstractt;

	@XmlElementWrapper(name = "TileMaps")
	@XmlElement(name = "TileMap")
	private List<TileMapRef> tileMaps = new ArrayList<TileMapRef>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstract() {
		return abstractt;
	}

	public void setAbstract(String abstractt) {
		this.abstractt = abstractt;
	}

	public List<TileMapRef> getTileMaps() {
		return tileMaps;
	}

	public void setTileMaps(List<TileMapRef> tileMaps) {
		this.tileMaps = tileMaps;
	}

}
