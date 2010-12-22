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
import org.geomajas.puregwt.client.spatial.MultiPoint;
import org.geomajas.puregwt.client.spatial.Point;

/**
 * MultiPoint client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public class MultiPointImpl extends AbstractGeometry implements MultiPoint {

	private static final long serialVersionUID = 100L;

	private Point[] points;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	MultiPointImpl(GeometryFactory factory, SpatialService service) {
		this(factory, service, null);
	}

	MultiPointImpl(GeometryFactory factory, SpatialService service, Point[] points) {
		super(factory, service);
		this.points = points;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	public boolean isEmpty() {
		return (points == null || points.length == 0);
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

	/**
	 * Create a copy of this geometry and return it.
	 */
	public Object clone() {
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

		return new BboxImpl(minX, minY, maxX - minX, maxY - minY);
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
			return "MULTIPOINT(EMPTY)";
		}
		String wkt = "MULTIPOINT(";
		for (int i = 0; i < points.length; i++) {
			String lineWkt = points[i].toWkt();
			if (i > 0) {
				wkt += ",";
			}
			wkt += lineWkt.substring(lineWkt.indexOf("("));
		}
		return wkt + ")";
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected Point[] getPoints() {
		return points;
	}

	protected void setPoints(Point[] points) {
		this.points = points;
	}
}