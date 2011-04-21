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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;

/**
 * <p>
 * GWT client side implementation of a Point.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class PointImpl extends AbstractGeometry implements Point {

	private static final long serialVersionUID = 100L;

	private Coordinate coordinate;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	PointImpl(Coordinate c) {
		coordinate = c;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Coordinate[] getCoordinates() {
		return new Coordinate[] { coordinate };
	}

	/**
	 * Return the number of coordinates.
	 */
	public int getNumPoints() {
		return 1;
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
	 * @return Return the minimal distance. If this point is empty, Double.MAX_VALUE is returned.
	 */
	public double getDistance(Coordinate point) {
		if (isEmpty()) {
			return Double.MAX_VALUE;
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
		return new BboxImpl(coordinate.getX(), coordinate.getY(), 0, 0);
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
					double distance = service.distance(coordinates[i], coordinates[i + 1], coordinate);
					if (distance < GeometryFactory.PARAM_DEFAULT_DELTA) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public String toWkt() {
		if (isEmpty()) {
			return "POINT(EMPTY)";
		}
		return "POINT(" + coordinate.getX() + " " + coordinate.getY() + ")";
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public double getX() {
		if (isEmpty()) {
			return Double.MIN_VALUE;
		}
		return coordinate.getX();
	}

	public double getY() {
		if (isEmpty()) {
			return Double.MIN_VALUE;
		}
		return coordinate.getY();
	}
}
