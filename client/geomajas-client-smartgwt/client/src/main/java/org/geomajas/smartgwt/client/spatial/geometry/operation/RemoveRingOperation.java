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

package org.geomajas.smartgwt.client.spatial.geometry.operation;

import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;

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
