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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.Geometry;
import org.geomajas.puregwt.client.spatial.LinearRing;
import org.geomajas.puregwt.client.spatial.Polygon;

/**
 * Ploygon client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public class PolygonImpl extends AbstractGeometry implements Polygon {

	private static final long serialVersionUID = 100L;

	private LinearRing exteriorRing;

	private LinearRing[] interiorRings;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	PolygonImpl(GeometryFactory factory) {
		super(factory);
	}

	PolygonImpl(GeometryFactory factory, LinearRing exteriorRing, LinearRing[] interiorRings) {
		super(factory);
		this.exteriorRing = exteriorRing;
		this.interiorRings = interiorRings;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	LinearRing[] getInteriorRings() {
		return interiorRings;
	}

	void setInteriorRings(LinearRing[] interiorRings) {
		this.interiorRings = interiorRings;
	}

	void setExteriorRing(LinearRing exteriorRing) {
		this.exteriorRing = exteriorRing;
	}

	/**
	 * Get the outermost LinearRing of the polygon.
	 */
	public LinearRing getExteriorRing() {
		return exteriorRing;
	}

	/**
	 * Get one of the interior LinearRing geometries. i.e. one of the holes.
	 * 
	 * @param n
	 *            Index in the geometry's interior rings.
	 * @return Returns the hole if it exists, null otherwise.
	 */
	public LinearRing getInteriorRingN(int n) {
		if (!isEmpty()) {
			if (interiorRings != null && interiorRings.length > n) {
				return interiorRings[n];
			}
		}
		return null;
	}

	/**
	 * Get the total number of interior rings (holes) in the polygon.
	 * 
	 * @return
	 */
	public int getNumInteriorRing() {
		if (interiorRings != null) {
			return interiorRings.length;
		}
		return 0;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	/**
	 * Create a copy of this geometry and return it.
	 */
	public Object clone() {
		return getGeometryFactory().createPolygon(exteriorRing, interiorRings);
	}

	/**
	 * Return the minimal distance between this coordinate and any vertex of the geometry.
	 * 
	 * @return Return the minimal distance
	 */
	public double getDistance(Coordinate coordinate) {
		double distance = Double.MAX_VALUE;
		if (!isEmpty()) {
			for (LinearRing interiorRing : interiorRings) {
				double d = interiorRing.getDistance(coordinate);
				if (d < distance) {
					distance = d;
				}
			}
			double d = exteriorRing.getDistance(coordinate);
			if (d < distance) {
				distance = d;
			}
		}
		return distance;
	}

	/**
	 * Return the sum of all points in the exterior ring + the total number of points in all the interior rings.
	 */
	public int getNumPoints() {
		if (isEmpty()) {
			return 0;
		}
		int total = exteriorRing.getNumPoints();
		if (interiorRings != null) {
			for (LinearRing interiorRing : interiorRings) {
				total += interiorRing.getNumPoints();
			}
		}
		return total;
	}

	/**
	 * Return the shell area minus holes areas.
	 */
	public double getArea() {
		double area = 0;
		if (!isEmpty()) {
			area = exteriorRing.getArea();
			if (interiorRings != null) {
				for (LinearRing interiorRing : interiorRings) {
					area -= interiorRing.getArea();
				}
			}
		}
		return area;
	}

	/**
	 * Return the total length of all rings.
	 */
	public double getLength() {
		double length = 0;
		if (!isEmpty()) {
			length = exteriorRing.getLength();
			if (interiorRings != null) {
				for (LinearRing interiorRing : interiorRings) {
					length += interiorRing.getLength();
				}
			}
		}
		return length;
	}

	/**
	 * The centroid is also known as the "center of gravity" or the "center of mass".
	 * 
	 * @return Return the center point.
	 */
	public Coordinate getCentroid() {
		if (isEmpty()) {
			return null;
		}
		return exteriorRing.getCentroid();
	}

	/**
	 * Is the shell empty or not?
	 */
	public boolean isEmpty() {
		return exteriorRing == null;
	}

	/**
	 * Check the exterior ring and all interior rings for validity. Also checks to see if there are no intersections
	 * between any of the rings (both exterior and interior).
	 */
	public boolean isValid() {
		if (isEmpty()) {
			return true;
		}
		if (!exteriorRing.isValid()) {
			return false;
		}
		if (interiorRings != null) {
			LinearRing ring1;
			LinearRing ring2;
			for (int i = 0; i <= interiorRings.length; i++) {
				if (i == interiorRings.length) {
					ring1 = exteriorRing;
				} else {
					ring1 = interiorRings[i];
					if (!ring1.isValid()) {
						return false;
					}
				}
				for (int j = 0; j <= interiorRings.length; j++) {
					if (i == j) {
						continue;
					}
					if (j == interiorRings.length) {
						ring2 = exteriorRing;
					} else {
						ring2 = interiorRings[j];
					}
					if (ring1.intersects(ring2)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Return the bounds of the exterior ring, or null if the polygon is empty.
	 */
	public Bbox getBounds() {
		if (isEmpty()) {
			return null;
		}
		return exteriorRing.getBounds();
	}

	/**
	 * Return the coordinate of the exterior ring, or null if the polygon is empty.
	 */
	public Coordinate getCoordinate() {
		if (isEmpty()) {
			return null;
		}
		return exteriorRing.getCoordinate();
	}

	/**
	 * Return the concatenated coordinates of both the exterior ring and all the interior rings. If the polygon is
	 * empty, null will be returned.
	 */
	public Coordinate[] getCoordinates() {
		if (isEmpty()) {
			return null;
		}
		int len = getNumPoints();
		Coordinate[] coordinates = new Coordinate[len];
		int count;
		for (count = 0; count < exteriorRing.getNumPoints(); count++) {
			coordinates[count] = exteriorRing.getCoordinateN(count);
		}
		if (interiorRings != null) {
			for (LinearRing interiorRing : interiorRings) {
				for (int n = 0; n < interiorRing.getNumPoints(); n++) {
					coordinates[count++] = interiorRing.getCoordinateN(n);
				}
			}
		}
		return coordinates;
	}

	/**
	 * Does this polygon intersects with the given geometry?
	 * 
	 * @param geometry
	 *            The other geometry.
	 * @return Returns true of the polygon intersects the other geometry.
	 */
	public boolean intersects(Geometry geometry) {
		if (isEmpty()) {
			return false;
		}
		if (exteriorRing.intersects(geometry)) {
			return true;
		}
		if (interiorRings != null) {
			for (LinearRing interiorRing : interiorRings) {
				if (interiorRing.intersects(geometry)) {
					return true;
				}
			}
		}
		return false;
	}

	public String toWkt() {
		if (isEmpty()) {
			return "POLYGON(EMPTY)";
		}
		String wkt = "POLYGON(";
		String ringWkt = exteriorRing.toWkt();
		wkt += ringWkt.substring(ringWkt.indexOf("("));

		for (LinearRing interiorRing : interiorRings) {
			ringWkt = interiorRing.toWkt();
			wkt += "," + ringWkt.substring(ringWkt.indexOf("("));
		}
		return wkt + ")";
	}
}