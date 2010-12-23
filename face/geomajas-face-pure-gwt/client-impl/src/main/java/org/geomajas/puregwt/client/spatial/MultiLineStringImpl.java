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
import org.geomajas.puregwt.client.spatial.LineString;
import org.geomajas.puregwt.client.spatial.MultiLineString;
import org.geomajas.puregwt.client.util.SpatialService;

/**
 * MultiLineString client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public class MultiLineStringImpl extends AbstractGeometry implements MultiLineString {

	private static final long serialVersionUID = 100L;

	private LineString[] lineStrings;

	// -------------------------------------------------------------------------
	// Constructor (package visibility)
	// -------------------------------------------------------------------------

	MultiLineStringImpl(GeometryFactory factory) {
		this(factory, null, null);
	}

	MultiLineStringImpl(GeometryFactory factory, SpatialService service, LineString[] lineStrings) {
		super(factory, service);
		this.lineStrings = lineStrings;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	/**
	 * Create a copy of this geometry and return it.
	 */
	public Object clone() {
		return getGeometryFactory().createMultiLineString(lineStrings);
	}

	public Geometry getGeometryN(int n) {
		if (!isEmpty()) {
			if (n >= 0 && n < lineStrings.length) {
				return lineStrings[n];
			}
		}
		return this;
	}

	/**
	 * Return the number of LineStrings.
	 */
	public int getNumGeometries() {
		if (isEmpty()) {
			return 0;
		}
		return lineStrings.length;
	}

	/**
	 * Get the sum of points of all LineStrings in this MultiLineString.
	 */
	public int getNumPoints() {
		if (isEmpty()) {
			return 0;
		}
		int sum = 0;
		for (LineString lineString : lineStrings) {
			sum += lineString.getNumPoints();
		}
		return sum;
	}

	/**
	 * Return the added length of all the LineStrings.
	 */
	public double getLength() {
		double len = 0;
		if (!isEmpty()) {
			for (LineString lineString : lineStrings) {
				len += lineString.getLength();
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
		Coordinate[] coordinates = new Coordinate[lineStrings.length];
		for (int i = 0; i < lineStrings.length; i++) {
			coordinates[i] = lineStrings[i].getCentroid();
		}
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
	 * Return the minimal distance between this coordinate and any vertex of the geometry.
	 * 
	 * @return Return the minimal distance
	 */
	public double getDistance(Coordinate coordinate) {
		double distance = Double.MAX_VALUE;
		if (!isEmpty()) {
			for (LineString lineString : lineStrings) {
				double d = lineString.getDistance(coordinate);
				if (d < distance) {
					distance = d;
				}
			}
		}
		return distance;
	}

	public boolean isEmpty() {
		return (lineStrings == null || lineStrings.length == 0);
	}

	/**
	 * Check the validity of all the LineStrings.
	 */
	public boolean isValid() {
		if (isEmpty()) {
			return true;
		}
		for (LineString lineString : lineStrings) {
			if (!lineString.isValid()) {
				return false;
			}
		}
		return true;
	}

	public boolean isSimple() {
		if (isEmpty()) {
			return true;
		}
		for (int i = 0; i < lineStrings.length - 1; i++) {
			for (int j = i + 1; j < lineStrings.length; j++) {
				if (lineStrings[i].intersects(lineStrings[j])) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Return the closest bounding box around the geometry.
	 */
	public Bbox getBounds() {
		Bbox bounds = null;
		if (!isEmpty()) {
			for (LineString lineString : lineStrings) {
				if (bounds == null) {
					bounds = lineString.getBounds();
				} else {
					bounds = bounds.union(lineString.getBounds());
				}
			}
		}
		return bounds;
	}

	/**
	 * Return the first coordinate of the first LineString in the array, unless the geometry is empty, in which case
	 * null is returned.
	 */
	public Coordinate getCoordinate() {
		if (isEmpty()) {
			return null;
		}
		return lineStrings[0].getCoordinate();
	}

	/**
	 * Get the full concatenated list of coordinates of all the LineStrings in this MultiLineString geometry.
	 */
	public Coordinate[] getCoordinates() {
		if (isEmpty()) {
			return null;
		}
		Coordinate[] coordinates = new Coordinate[getNumPoints()];
		int count = 0;
		for (LineString lineString : lineStrings) {
			for (int j = 0; j < lineString.getNumPoints(); j++) {
				coordinates[count++] = lineString.getCoordinateN(j);
			}
		}
		return coordinates;
	}

	/**
	 * Check the LineStrings one by one for intersections with the given geometry.
	 */
	public boolean intersects(Geometry geometry) {
		if (isEmpty()) {
			return false;
		}
		for (LineString lineString : lineStrings) {
			if (lineString.intersects(geometry)) {
				return true;
			}
		}

		return false;
	}

	public String toWkt() {
		if (isEmpty()) {
			return "MULTILINESTRING(EMPTY)";
		}
		String wkt = "MULTILINESTRING(";
		for (int i = 0; i < lineStrings.length; i++) {
			String lineWkt = lineStrings[i].toWkt();
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

	protected LineString[] getLineStrings() {
		return lineStrings;
	}

	protected void setLineStrings(LineString[] lineStrings) {
		this.lineStrings = lineStrings;
	}
}