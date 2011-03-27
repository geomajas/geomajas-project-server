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

import com.google.inject.Inject;

/**
 * Implementation of the transformation methods of the view port.
 * 
 * @author Pieter De Graef
 */
public class ViewPortTransformationService {

	@Inject
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
			case PAN:
				switch (to) {
					case PAN:
						return coordinate;
					case SCREEN:
						throw new RuntimeException("Not implemented.");
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case SCREEN:
				switch (to) {
					case PAN:
						return viewToPan(coordinate);
					case SCREEN:
						return coordinate;
					case WORLD:
						return viewToWorld(coordinate);
				}
			case WORLD:
				switch (to) {
					case PAN:
						return worldToPan(coordinate);
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
			case PAN:
				switch (to) {
					case PAN:
						return geometry;
					case SCREEN:
						throw new RuntimeException("Not implemented.");
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case SCREEN:
				switch (to) {
					case PAN:
						return viewToPan(geometry);
					case SCREEN:
						return geometry;
					case WORLD:
						return viewToWorld(geometry);
				}
			case WORLD:
				switch (to) {
					case PAN:
						return worldToPan(geometry);
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
			case PAN:
				switch (to) {
					case PAN:
						return bbox;
					case SCREEN:
						throw new RuntimeException("Not implemented.");
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case SCREEN:
				switch (to) {
					case PAN:
						return viewToPan(bbox);
					case SCREEN:
						return bbox;
					case WORLD:
						return viewToWorld(bbox);
				}
			case WORLD:
				switch (to) {
					case PAN:
						return worldToPan(bbox);
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
			case PAN:
				switch (to) {
					case PAN:
						return MatrixImpl.IDENTITY;
					case SCREEN:
						throw new RuntimeException("Not implemented.");
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case SCREEN:
				switch (to) {
					case PAN:
						throw new RuntimeException("Not implemented.");
					case SCREEN:
						return MatrixImpl.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case PAN:
						return getWorldToViewTransformation();
					case SCREEN:
						return getWorldToPanTransformation();
					case WORLD:
						return MatrixImpl.IDENTITY;
				}
		}
		return null;
	}

	public MatrixImpl getTranslationMatrix(RenderSpace from, RenderSpace to) {
		switch (from) {
			case PAN:
				switch (to) {
					case PAN:
						return MatrixImpl.IDENTITY;
					case SCREEN:
						return getPanToViewTranslation();
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case SCREEN:
				switch (to) {
					case PAN:
						throw new RuntimeException("Not implemented.");
					case SCREEN:
						return MatrixImpl.IDENTITY;
					case WORLD:
						throw new RuntimeException("Not implemented.");
				}
			case WORLD:
				switch (to) {
					case PAN:
						return getWorldToPanTranslation();
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

	/** Return the world-to-pan space translation matrix. */
	public MatrixImpl getWorldToPanTransformation() {
		if (viewPort.getScale() > 0) {
			double dX = -(viewPort.getDragOrigin().getX() * viewPort.getScale());
			double dY = viewPort.getDragOrigin().getY() * viewPort.getScale();
			return new MatrixImpl(viewPort.getScale(), 0, 0, -viewPort.getScale(), dX, dY);
		}
		return new MatrixImpl(1, 0, 0, 1, 0, 0);
	}

	/**
	 * Return the translation of scaled world coordinates to coordinates relative to the pan origin.<br/>
	 */
	public MatrixImpl getWorldToPanTranslation() {
		if (viewPort.getScale() > 0) {
			double dX = -(viewPort.getDragOrigin().getX() * viewPort.getScale());
			double dY = viewPort.getDragOrigin().getY() * viewPort.getScale();
			return new MatrixImpl(0, 0, 0, 0, dX, dY);
		}
		return new MatrixImpl(0, 0, 0, 0, 0, 0);
	}

	/**
	 * Return the translation of coordinates relative to the pan origin to view coordinates.<br/>
	 */
	public MatrixImpl getPanToViewTranslation() {
		if (viewPort.getScale() > 0) {
			double dX = -((viewPort.getPosition().getX() - viewPort.getDragOrigin().getX()) * viewPort.getScale())
					+ viewPort.getMapWidth() / 2;
			double dY = (viewPort.getPosition().getY() - viewPort.getDragOrigin().getY()) * viewPort.getScale()
					+ viewPort.getMapHeight() / 2;
			return new MatrixImpl(0, 0, 0, 0, dX, dY);
		}
		return new MatrixImpl(0, 0, 0, 0, 0, 0);
	}

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

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	/**
	 * Transform a bounding box by a given <code>Matrix</code>.
	 * 
	 * @param bbox
	 *            The bounding box to transform.
	 * @param matrix
	 *            The transformation matrix.
	 * @return Returns a transformed bounding box, or null if one of the given parameters was null.
	 */
	protected Bbox transform(Bbox bbox, MatrixImpl matrix) {
		if (bbox != null) {
			Coordinate c1 = transform(bbox.getOrigin(), matrix);
			Coordinate c2 = transform(bbox.getEndPoint(), matrix);
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return factory.createBbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
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
	protected Coordinate transform(Coordinate coordinate, MatrixImpl matrix) {
		if (coordinate != null && matrix != null) {
			double x = matrix.getXx() * coordinate.getX() + matrix.getXy() * coordinate.getY() + matrix.getDx();
			double y = matrix.getYx() * coordinate.getX() + matrix.getYy() * coordinate.getY() + matrix.getDy();
			return new Coordinate(x, y);
		}
		return null;
	}

	protected Geometry transform(Geometry geometry, MatrixImpl matrix) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = transform(geometry.getCoordinate(), matrix);
				return factory.createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = transform(geometry.getCoordinates()[i], matrix);
				}
				return factory.createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = transform(geometry.getCoordinates()[i], matrix);
				}
				return factory.createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) transform(polygon.getExteriorRing(), matrix);
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) transform(polygon.getInteriorRingN(n), matrix);
				}
				return factory.createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) transform(geometry.getGeometryN(n), matrix);
				}
				return factory.createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) transform(geometry.getGeometryN(n), matrix);
				}
				return factory.createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) transform(geometry.getGeometryN(n), matrix);
				}
				return factory.createMultiPolygon(polygons);
			}
		}
		return null;
	}

	public Coordinate worldToPan(Coordinate coordinate) {
		if (coordinate != null) {
			Vector2D position = new Vector2D(coordinate);
			double scale = viewPort.getScale();
			position.scale(scale, -scale);
			Coordinate panOrigin = viewPort.getDragOrigin();
			position.translate(-(panOrigin.getX() * scale), panOrigin.getY() * scale);
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
			return factory.createBbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	public Geometry worldToPan(Geometry geometry) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = worldToPan(geometry.getCoordinate());
				return factory.createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToPan(geometry.getCoordinates()[i]);
				}
				return factory.createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = worldToPan(geometry.getCoordinates()[i]);
				}
				return factory.createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) worldToPan(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) worldToPan(polygon.getInteriorRingN(n));
				}
				return factory.createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) worldToPan(geometry.getGeometryN(n));
				}
				return factory.createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) worldToPan(geometry.getGeometryN(n));
				}
				return factory.createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) worldToPan(geometry.getGeometryN(n));
				}
				return factory.createMultiPolygon(polygons);
			}
		}
		return null;
	}

	/**
	 * Transform a coordinate from view space to pan space.
	 * 
	 * @param coordinate
	 *            The views pace coordinate.
	 * @return Returns the pan space equivalent of the given coordinate.
	 */
	public Coordinate viewToPan(Coordinate coordinate) {
		if (coordinate != null) {
			Vector2D position = new Vector2D(coordinate);
			double scale = viewPort.getScale();
			Coordinate panOrigin = viewPort.getDragOrigin();

			double translateX = (viewPort.getPosition().getX() - panOrigin.getX()) * scale
					- (viewPort.getMapWidth() / 2);
			double translateY = -(viewPort.getPosition().getY() - panOrigin.getY()) * scale
					- (viewPort.getMapHeight() / 2);
			position.translate(translateX, translateY);
			return new Coordinate(position.getX(), position.getY());
		}
		return null;
	}

	public Bbox viewToPan(Bbox bbox) {
		if (bbox != null) {
			Coordinate c1 = viewToPan(bbox.getOrigin());
			Coordinate c2 = viewToPan(bbox.getEndPoint());
			double x = (c1.getX() < c2.getX()) ? c1.getX() : c2.getX();
			double y = (c1.getY() < c2.getY()) ? c1.getY() : c2.getY();
			return factory.createBbox(x, y, Math.abs(c1.getX() - c2.getX()), Math.abs(c1.getY() - c2.getY()));
		}
		return null;
	}

	public Geometry viewToPan(Geometry geometry) {
		if (geometry != null) {
			if (geometry instanceof Point) {
				Coordinate transformed = viewToPan(geometry.getCoordinate());
				return factory.createPoint(transformed);
			} else if (geometry instanceof LinearRing) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = viewToPan(geometry.getCoordinates()[i]);
				}
				return factory.createLinearRing(coordinates);
			} else if (geometry instanceof LineString) {
				Coordinate[] coordinates = new Coordinate[geometry.getNumPoints()];
				for (int i = 0; i < coordinates.length; i++) {
					coordinates[i] = viewToPan(geometry.getCoordinates()[i]);
				}
				return factory.createLineString(coordinates);
			} else if (geometry instanceof Polygon) {
				Polygon polygon = (Polygon) geometry;
				LinearRing shell = (LinearRing) worldToPan(polygon.getExteriorRing());
				LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
				for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
					holes[n] = (LinearRing) viewToPan(polygon.getInteriorRingN(n));
				}
				return factory.createPolygon(shell, holes);
			} else if (geometry instanceof MultiPoint) {
				Point[] points = new Point[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					points[n] = (Point) viewToPan(geometry.getGeometryN(n));
				}
				return factory.createMultiPoint(points);
			} else if (geometry instanceof MultiLineString) {
				LineString[] lineStrings = new LineString[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					lineStrings[n] = (LineString) viewToPan(geometry.getGeometryN(n));
				}
				return factory.createMultiLineString(lineStrings);
			} else if (geometry instanceof MultiPolygon) {
				Polygon[] polygons = new Polygon[geometry.getNumGeometries()];
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					polygons[n] = (Polygon) viewToPan(geometry.getGeometryN(n));
				}
				return factory.createMultiPolygon(polygons);
			}
		}
		return null;
	}
}