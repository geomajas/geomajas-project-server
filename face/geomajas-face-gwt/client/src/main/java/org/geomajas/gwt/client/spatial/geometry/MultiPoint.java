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

package org.geomajas.gwt.client.spatial.geometry;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.LineSegment;
import org.geomajas.layer.LayerType;

/**
 * MultiPoint client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public class MultiPoint extends AbstractGeometry {

	private static final long serialVersionUID = 2262892444046706151L;

	private Point[] points;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	MultiPoint(int srid, int precision) {
		super(srid, precision);
	}

	MultiPoint(int srid, int precision, Point[] points) {
		super(srid, precision);
		this.points = points;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	public boolean isEmpty() {
		return (points == null || points.length == 0);
	}

	Point[] getPoints() {
		return points;
	}

	void setPoints(Point[] points) {
		this.points = points;
	}

	/**
	 * Return true.
	 */
	public boolean isValid() {
		return true;
	}

	public Coordinate getCentroid() {
		if (isEmpty()) {
			return null;
		}
		double sumX = 0;
		double sumY = 0;
		double numPoints = getNumPoints();
		for (Point point : points) {
			sumX += point.getX();
			sumY += point.getY();
		}
		return new Coordinate(sumX / numPoints, sumY / numPoints);
	}

	/**
	 * Return 0.
	 */
	public double getLength() {
		return 0;
	}

	/**
	 * Return the total number of points in this MultiPoint geometry.
	 */
	public int getNumGeometries() {
		if (isEmpty()) {
			return 0;
		}
		return points.length;
	}

	/**
	 * Return the total number of points in this MultiPoint geometry. Since every Point object has only one coordinate,
	 * the result is the same as when requesting the getNumGeometries.
	 */
	public int getNumPoints() {
		if (isEmpty()) {
			return 0;
		}
		return points.length;
	}

	@Override
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		return getGeometryFactory().createMultiPoint(points);
	}

	/**
	 * Return null if the MultiPoint is empty, returns one of the points if the requested index exists, throws an
	 * exception otherwise.
	 * 
	 * @param n
	 *            The index in the point array to retrieve. Better make sure it is a valid index. (0 < n < getNumPoints)
	 */
	public Geometry getGeometryN(int n) {
		if (isEmpty()) {
			return null;
		}
		if (n >= 0 && n < points.length) {
			return points[n];
		}
		throw new ArrayIndexOutOfBoundsException(n);
		// return this;
	}

	public double getDistance(Coordinate coordinate) {
		double distance = Double.MAX_VALUE;
		if (!isEmpty()) {
			for (Point point : points) {
				double temp = new LineSegment(point.getCoordinate(), coordinate).getLength();
				if (temp < distance) {
					distance = temp;
				}
			}
		}
		return distance;
	}

	/**
	 * Return the bounding box spanning the full list of points.
	 */
	public Bbox getBounds() {
		if (isEmpty()) {
			return null;
		}
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;

		for (Point point : points) {
			if (point.getX() < minX) {
				minX = point.getX();
			}
			if (point.getY() < minY) {
				minY = point.getY();
			}
			if (point.getX() > maxX) {
				maxX = point.getX();
			}
			if (point.getY() > maxY) {
				maxY = point.getY();
			}
		}

		return new Bbox(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * Get the coordinate from the first point, or null if the MultiPoint is empty.
	 */
	public Coordinate getCoordinate() {
		if (isEmpty()) {
			return null;
		}
		return points[0].getCoordinate();
	}

	/**
	 * Get the full list of coordinates from all the points in this MultiPoint geometry. If this geometry is empty, null
	 * will be returned.
	 */
	public Coordinate[] getCoordinates() {
		if (isEmpty()) {
			return null;
		}
		Coordinate[] coordinates = new Coordinate[points.length];
		for (int i = 0; i < points.length; i++) {
			coordinates[i] = points[i].getCoordinate();
		}
		return coordinates;
	}

	public LayerType getLayerType() {
		return LayerType.MULTIPOINT;
	}
	
	/**
	 * Return true if any of the points intersect the given geometry.
	 */
	public boolean intersects(Geometry geometry) {
		if (isEmpty()) {
			return false;
		}
		for (Point point : points) {
			if (point.intersects(geometry)) {
				return true;
			}
		}
		return false;
	}

	public String toWkt() {
		if (isEmpty()) {
			return "MULTIPOINT EMPTY ";
		}
		String wkt = "MULTIPOINT (";
		for (int i = 0; i < points.length; i++) {
			String lineWkt = points[i].toWkt();
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}
}
