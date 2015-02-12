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
 * Root element of TMS tile set.
 * 
 * @author Jan De Moerloose
 *
 */
@XmlRootElement(name = "TileSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileSet {

	@XmlAttribute
	private String href;

	@XmlAttribute(name = "units-per-pixel")
	private double unitsPerPixel;

	@XmlAttribute
	private int order;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public double getUnitsPerPixel() {
		return unitsPerPixel;
	}

	public void setUnitsPerPixel(double unitsPerPixel) {
		this.unitsPerPixel = unitsPerPixel;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
