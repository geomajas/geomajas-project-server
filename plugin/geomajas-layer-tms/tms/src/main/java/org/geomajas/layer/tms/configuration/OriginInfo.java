/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms.configuration;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class OriginInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private double x;

	private double y;

	public OriginInfo() {
	}

	@XmlAttribute(name = "x")
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@XmlAttribute(name = "y")
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}