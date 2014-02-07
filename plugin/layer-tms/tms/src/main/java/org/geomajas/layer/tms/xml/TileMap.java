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

package org.geomajas.layer.tms.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the TileMap tag in the TMS description/capabilities XML file.
 * 
 * @author Pieter De Graef
 */
@XmlRootElement(name = "TileMap")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TileMap implements Serializable {

	private static final long serialVersionUID = 100L;

	private String version;

	private String tileMapService;

	private String title;

	private String abstractTxt;

	private String srs;

	private BoundingBox boundingBox;

	private Origin origin;

	private TileFormat tileFormat;

	private TileSets tileSets;

	public TileMap() {
	}

	@XmlAttribute()
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlAttribute(name = "tilemapservice")
	public String getTileMapService() {
		return tileMapService;
	}

	public void setTileMapService(String tileMapService) {
		this.tileMapService = tileMapService;
	}

	@XmlElement(name = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "SRS")
	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	@XmlElement(name = "BoundingBox")
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	@XmlElement(name = "Origin")
	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	@XmlElement(name = "TileFormat")
	public TileFormat getTileFormat() {
		return tileFormat;
	}

	public void setTileFormat(TileFormat tileFormat) {
		this.tileFormat = tileFormat;
	}

	@XmlElement(name = "TileSets")
	public TileSets getTileSets() {
		return tileSets;
	}

	public void setTileSets(TileSets tileSets) {
		this.tileSets = tileSets;
	}

	@XmlElement(name = "Abstract")
	public String getAbstractTxt() {
		return abstractTxt;
	}

	public void setAbstractTxt(String abstractTxt) {
		this.abstractTxt = abstractTxt;
	}
}