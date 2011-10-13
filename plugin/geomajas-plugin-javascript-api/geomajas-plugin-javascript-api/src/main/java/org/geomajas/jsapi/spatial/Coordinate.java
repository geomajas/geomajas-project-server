/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.jsapi.spatial;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Javascript exportable facade for a Coordinate.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("Coordinate")
@ExportPackage("org.geomajas.jsapi.spatial")
@Api(allMethods = true)
public class Coordinate implements ExportOverlay<org.geomajas.geometry.Coordinate> {

	@ExportConstructor
	public static org.geomajas.geometry.Coordinate constructor(double x, double y) {
		return new org.geomajas.geometry.Coordinate(x, y);
	}

	public String toString() {
		return "";
	}

	public boolean equals(Object other) {
		return false;
	}

	public int hashCode() {
		return 0;
	}

	public double distance(org.geomajas.geometry.Coordinate c) {
		return 0;
	}

	public double getX() {
		return 0;
	}

	public double getY() {
		return 0;
	}

	public void setX(double x) {
	}

	public void setY(double y) {
	}
}