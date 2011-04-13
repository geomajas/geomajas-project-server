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

package org.geomajas.puregwt.client.map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.Geometry;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.GeometryFactoryImpl;
import org.geomajas.puregwt.client.spatial.LineString;
import org.geomajas.puregwt.client.spatial.LinearRing;
import org.geomajas.puregwt.client.spatial.MatrixImpl;
import org.geomajas.puregwt.client.spatial.MultiLineString;
import org.geomajas.puregwt.client.spatial.MultiPoint;
import org.geomajas.puregwt.client.spatial.MultiPolygon;
import org.geomajas.puregwt.client.spatial.Point;
import org.geomajas.puregwt.client.spatial.Polygon;
import org.geomajas.puregwt.client.spatial.Vector2D;

/**
 * Implementation of the transformation methods of the view port.
 * 
 * @author Pieter De Graef
 */
public class ViewPortTransformationService {

	private GeometryFactory factory;

	/**
	 * The central viewing object on a map. It contains all the necessary parameters to calculate correct
	 * transformations.
	 */
	private ViewPortImpl viewPort;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Creates a transformation object, based on a <code>MapView</code> object. This <code>MapView</code> is the central
	 * viewing object of a map.
	 * 
	 * @param viewPort
	 *            The central viewing object on a map. It contains all the necessary parameters to calculate correct
	 *            transformations.
	 */
	protected ViewPortTransformationService(ViewPortImpl viewPort) {
		this.viewPort = viewPort;
		factory = new GeometryFactoryImpl();
	}

	// -------------------------------------------------------------------------
	// ViewPort transformation methods:
	// -------------------------------------------------------------------------

	public Coordinate transform(Coordinate coordinate, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return coordinate;
					case WORLD:
						return viewToWorld(coordinate);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToView(coordinate);
					case WORLD:
						return coordinate;
				}
		}
		return coordinate;
	}

	public Geometry transform(Geometry geometry, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return geometry;
					case WORLD:
						return viewToWorld(geometry);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToView(geometry);
					case WORLD:
						return geometry;
				}
		}
		return geometry;
	}

	public Bbox transform(Bbox bbox, RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return bbox;
					case WORLD:
						return viewToWorld(bbox);
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return worldToView(bbox);
					case WORLD:
						return bbox;
				}
		}
		return bbox;
	}

	public MatrixImpl getTransformationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return MatrixImpl.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return getWorldToViewTransformation();
					case WORLD:
						return MatrixImpl.IDENTITY;
				}
		}
		return null;
	}

	public MatrixImpl getTranslationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case SCREEN:
				switch (to) {
					case SCREEN:
						return MatrixImpl.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case SCREEN:
						return getWorldToViewTranslation();
					case WORLD:
						return MatrixImpl.IDENTITY;
				}
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// Transformation matrices:
	// -------------------------------------------------------------------------

	public MatrixImpl getWorldToViewTransformation() {
		if (viewPort.getScale() > 0) {
			double dX = -(viewPort.getPosition().getX() * viewPort.getScale()) + viewPort.getMapWidth() / 2;
			double dY = viewPort.getPosition().getY() * viewPort.getScale() + viewPort.getMapHeight() / 2;
			return new MatrixImpl(viewPort.getScale(), 0, 0, -viewPort.getScale(), dX, dY);
		}
		return new MatrixImpl(1, 0, 0, 1, 0, 0);
	}

	public MatrixImpl getWorldToViewTranslation() {
		if (viewPort.getScale() > 0) {
			double dX = -(viewPort.getPosition().getX() * viewPort.getScale()) + viewPort.getMapWidth() / 2;
			double dY = viewPort.getPosition().getY() * viewPort.getScale() + viewPort.getMapHeight() / 2;
			return new MatrixImpl(0, 0, 0, 0, dX, dY);
		}
		return new MatrixImpl(0, 0, 0, 0, 0, 0);
	}

	// -------------------------------------------------------------------------
	// Transformation methods:
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
			double scale = viewPort.getScale();
			position.scale(scale, -scale);
			double translateX = -(viewPort.getPosition().getX() * scale) + (viewPort.getMapWidth() / 2);
			double translateY = (viewPort.getPosition().getY() * scale) + (viewPort.getMapHeight() / 2);
			position.translate(translateX, translateY);
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
				return factory.createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToView(geometry.getCoordinates()[i]);
				}
				return factory.createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToView(geometry.getCoordinates()[i]);
				}
				return factory.createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) worldToView(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) worldToView(polygon.getInteriorRingN(n));
				}
				return factory.createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) worldToView(geometry.getGeometryN(n));
				}
				return factory.createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) worldToView(geometry.getGeometryN(n));
				}
				return factory.createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) worldToView(geometry.getGeometryN(n));
				}
				return factory.createMultiPolygon(polygons);
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
			return factory.createBbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
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
			double inverseScale = 1 / viewPort.getScale();
			position.scale(inverseScale, -inverseScale);

			Bbox bounds = viewPort.getBounds();
			// -cam: center X axis around cam. +bbox.w/2: to place the origin in the center of the screen
			double translateX = -viewPort.getPosition().getX() + (bounds.getWidth() / 2);
			double translateY = -viewPort.getPosition().getY() - (bounds.getHeight() / 2); // Inverted Y-axis here...
			position.translate(-translateX, -translateY);
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
				return factory.createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = viewToWorld(geometry.getCoordinates()[i]);
				}
				return factory.createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = viewToWorld(geometry.getCoordinates()[i]);
				}
				return factory.createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) viewToWorld(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) viewToWorld(polygon.getInteriorRingN(n));
				}
				return factory.createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) viewToWorld(geometry.getGeometryN(n));
				}
				return factory.createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) viewToWorld(geometry.getGeometryN(n));
				}
				return factory.createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) viewToWorld(geometry.getGeometryN(n));
				}
				return factory.createMultiPolygon(polygons);
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
			return factory.createBbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}
}