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
import org.geomajas.puregwt.client.spatial.MultiPolygon;
import org.geomajas.puregwt.client.spatial.Polygon;

/**
 * MultiPolygon client-side GWT object.
 * 
 * @author Pieter De Graef
 */
public class MultiPolygonImpl extends AbstractGeometry implements MultiPolygon {

	private static final long serialVersionUID = 100L;

	private Polygon[] polygons;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	MultiPolygonImpl(GeometryFactory factory) {
		super(factory);
	}

	MultiPolygonImpl(GeometryFactory factory, Polygon[] polygons) {
		super(factory);
		this.polygons = polygons;
	}

	// -------------------------------------------------------------------------
	// Geometry implementation:
	// -------------------------------------------------------------------------

	/**
	 * Create a copy of this geometry and return it.
	 */
	public Object clone() {
		return getGeometryFactory().createMultiPolygon(polygons);
	}

	public Geometry getGeometryN(int n) {
		if (isEmpty()) {
			return null;
		}
		if (n >= 0 && n < polygons.length) {
			return polygons[n];
		}
		return this;
	}

	/**
	 * Return the number of polygons.
	 */
	public int getNumGeometries() {
		if (isEmpty()) {
			return 0;
		}
		return polygons.length;
	}

	/**
	 * Return the sum of all the points of all the polygons in this MultiPolygon.
	 */
	public int getNumPoints() {
		if (isEmpty()) {
			return 0;
		}
		int total = 0;
		for (Polygon polygon : polygons) {
			total += polygon.getNumPoints();
		}
		return total;
	}

	/**
	 * Return the added number of polygon area's.
	 */
	public double getArea() {
		double area = 0;
		if (!isEmpty()) {
			for (Polygon polygon : polygons) {
				area += polygon.getArea();
			}
		}
		return area;
	}

	/**
	 * Return the total length.
	 */
	public double getLength() {
		double len = 0;
		if (!isEmpty()) {
			for (Polygon polygon : polygons) {
				len += polygon.getLength();
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
		Coordinate[] coordinates = new Coordinate[polygons.length];
		for (int i = 0; i < polygons.length; i++) {
			coordinates[i] = polygons[i].getCentroid();
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
			for (Polygon polygon : polygons) {
				double d = polygon.getDistance(coordinate);
				if (d < distance) {
					distance = d;
				}
			}
		}
		return distance;
	}

	/**
	 * Checks all it's polygons for validity.
	 */
	public boolean isValid() {
		if (!isEmpty()) {
			for (int i = 0; i < polygons.length; i++) {
				if (!polygons[i].isValid()) {
					return false;
				}
				for (int j = i + 1; j < polygons.length; j++) {
					if (polygons[i].intersects(polygons[j])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isEmpty() {
		return (polygons == null || polygons.length == 0);
	}

	/**
	 * Return the closest bounding box around the geometry.
	 */
	public Bbox getBounds() {
		Bbox bounds = null;
		if (!isEmpty()) {
			for (Polygon polygon : polygons) {
				if (bounds == null) {
					bounds = polygon.getBounds();
				} else {
					bounds = bounds.union(polygon.getBounds());
				}
			}
		}
		return bounds;
	}

	public Coordinate getCoordinate() {
		if (isEmpty()) {
			return null;
		}
		return polygons[0].getCoordinate();
	}

	/**
	 * Return the concatenated array of all coordinates of all polygons in this MultiPolygon geometry.
	 */
	public Coordinate[] getCoordinates() {
		if (isEmpty()) {
			return null;
		}
		Coordinate[] coordinates = new Coordinate[getNumPoints()];
		int count = 0;
		for (Polygon polygon : polygons) {
			Coordinate[] polyCoords = polygon.getCoordinates();
			for (Coordinate polyCoord : polyCoords) {
				coordinates[count++] = polyCoord;
			}
		}
		return coordinates;
	}

	public String toWkt() {
		if (isEmpty()) {
			return "MULTIPOLYGON(EMPTY)";
		}
		String wkt = "MULTIPOLYGON(";
		for (int i = 0; i < polygons.length; i++) {
			String lineWkt = polygons[i].toWkt();
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

	protected Polygon[] getPolygons() {
		return polygons;
	}

	protected void setPolygons(Polygon[] polygons) {
		this.polygons = polygons;
	}
}