/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.gfx;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
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
		if (geometry != null) {
			if (Geometry.POINT.equals(geometry.getGeometryType())) {
				return toPathPoint(geometry);
			} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
				return toPathLineString(geometry);
			} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
				return toPathLinearRing(geometry);
			} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
				return toPathPolygon(geometry);
			} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
				return toPathMultiPoint(geometry);
			} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
				return toPathMultiLineString(geometry);
			} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
				return toPathMultiPolygon(geometry);
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Path toPathPoint(Geometry point) {
		if (point.getCoordinates() != null && point.getCoordinates().length == 1) {
			Coordinate first = point.getCoordinates()[0];
			return new Path((int) first.getX(), (int) first.getY());
		}
		return null;
	}

	private Path toPathLineString(Geometry lineString) {
		if (lineString.getCoordinates() != null && lineString.getCoordinates().length > 0) {
			Coordinate first = lineString.getCoordinates()[0];
			Path path = new Path((int) first.getX(), (int) first.getY());
			for (int i = 1; i < lineString.getCoordinates().length; i++) {
				Coordinate coordinate = lineString.getCoordinates()[i];
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
			return path;
		}
		return null;
	}

	private Path toPathLinearRing(Geometry linearRing) {
		if (linearRing.getCoordinates() != null && linearRing.getCoordinates().length > 0) {
			Coordinate first = linearRing.getCoordinates()[0];
			Path path = new Path((int) first.getX(), (int) first.getY());
			for (int i = 1; i < linearRing.getCoordinates().length - 1; i++) {
				Coordinate coordinate = linearRing.getCoordinates()[i];
				path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
			}
			path.close();
			path.getElement().getStyle().setProperty("fillRule", "evenOdd");
			return path;
		}
		return null;
	}

	private Path toPathPolygon(Geometry polygon) {
		if (polygon.getGeometries() != null && polygon.getGeometries().length > 0) {
			Path path = toPathLinearRing(polygon.getGeometries()[0]);
			path.getElement().getStyle().setProperty("fillRule", "evenOdd");
			for (int i = 1; i < polygon.getGeometries().length; i++) {
				Geometry ring = polygon.getGeometries()[i];
				path.moveTo((int) ring.getCoordinates()[0].getX(), (int) ring.getCoordinates()[0].getY());
				for (int j = 1; j < ring.getCoordinates().length - 1; j++) {
					Coordinate coordinate = ring.getCoordinates()[j];
					path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
				}
				path.close();
			}
			return path;
		}
		return null;
	}

	private Path toPathMultiPoint(Geometry multiPoint) {
		if (multiPoint.getGeometries() != null && multiPoint.getGeometries().length > 0) {
			Path path = toPathPoint(multiPoint.getGeometries()[0]);
			for (int i = 1; i < multiPoint.getGeometries().length; i++) {
				Geometry point = multiPoint.getGeometries()[i];
				path.moveTo(point.getCoordinates()[0].getX(), point.getCoordinates()[0].getY());
			}
			return path;
		}
		return null;
	}

	private Path toPathMultiLineString(Geometry multiLineString) {
		if (multiLineString.getGeometries() != null && multiLineString.getGeometries().length > 0) {
			Path path = toPathLineString(multiLineString.getGeometries()[0]);
			for (int i = 1; i < multiLineString.getGeometries().length; i++) {
				Geometry lineString = multiLineString.getGeometries()[i];
				path.moveTo((int) lineString.getCoordinates()[0].getX(), (int) lineString.getCoordinates()[0].getY());
				for (int j = 1; j < lineString.getCoordinates().length; j++) {
					Coordinate coordinate = lineString.getCoordinates()[j];
					path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
				}
			}
			return path;
		}
		return null;
	}

	private Path toPathMultiPolygon(Geometry multiPolygon) {
		if (multiPolygon.getGeometries() != null && multiPolygon.getGeometries().length > 0) {
			Path path = toPathPolygon(multiPolygon.getGeometries()[0]);
			for (int i = 1; i < multiPolygon.getGeometries().length; i++) {
				Geometry polygon = multiPolygon.getGeometries()[i];
				for (int j = 0; j < polygon.getGeometries().length; j++) {
					Geometry ring = polygon.getGeometries()[0];
					path.moveTo((int) ring.getCoordinates()[0].getX(), (int) ring.getCoordinates()[0].getY());
					for (int k = 1; k < ring.getCoordinates().length; k++) {
						Coordinate coordinate = ring.getCoordinates()[k];
						path.lineTo((int) coordinate.getX(), (int) coordinate.getY());
					}
					path.close();
				}
			}
			return path;
		}
		return null;
	}
}