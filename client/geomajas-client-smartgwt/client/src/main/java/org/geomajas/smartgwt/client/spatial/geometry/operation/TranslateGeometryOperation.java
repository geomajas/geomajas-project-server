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

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPoint;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.smartgwt.client.spatial.geometry.Point;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;

/**
 * This operation translates an entire {@link org.geomajas.smartgwt.client.spatial.geometry.Geometry} to another
 * location.
 *
 * @author Pieter De Graef
 */
public class TranslateGeometryOperation implements GeometryOperation {

	/**
	 * Translation value along the X-axis.
	 */
	private double translateX;

	/**
	 * Translation value along the Y-axis.
	 */
	private double translateY;

	/**
	 * This constructor sets all the necessary parameter values.
	 *
	 * @param translateX
	 *            Translation value along the X-axis.
	 * @param translateY
	 *            Translation value along the Y-axis.
	 */
	public TranslateGeometryOperation(double translateX, double translateY) {
		this.translateX = translateX;
		this.translateY = translateY;
	}

	/**
	 * Execute the operation!
	 *
	 * @return Returns a new geometry. If the given geometry is null, then null is returned.
	 */
	public Geometry execute(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		if (geometry instanceof Point) {
			return translate((Point) geometry);
		} else if (geometry instanceof LinearRing) {
			return translate((LinearRing) geometry);
		} else if (geometry instanceof LineString) {
			return translate((LineString) geometry);
		} else if (geometry instanceof Polygon) {
			return translate((Polygon) geometry);
		} else if (geometry instanceof MultiPoint) {
			return translate((MultiPoint) geometry);
		} else if (geometry instanceof MultiLineString) {
			return translate((MultiLineString) geometry);
		} else if (geometry instanceof MultiPolygon) {
			return translate((MultiPolygon) geometry);
		}

		return null;
	}

	//-------------------------------------------------------------------------
	// Private functions translating every type of geometry:
	//-------------------------------------------------------------------------

	private Point translate(Point point) {
		Coordinate c = point.getCoordinate();
		return point.getGeometryFactory().createPoint(new Coordinate(c.getX() + translateX, c.getY() + translateY));
	}

	private LineString translate(LineString lineString) {
		Coordinate[] coordinates = new Coordinate[lineString.getNumPoints()];
		for (int n = 0; n < lineString.getNumPoints(); n++) {
			Coordinate original = lineString.getCoordinateN(n);
			coordinates[n] = new Coordinate(original.getX() + translateX, original.getY() + translateY);
		}
		return lineString.getGeometryFactory().createLineString(coordinates);
	}

	private LinearRing translate(LinearRing linearRing) {
		Coordinate[] coordinates = new Coordinate[linearRing.getNumPoints()];
		for (int n = 0; n < linearRing.getNumPoints(); n++) {
			Coordinate original = linearRing.getCoordinateN(n);
			coordinates[n] = new Coordinate(original.getX() + translateX, original.getY() + translateY);
		}
		return linearRing.getGeometryFactory().createLinearRing(coordinates);
	}

	private Polygon translate(Polygon polygon) {
		LinearRing exteriorRing = translate(polygon.getExteriorRing());
		LinearRing[] interiorRings = new LinearRing[polygon.getNumInteriorRing()];
		for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
			interiorRings[n] = translate(polygon.getInteriorRingN(n));
		}
		return polygon.getGeometryFactory().createPolygon(exteriorRing, interiorRings);
	}

	private MultiPoint translate(MultiPoint multiPoint) {
		Point[] points = new Point[multiPoint.getNumGeometries()];
		for (int n = 0; n < multiPoint.getNumGeometries(); n++) {
			points[n] = translate((Point) multiPoint.getGeometryN(n));
		}
		return multiPoint.getGeometryFactory().createMultiPoint(points);
	}

	private MultiLineString translate(MultiLineString multiLineString) {
		LineString[] lineStrings = new LineString[multiLineString.getNumGeometries()];
		for (int n = 0; n < multiLineString.getNumGeometries(); n++) {
			lineStrings[n] = translate((LineString) multiLineString.getGeometryN(n));
		}
		return multiLineString.getGeometryFactory().createMultiLineString(lineStrings);
	}

	private MultiPolygon translate(MultiPolygon multiPolygon) {
		Polygon[] polygons = new Polygon[multiPolygon.getNumGeometries()];
		for (int n = 0; n < multiPolygon.getNumGeometries(); n++) {
			polygons[n] = translate((Polygon) multiPolygon.getGeometryN(n));
		}
		return multiPolygon.getGeometryFactory().createMultiPolygon(polygons);
	}
}
