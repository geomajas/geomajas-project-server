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

package org.geomajas.gwt.client.spatial.geometry;

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
