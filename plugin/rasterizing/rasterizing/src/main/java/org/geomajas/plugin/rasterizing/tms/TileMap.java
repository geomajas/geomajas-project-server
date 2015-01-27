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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Root element of TMS layer definition.
 * 
 * @author Jan De Moerloose
 *
 */
@XmlRootElement(name = "TileMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileMap {

	@XmlAttribute
	private String version;

	@XmlAttribute(name = "tilemapservice")
	private String tileMapService;

	@XmlElement(name = "Title")
	private String title;

	@XmlElement(name = "Abstract")
	private String abstractt;

	@XmlElement(name = "SRS")
	private String srs;

	@XmlElement(name = "BoundingBox")
	private BoundingBox boundingBox;

	@XmlElement(name = "Origin")
	private Origin origin;

	@XmlElement(name = "TileFormat")
	private TileFormat tileFormat;

	@XmlElement(name = "TileSets")
	private TileSets tileSets;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTileMapService() {
		return tileMapService;
	}

	public void setTileMapService(String tileMapService) {
		this.tileMapService = tileMapService;
	}

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

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public void setBoundingBox(Envelope bbox) {
		this.boundingBox = new BoundingBox();
		boundingBox.setMinX(bbox.getMinX());
		boundingBox.setMinY(bbox.getMinY());
		boundingBox.setMaxX(bbox.getMaxX());
		boundingBox.setMaxy(bbox.getMaxY());
	}

	public void setOrigin(Coordinate c) {
		this.origin = new Origin();
		origin.setX(c.x);
		origin.setY(c.y);
	}

	public TileFormat getTileFormat() {
		return tileFormat;
	}

	public void setTileFormat(TileFormat tileFormat) {
		this.tileFormat = tileFormat;
	}

	public TileSets getTileSets() {
		return tileSets;
	}

	public void setTileSets(TileSets tileSets) {
		this.tileSets = tileSets;
	}

	public void setProfile(TmsProfile p, String href) {
		tileSets = new TileSets();
		tileSets.setProfile(p.getProfile().getName());
		int i = 0;
		for (double resolution : p.getResolutions()) {
			TileSet set = new TileSet();
			set.setHref(href + "/" + i);
			set.setOrder(i++);
			set.setUnitsPerPixel(resolution);
			tileSets.getTileSets().add(set);
		}
	}

}
