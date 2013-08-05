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
 * <p>
 * GWT client side implementation of a Point.
 * </p>
 *
 * @author Pieter De Graef
 */
public class Point extends AbstractGeometry {

	private static final long serialVersionUID = -7676010894505196439L;

	private Coordinate coordinate;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	Point(int srid, int precision) {
		super(srid, precision);
	}

	Point(int srid, int precision, double x, double y) {
		super(srid, precision);
		coordinate = new Coordinate(x, y);
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	@Override
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		return getGeometryFactory().createPoint(coordinate);
	}

	@Override
	public Coordinate[] getCoordinates() {
		if (coordinate == null) {
			return null;
		}
		return new Coordinate[] {coordinate};
	}

	/**
	 * Return the number of coordinates.
	 */
	public int getNumPoints() {
		return coordinate == null ? 0 : 1;
	}

	/**
	 * Return the only coordinate or this point itself.
	 *
	 * @param n
	 *            Integer or array of integers.
	 */
	public Geometry getGeometryN(int n) {
		return this;
	}

	@Override
	public int getNumGeometries() {
		return 1;
	}

	/**
	 * Return 0.
	 */
	public double getArea() {
		return 0;
	}

	/**
	 * Return 0.
	 */
	public double getLength() {
		return 0;
	}

	/**
	 * The centroid is also known as the "center of gravity" or the "center of mass". In the case of a point, it is the
	 * point's coordinate.
	 *
	 * @return Return the center point.
	 */
	public Coordinate getCentroid() {
		return coordinate;
	}

	/**
	 * Return the minimal distance between coordinate and this point.
	 *
	 * @return Return the minimal distance
	 */
	public double getDistance(Coordinate point) {
		if (isEmpty()) {
			return Double.MIN_VALUE;
		}
		return this.coordinate.distance(point);
	}

	/**
	 * Return true.
	 */
	public boolean isValid() {
		return true;
	}

	/**
	 * Return true.
	 */
	public boolean isSimple() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return coordinate == null;
	}

	/**
	 * Return a bounding box from the point's coordinate, with width and height equal to 0.
	 */
	public Bbox getBounds() {
		if (isEmpty()) {
			return null;
		}
		return new Bbox(coordinate.getX(), coordinate.getY(), 0, 0);
	}

	@Override
	public LayerType getLayerType() {
		return LayerType.POINT;
	}

	/**
	 * Calculate whether or not this geometry intersects with another.
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
			Coordinate[] coordinates = geometry.getCoordinates();
			if (coordinates.length == 1) {
				return coordinate.equals(coordinates[0]);
			} else {
				for (int i = 0; i < coordinates.length - 1; i++) {
					if (Mathlib.distance(coordinates[i], coordinates[i + 1], coordinate) < Mathlib.ZERO) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public String toWkt() {
		if (isEmpty()) {
			return "POINT EMPTY";
		}
		return "POINT (" + coordinate.getX() + " " + coordinate.getY() + ")";
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	@Override
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * Get x coordinate.
	 *
	 * @return x
	 */
	public double getX() {
		if (isEmpty()) {
			return Double.MIN_VALUE;
		}
		return coordinate.getX();
	}

	/**
	 * Get y coordinate.
	 *
	 * @return y
	 */
	public double getY() {
		if (isEmpty()) {
			return Double.MIN_VALUE;
		}
		return coordinate.getY();
	}
}
