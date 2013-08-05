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

import java.util.ArrayList;
import java.util.Collections;

import org.geomajas.geometry.Coordinate;

/**
 * The only class that is able to edit geometries. We needed this to copy the Javascript functionality. TODO:
 * Re-evaluate this mechanism. Perhaps we should never change existing geometries?
 * 
 * @author Pieter De Graef
 */
public class GeometryEditor {

	protected GeometryEditor() {
	}

	protected void setCoordinates(LineString lineString, Coordinate[] coordinates) {
		lineString.setCoordinates(coordinates);
	}

	protected boolean setCoordinateN(LineString lineString, Coordinate coordinate, int n) {
		if (!lineString.isEmpty() && n < lineString.getNumPoints()) {
			lineString.getCoordinates()[n] = coordinate;
			return true;
		}
		return false;
	}

	protected void setLineStrings(MultiLineString multiLineString, LineString[] lineStrings) {
		multiLineString.setLineStrings(lineStrings);
	}

	protected boolean setLineStringN(MultiLineString multiLineString, LineString lineString, int n) {
		if (!multiLineString.isEmpty() && n < multiLineString.getNumGeometries()) {
			multiLineString.getLineStrings()[n] = lineString;
			return true;
		}
		return false;
	}

	protected void setPolygons(MultiPolygon multiPolygon, Polygon[] polygons) {
		multiPolygon.setPolygons(polygons);
	}

	protected boolean setPolygonN(MultiPolygon multiPolygon, Polygon polygon, int n) {
		if (!multiPolygon.isEmpty() && n < multiPolygon.getNumGeometries()) {
			multiPolygon.getPolygons()[n] = polygon;
			return true;
		}
		return false;
	}

	protected void setPoints(MultiPoint multiPoint, Point[] points) {
		multiPoint.setPoints(points);
	}

	protected boolean setPointN(MultiPoint multiPoint, Point point, int n) {
		if (!multiPoint.isEmpty() && n < multiPoint.getNumGeometries()) {
			multiPoint.getPoints()[n] = point;
			return true;
		}
		return false;
	}

	protected void addPoint(MultiPoint multiPoint, Point point) {
		if (point != null) {
			Point[] points = multiPoint.getPoints();
			ArrayList<Point> list = new ArrayList<Point>(points.length + 1);
			Collections.addAll(list, points);
			list.add(point);
			multiPoint.setPoints(list.toArray(new Point[list.size()]));
		}
	}

	protected void setExteriorRing(Polygon polygon, LinearRing exteriorRing) {
		polygon.setExteriorRing(exteriorRing);
	}

	protected void setInteriorRings(Polygon polygon, LinearRing[] interiorRings) {
		polygon.setInteriorRings(interiorRings);
	}

	protected boolean setInteriorRingN(Polygon polygon, LinearRing interiorRing, int n) {
		if (!polygon.isEmpty() && n < polygon.getNumInteriorRing()) {
			polygon.getInteriorRings()[n] = interiorRing;
			return true;
		}
		return false;
	}

	protected void setCoordinate(Point point, Coordinate coordinate) {
		point.setCoordinate(coordinate);
	}
}
