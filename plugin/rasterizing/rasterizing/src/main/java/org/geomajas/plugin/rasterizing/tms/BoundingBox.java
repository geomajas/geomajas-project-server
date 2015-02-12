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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bounding box of the TMS. Either from profile or by converting layer extent to map crs.
 * 
 * @author Jan De Moerloose
 *
 */
@XmlRootElement(name = "BoundingBox")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundingBox {

	@XmlAttribute(name = "minx")
	private double minX;

	@XmlAttribute(name = "miny")
	private double minY;

	@XmlAttribute(name = "maxx")
	private double maxX;

	@XmlAttribute(name = "maxy")
	private double maxy;

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxy() {
		return maxy;
	}

	public void setMaxy(double maxy) {
		this.maxy = maxy;
	}

}
