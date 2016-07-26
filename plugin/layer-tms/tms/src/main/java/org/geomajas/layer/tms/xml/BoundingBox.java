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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Represents the BoundingBox tag in the TMS description/capabilities XML file.
 * 
 * @author Pieter De Graef
 */
public class BoundingBox implements Serializable {

	private static final long serialVersionUID = 100L;

	private double minX;

	private double maxX;

	private double minY;

	private double maxY;

	@XmlAttribute(name = "minx")
	public double getMinX() {
		return minX;
	}

	public void setMinX(double minx) {
		this.minX = minx;
	}

	@XmlAttribute(name = "maxx")
	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxx) {
		this.maxX = maxx;
	}

	@XmlAttribute(name = "miny")
	public double getMinY() {
		return minY;
	}

	public void setMinY(double miny) {
		this.minY = miny;
	}

	@XmlAttribute(name = "maxy")
	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxy) {
		this.maxY = maxy;
	}
}