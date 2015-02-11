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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tile map reference.
 * 
 * @author Jan De Moerloose
 *
 */
@XmlRootElement(name = "TileMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileMapRef {

	@XmlAttribute
	private String title;

	@XmlAttribute
	private String srs;

	@XmlAttribute
	private String profile;

	@XmlAttribute
	private String href;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
