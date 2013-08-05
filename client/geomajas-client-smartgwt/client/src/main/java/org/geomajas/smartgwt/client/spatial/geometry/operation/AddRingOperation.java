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
 * <code>GeometryOperation</code> that adds a new {@link LinearRing} to a {@link Polygon} object.
 *
 * @author Pieter De Graef
 */
public class AddRingOperation implements GeometryOperation {

	/**
	 * The actual LinearRing to add.
	 */
	private LinearRing ring;

	/**
	 * Add a new LinearRing to a {@link Polygon} object.
	 *
	 * @param ring
	 *            The actual LinearRing to add.
	 */
	public AddRingOperation(LinearRing ring) {
		this.ring = ring;
	}

	/**
	 * Execute the operation! When the geometry is not a Polygon, null is returned.
	 */
	public Geometry execute(Geometry geometry) {
		if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			if (geometry.isEmpty()) {
				return geometry.getGeometryFactory().createPolygon(ring, null);
			} else {
				LinearRing[] interiorRings = new LinearRing[polygon.getNumInteriorRing() + 1];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					interiorRings[n] = polygon.getInteriorRingN(n);
				}
				interiorRings[interiorRings.length - 1] = ring;
				return geometry.getGeometryFactory().createPolygon(polygon.getExteriorRing(), interiorRings);
			}
		}
		return null;
	}

}
