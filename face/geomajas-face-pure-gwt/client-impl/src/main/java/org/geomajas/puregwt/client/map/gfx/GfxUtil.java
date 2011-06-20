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

package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.spatial.Geometry;
import org.geomajas.puregwt.client.spatial.LineString;
import org.geomajas.puregwt.client.spatial.LinearRing;
import org.geomajas.puregwt.client.spatial.MultiLineString;
import org.geomajas.puregwt.client.spatial.MultiPoint;
import org.geomajas.puregwt.client.spatial.MultiPolygon;
import org.geomajas.puregwt.client.spatial.Point;
import org.geomajas.puregwt.client.spatial.Polygon;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.shape.Path;

import com.google.inject.Inject;

/**
 * Utility class concerning custom graphics rendering on the map.
 * 
 * @author Pieter De Graef
 */
public final class GfxUtil {

	@Inject
	private GfxUtil() {
	}

	public void applyStyle(Shape shape, FeatureStyleInfo style) {
		if (style.getFillColor() != null) {
			shape.setFillColor(style.getFillColor());
		}
		if (style.getFillOpacity() >= 0) {
			shape.setFillOpacity(style.getFillOpacity());
		}
		if (style.getStrokeColor() != null) {
			shape.setStrokeColor(style.getStrokeColor());
		}
		if (style.getStrokeOpacity() >= 0) {
			shape.setStrokeOpacity(style.getStrokeOpacity());
		}
		if (style.getStrokeWidth() >= 0) {
			shape.setStrokeWidth(style.getStrokeWidth());
		}
	}

	public Path toPath(Geometry geometry) {
		if (geometry instanceof Point) {
			Point point = (Point) geometry;
			return new Path((int) point.getX(), (int) point.getY());
		} else if (geometry instanceof LineString) {
			return toPath((LineString) geometry);
		} else if (geometry instanceof LinearRing) {
			return toPath((LinearRing) geometry);
		} else if (geometry instanceof Polygon) {
			return toPath((Polygon) geometry);
		} else if (geometry instanceof MultiPoint) {
			return toPath((MultiPoint) geometry);
		} else if (geometry instanceof MultiLineString) {
			return toPath((MultiLineString) geometry);
		} else if (geometry instanceof MultiPolygon) {
			return toPath((MultiPolygon) geometry);
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Path toPath(LineString lineString) {
		Path path = new Path((int) lineString.getCoordinate().getX(), (int) lineString.getCoordinate().getY());
		for (int i = 1; i < lineString.getNumPoints(); i++) {
			Coordinate coordinate = lineString.getCoordinateN(i);
			path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
		}
		return path;
	}

	private Path toPath(LinearRing linearRing) {
		Path path = new Path((int) linearRing.getCoordinate().getX(), (int) linearRing.getCoordinate().getY());
		for (int i = 1; i < linearRing.getNumPoints() - 1; i++) {
			Coordinate coordinate = linearRing.getCoordinateN(i);
			path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
		}
		path.close();
		return path;
	}

	private Path toPath(Polygon polygon) {
		Path path = toPath(polygon.getExteriorRing());
		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			LinearRing interiorRing = polygon.getInteriorRingN(i);
			path.moveTo((int) interiorRing.getCoordinate().getX(), (int) interiorRing.getCoordinate().getY());
			for (int j = 1; j < interiorRing.getNumPoints() - 1; j++) {
				Coordinate coordinate = interiorRing.getCoordinateN(j);
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
			path.close();
		}
		return path;
	}

	private Path toPath(MultiPoint multiPoint) {
		Path path = new Path((int) multiPoint.getCoordinate().getX(), (int) multiPoint.getCoordinate().getY());
		for (int i = 1; i < multiPoint.getNumGeometries(); i++) {
			Point point = (Point) multiPoint.getGeometryN(i);
			path.moveTo((int) point.getX(), (int) point.getY());
		}
		return path;
	}

	private Path toPath(MultiLineString multiLineString) {
		Path path = toPath((LineString) multiLineString.getGeometryN(0));
		for (int i = 1; i < multiLineString.getNumGeometries(); i++) {
			LineString lineString = (LineString) multiLineString.getGeometryN(i);
			path.moveTo((int) lineString.getCoordinate().getX(), (int) lineString.getCoordinate().getY());
			for (int j = 1; j < lineString.getNumPoints(); j++) {
				Coordinate coordinate = lineString.getCoordinateN(j);
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
		}
		return path;
	}

	private Path toPath(MultiPolygon multiPolygon) {
		Path path = toPath((Polygon) multiPolygon.getGeometryN(0));
		for (int g = 1; g < multiPolygon.getNumGeometries(); g++) {
			Polygon polygon = (Polygon) multiPolygon.getGeometryN(g);

			LinearRing exteriorRing = polygon.getExteriorRing();
			path.moveTo((int) exteriorRing.getCoordinate().getX(), (int) exteriorRing.getCoordinate().getY());
			for (int i = 1; i < exteriorRing.getNumPoints() - 1; i++) {
				Coordinate coordinate = exteriorRing.getCoordinateN(i);
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
			path.close();

			for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
				LinearRing interiorRing = polygon.getInteriorRingN(i);
				path.moveTo((int) interiorRing.getCoordinate().getX(), (int) interiorRing.getCoordinate().getY());
				for (int j = 1; j < interiorRing.getNumPoints() - 1; j++) {
					Coordinate coordinate = interiorRing.getCoordinateN(j);
					path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
				}
				path.close();
			}
		}
		return path;
	}
}