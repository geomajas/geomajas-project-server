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

package org.geomajas.smartgwt.client.spatial.geometry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.spatial.Mathlib;
import org.geomajas.layer.LayerType;

/**
 * LineString client-side GWT object.
 *
 * @author Pieter De Graef
 */
public class LineString extends AbstractGeometry {

	private static final long serialVersionUID = 1311848562135714815L;

	private Coordinate[] coordinates;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	LineString(int srid, int precision) {
		super(srid, precision);
	}

	LineString(int srid, int precision, Coordinate[] coordinates) {
		super(srid, precision);
		this.coordinates = coordinates;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Return a coordinate, or null.
	 *
	 * @param n
	 *            Index in the geometry. This can be an integer value or an array of values.
	 * @return A coordinate or null.
	 */
	public Coordinate getCoordinateN(int n) {
		if (isEmpty()) {
			return null;
		}
		if (n >= 0 && n < coordinates.length) {
			return coordinates[n];
		}
		return null;
	}

	/**
	 * Checks whether or not the ring is closed.
	 *
	 * @return Returns true if the first coordinate equals the last coordinate.
	 */
	public boolean isClosed() {
		return !isEmpty() && getCoordinateN(0).equals(getCoordinateN(getNumPoints() - 1));
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	/**
	 * Calculate whether or not this geometry intersects with another. (calculates simple line intersections)
	 *
	 * @param geometry
	 *            The other geometry to check for intersection.
	 * @return Returns true or false.
	 */
	public boolean intersects(Geometry geometry) {
		if (geometry == null || isEmpty() || geometry.isEmpty()) {
			return false;
		}

		if (geometry.getNumGeometries() > 1) {
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				if (intersects(geometry.getGeometryN(n))) {
					return true;
				}
			}
		} else {
			final Coordinate[] coords = geometry.getCoordinates();
			if (coords.length > 1 && coordinates.length > 1) {
				for (int i = 0; i < coordinates.length - 1; i++) {
					for (int j = 0; j < coords.length - 1; j++) {
						if (Mathlib.lineIntersects(coordinates[i], coordinates[i + 1], coords[j], coords[j + 1])) {
							return true;
						}
					}
					if (Mathlib.touches(geometry, coordinates[i])) {
						return true;
					}
				}
			} else {
				// TODO implement me
			}
		}

		return false;
	}

	public Coordinate[] getCoordinates() {
		return coordinates;
	}

	void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		return getGeometryFactory().createLineString(coordinates);
	}

	/**
	 * Return the geometry itself.
	 *
	 * @param n
	 *            integer value...doesn't matter.
	 *
	 * @return this
	 */
	public Geometry getGeometryN(int n) {
		return this;
	}

	public int getNumPoints() {
		if (isEmpty()) {
			return 0;
		}
		return coordinates.length;
	}

	/**
	 * Return the length of the LineString.
	 */
	public double getLength() {
		double len = 0;
		if (!isEmpty()) {
			for (int i = 0; i < coordinates.length - 1; i++) {
				double deltaX = coordinates[i + 1].getX() - coordinates[i].getX();
				double deltaY = coordinates[i + 1].getY() - coordinates[i].getY();
				len += Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			}
		}
		return len;
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
		double sumX = 0;
		double sumY = 0;
		double totalLength = 0;
		for (int i = 0; i < coordinates.length - 1; i++) {
			double length = coordinates[i].distance(coordinates[i + 1]);
			totalLength += length;
			double midx = (coordinates[i].getX() + coordinates[i + 1].getX()) / 2;
			sumX += length * midx;
			double midy = (coordinates[i].getY() + coordinates[i + 1].getY()) / 2;
			sumY += length * midy;
		}
		return new Coordinate(sumX / totalLength, sumY / totalLength);
	}

	/**
	 * Return the minimal distance between this coordinate and any line segment of the geometry.
	 *
	 * @return Return the minimal distance
	 */

	public double getDistance(Coordinate coordinate) {
		double minDistance = Double.MAX_VALUE;

		if (!isEmpty()) {
			for (int i = 0; i < this.coordinates.length - 1; i++) {
				double dist = Mathlib.distance(this.coordinates[i], this.coordinates[i + 1], coordinate);
				if (dist < minDistance) {
					minDistance = dist;
				}
			}
		}
		return minDistance;
	}

	/**
	 * A linestring is valid if isEmpty() == true or coordinates.length != 1.
	 */
	public boolean isValid() {
		return isEmpty() || (coordinates.length != 1);
	}

	public boolean isEmpty() {
		return coordinates == null || coordinates.length == 0;
	}

	/**
	 * The area of a LineString is always 0.
	 */
	public double getArea() {
		return 0;
	}

	public Bbox getBounds() {
		if (isEmpty()) {
			return null;
		}
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;

		for (Coordinate coordinate : coordinates) {
			if (coordinate.getX() < minX) {
				minX = coordinate.getX();
			}
			if (coordinate.getY() < minY) {
				minY = coordinate.getY();
			}
			if (coordinate.getX() > maxX) {
				maxX = coordinate.getX();
			}
			if (coordinate.getY() > maxY) {
				maxY = coordinate.getY();
			}
		}

		return new Bbox(minX, minY, maxX - minX, maxY - minY);
	}

	public Coordinate getCoordinate() {
		if (isEmpty()) {
			return null;
		}
		return coordinates[0];
	}

	public LayerType getLayerType() {
		return LayerType.LINESTRING;
	}

	public String toWkt() {
		if (isEmpty()) {
			return "LINESTRING EMPTY";
		}
		String wkt = "LINESTRING (";
		for (int i = 0; i < coordinates.length; i++) {
			if (i > 0) {
				wkt += ", ";
			}
			wkt += coordinates[i].getX() + " " + coordinates[i].getY();
		}
		return wkt + ")";
	}

}
