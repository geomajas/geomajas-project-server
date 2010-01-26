/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.spatial.geometry.operation;

import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * <code>GeometryOperation</code> that inserts a new {@link LinearRing} in the array of interior rings of a
 * {@link Polygon} object.
 *
 * @author Pieter De Graef
 */
public class InsertRingOperation implements GeometryOperation {

	/**
	 * The actual LinearRing to add.
	 */
	private LinearRing ring;

	/**
	 * The integer index where to insert the ring into the array of interior rings.
	 */
	private int index;

	/**
	 * Add a new LinearRing to a {@link Polygon} object.
	 *
	 * @param ring
	 *            The actual LinearRing to add.
	 */
	public InsertRingOperation(LinearRing ring, int index) {
		this.ring = ring;
		this.index = index;
	}

	/**
	 * Execute the operation! When the geometry is not a Polygon, null is returned.
	 */
	public Geometry execute(Geometry geometry) {
		if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			if (geometry.isEmpty()) {
				return null;
			} else {
				LinearRing[] interiorRings = new LinearRing[polygon.getNumInteriorRing() + 1];
				int count = 0;
				for (int n = 0; n < interiorRings.length; n++) {
					if (n == index) {
						interiorRings[n] = ring;
					} else {
						interiorRings[n] = polygon.getInteriorRingN(count);
					}
				}
				return geometry.getGeometryFactory().createPolygon(polygon.getExteriorRing(), interiorRings);
			}
		}
		return null;
	}

}
