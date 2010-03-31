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

package org.geomajas.gwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.Camera;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPoint;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * <p>
 * This class is able to transform coordinates, geometries and bounding boxes from world space to view space, and the
 * other way around. World space means that the objects are expressed in the coordinate system of the map they are in,
 * while view space is expressed in the pixel coordinates on the GraphicsContext.
 * </p>
 * <p>
 * Note that this class has no support for rotating maps, perhaps in the future we might need this...
 * </p>
 * 
 * @author Pieter De Graef
 */
public class WorldViewTransformer {

	/**
	 * The central viewing object on a map. It contains all the necessary parameters to calculate correct
	 * transformations.
	 */
	private MapView mapView;

	/**
	 * Shortcut to the MapView's camera object.
	 */
	private Camera camera;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Creates a transformation object, based on a <code>MapView</code> object. This <code>MapView</code> is the central
	 * viewing object of a map.
	 */
	public WorldViewTransformer(MapView mapView) {
		this.mapView = mapView;
		this.camera = mapView.getCamera();
	}

	// -------------------------------------------------------------------------
	// Transformation functions:
	// -------------------------------------------------------------------------

	/**
	 * Transform a single coordinate from world space to view space.
	 * 
	 * @param coordinate
	 *            The coordinate in world space.
	 * @return Returns a new coordinate that is the view space equivalent of the given coordinate.
	 */
	public Coordinate worldToView(Coordinate coordinate) {
		if (coordinate != null) {
			Vector2D position = new Vector2D(coordinate);
			double scale = mapView.getCurrentScale();
			position.scale(scale, -scale);
			double translateX = -(camera.getX() * scale) + (mapView.getWidth() / 2);
			double translateY = (camera.getY() * scale) + (mapView.getHeight() / 2);
			position.translate(translateX, translateY);

			if (camera.getAlpha() != 0) {
				// TODO: implement rotation support.
			}

			return new Coordinate(position.getX(), position.getY());
		}
		return null;
	}

	/**
	 * Transform an entire geometry from world space to view space.
	 * 
	 * @param geometry
	 *            The geometry to transform.
	 * @return Returns a new geometry that is the view space equivalent of the given geometry.
	 */
	public Geometry worldToView(Geometry geometry) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = worldToView(geometry.getCoordinate());
				return geometry.getGeometryFactory().createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToView(geometry.getCoordinates()[i]);
				}
				return geometry.getGeometryFactory().createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToView(geometry.getCoordinates()[i]);
				}
				return geometry.getGeometryFactory().createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) worldToView(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) worldToView(polygon.getInteriorRingN(n));
				}
				return polygon.getGeometryFactory().createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) worldToView(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) worldToView(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) worldToView(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiPolygon(polygons);
			}
		}
		return null;
	}

	/**
	 * Transform a bounding box from world- to view space.
	 * 
	 * @param bbox
	 *            The bounding box in world coordinates.
	 * @returns The view space equivalent of the given bounding box.
	 */
	public Bbox worldToView(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = worldToView(bbox.getOrigin());
			Coordinate c2 = worldToView(bbox.getEndPoint());
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	public Coordinate worldToPan(Coordinate coordinate) {
		if (coordinate != null) {
			Vector2D position = new Vector2D(coordinate);
			double scale = mapView.getCurrentScale();
			position.scale(scale, -scale);
			Coordinate panOrigin = mapView.getPanOrigin();
			position.translate(-(panOrigin.getX() * scale), panOrigin.getY() * scale);

			if (camera.getAlpha() != 0) {
				// TODO: implement rotation support.
			}

			return new Coordinate(position.getX(), position.getY());
		}
		return null;
	}

	public Bbox worldToPan(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = worldToPan(bbox.getOrigin());
			Coordinate c2 = worldToPan(bbox.getEndPoint());
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	public Geometry worldToPan(Geometry geometry) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = worldToPan(geometry.getCoordinate());
				return geometry.getGeometryFactory().createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToPan(geometry.getCoordinates()[i]);
				}
				return geometry.getGeometryFactory().createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToPan(geometry.getCoordinates()[i]);
				}
				return geometry.getGeometryFactory().createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) worldToPan(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) worldToPan(polygon.getInteriorRingN(n));
				}
				return polygon.getGeometryFactory().createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) worldToPan(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) worldToPan(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) worldToPan(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiPolygon(polygons);
			}
		}
		return null;
	}

	/**
	 * Transform a coordinate from view space to world space.
	 * 
	 * @param coordinate
	 *            The views pace coordinate.
	 * @return Returns the world space equivalent of the given coordinate.
	 */
	public Coordinate viewToWorld(Coordinate coordinate) {
		if (coordinate != null) {
			Vector2D position = new Vector2D(coordinate);
			double inverseScale = 1 / mapView.getCurrentScale();
			position.scale(inverseScale, -inverseScale);

			Bbox bounds = mapView.getBounds();
			// -cam: center X axis around cam. +bbox.w/2: to place the origin in the center of the screen
			double translateX = -camera.getX() + (bounds.getWidth() / 2);
			double translateY = -camera.getY() - (bounds.getHeight() / 2); // Inverted Y-axis here...
			position.translate(-translateX, -translateY);

			if (camera.getAlpha() != 0) {
				// TODO: implement rotation support.
			}

			return new Coordinate(position.getX(), position.getY());
		}
		return null;
	}

	/**
	 * Transform an entire geometry from view space to world space.
	 * 
	 * @param geometry
	 *            The geometry to transform.
	 * @return Returns a new geometry that is the world space equivalent of the given geometry.
	 */
	public Geometry viewToWorld(Geometry geometry) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = viewToWorld(geometry.getCoordinate());
				return geometry.getGeometryFactory().createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = viewToWorld(geometry.getCoordinates()[i]);
				}
				return geometry.getGeometryFactory().createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = viewToWorld(geometry.getCoordinates()[i]);
				}
				return geometry.getGeometryFactory().createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) viewToWorld(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) viewToWorld(polygon.getInteriorRingN(n));
				}
				return polygon.getGeometryFactory().createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) viewToWorld(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) viewToWorld(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) viewToWorld(geometry.getGeometryN(n));
				}
				return geometry.getGeometryFactory().createMultiPolygon(polygons);
			}
		}
		return null;
	}

	/**
	 * Transform a bounding box from view space to world space.
	 * 
	 * @param bbox
	 *            The bounding box in view coordinates.
	 * @returns The world space equivalent of the given bounding box.
	 */
	public Bbox viewToWorld(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = viewToWorld(bbox.getOrigin());
			Coordinate c2 = viewToWorld(bbox.getEndPoint());
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	/**
	 * Transform a bounding box by a given <code>Matrix</code>.
	 * 
	 * @param bbox
	 *            The bounding box to transform.
	 * @param matrix
	 *            The transformation matrix.
	 * @return Returns a transformed bounding box, or null if one of the given parameters was null.
	 */
	public Bbox transform(Bbox bbox, Matrix matrix) {
		if (bbox != null) {
			Coordinate c1 = transform(bbox.getOrigin(), matrix);
			Coordinate c2 = transform(bbox.getEndPoint(), matrix);
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return new Bbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	/**
	 * Transform a single coordinate by a given <code>Matrix</code>.
	 * 
	 * @param coordinate
	 *            The coordinate to transform.
	 * @param matrix
	 *            The transformation matrix.
	 * @return Returns a transformed coordinate, or null if one of the given parameters was null.
	 */
	public Coordinate transform(Coordinate coordinate, Matrix matrix) {
		if (coordinate != null && matrix != null) {
			double x = matrix.getXx() * coordinate.getX() + matrix.getXy() * coordinate.getY() + matrix.getDx();
			double y = matrix.getYx() * coordinate.getX() + matrix.getYy() * coordinate.getY() + matrix.getDy();
			return new Coordinate(x, y);
		}
		return null;
	}

	public Geometry transform(Geometry geometry, Matrix matrix) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = transform(geometry.getCoordinate(), matrix);
				return geometry.getGeometryFactory().createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = transform(geometry.getCoordinates()[i], matrix);
				}
				return geometry.getGeometryFactory().createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = transform(geometry.getCoordinates()[i], matrix);
				}
				return geometry.getGeometryFactory().createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) transform(polygon.getExteriorRing(), matrix);
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) transform(polygon.getInteriorRingN(n), matrix);
				}
				return polygon.getGeometryFactory().createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) transform(geometry.getGeometryN(n), matrix);
				}
				return geometry.getGeometryFactory().createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) transform(geometry.getGeometryN(n), matrix);
				}
				return geometry.getGeometryFactory().createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) transform(geometry.getGeometryN(n), matrix);
				}
				return geometry.getGeometryFactory().createMultiPolygon(polygons);
			}
		}
		return null;
	}
}
