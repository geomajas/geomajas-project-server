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

package org.geomajas.layer.tms.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Represents the TileFormat tag in the TMS description/capabilities XML file.
 * 
 * @author Pieter De Graef
 */
public class TileFormat implements Serializable {

	private static final long serialVersionUID = 100L;

	private int width;

	private int height;

	private String mimeType;

	private String extension;

	@XmlAttribute(name = "width")
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@XmlAttribute(name = "height")
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@XmlAttribute(name = "mime-type")
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@XmlAttribute(name = "extension")
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}