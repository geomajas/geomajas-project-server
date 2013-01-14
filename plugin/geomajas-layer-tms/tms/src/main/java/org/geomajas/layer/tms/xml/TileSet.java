/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Represents the TileSet tag in the TMS description/capabilities XML file.
 * 
 * @author Pieter De Graef
 */
public class TileSet implements Serializable {

	private static final long serialVersionUID = 100L;

	private String href;

	private double unitsPerPixel;

	private int order;

	public TileSet() {
	}

	@XmlAttribute(name = "href")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@XmlAttribute(name = "units-per-pixel")
	public double getUnitsPerPixel() {
		return unitsPerPixel;
	}

	public void setUnitsPerPixel(double unitsPerPixel) {
		this.unitsPerPixel = unitsPerPixel;
	}

	@XmlAttribute(name = "order")
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
