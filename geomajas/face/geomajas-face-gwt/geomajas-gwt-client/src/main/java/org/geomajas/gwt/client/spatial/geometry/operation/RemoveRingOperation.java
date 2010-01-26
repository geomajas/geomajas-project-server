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
 * <p>
 * <code>GeometryOperation</code> that removes an interior ring from a {@link Polygon} object.
 * </p>
 *
 * @author Pieter De Graef
 */
public class RemoveRingOperation implements GeometryOperation {

	/**
	 * The index of the ring to remove. Note that only interior rings can be removed! The exterior ring is never
	 * touched. If this index is smaller then 0, then the first interior ring is removed. If this index is too large,
	 * then the last interior ring is removed.
	 */
	private int ringIndex;

	/**
	 * Removes an interior ring from a {@link Polygon} object.
	 *
	 * @param ringIndex
	 *            The index of the ring to remove. Note that only interior rings can be removed! The exterior ring is
	 *            never touched. If this index is smaller then 0, then the first interior ring is removed. If this index
	 *            is too large, then the last interior ring is removed.
	 */
	public RemoveRingOperation(int ringIndex) {
		this.ringIndex = ringIndex;
	}

	/**
	 * Execute the operation! When the geometry is not a Polygon, null is returned. When the polygon does not have any
	 * interior rings, null is returned.
	 */
	public Geometry execute(Geometry geometry) {
		if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;

			// No rings? return null:
			if (polygon.getNumInteriorRing() == 0) {
				return null;
			}

			// Correct the index if necessary:
			if (ringIndex < 0) {
				ringIndex = 0;
			} else if (ringIndex >= polygon.getNumInteriorRing()) {
				ringIndex = polygon.getNumInteriorRing() - 1;
			}

			// Create the new array of interior rings:
			LinearRing[] interiorRings = new LinearRing[polygon.getNumInteriorRing() - 1];
			int count = 0;
			for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
				if (n != ringIndex) {
					interiorRings[count++] = polygon.getInteriorRingN(n);
				}
			}
			return geometry.getGeometryFactory().createPolygon(polygon.getExteriorRing(), interiorRings);
		}
		return null;
	}

}
