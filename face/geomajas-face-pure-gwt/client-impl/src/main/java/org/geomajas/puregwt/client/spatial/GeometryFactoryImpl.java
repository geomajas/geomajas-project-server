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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;

import com.google.inject.Inject;

/**
 * The main factory class for creating geometry objects on the GWT client.
 * 
 * @author Pieter De Graef
 */
public class GeometryFactoryImpl implements GeometryFactory {

	public static final int PARAM_DEFAULT_PRECISION = 5;

	public static final int PARAM_DEFAULT_SRID = 0;

	private double delta;

	@Inject
	private MathService mathService;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	@Inject
	public GeometryFactoryImpl() {
		delta = PARAM_DEFAULT_DELTA;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double getDelta() {
		return delta;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Create a new {@link Point}, given a coordinate.
	 * 
	 * @param coordinate
	 *            The {@link Coordinate} object that positions the point.
	 * @return Returns a {@link Point} object.
	 */
	public Point createPoint(Coordinate coordinate) {
		if (coordinate == null) {
			return new PointImpl(mathService, null);
		}
		return new PointImpl(mathService, new Coordinate(coordinate.getX(), coordinate.getY()));
	}

	/**
	 * Create a new {@link LineString}, given an array of coordinates.
	 * 
	 * @param coordinates
	 *            An array of {@link Coordinate} objects.
	 * @return Returns a {@link LineString} object.
	 */
	public LineString createLineString(Coordinate[] coordinates) {
		if (coordinates == null) {
			return new LineStringImpl(mathService, null);
		}
		Coordinate[] clones = new Coordinate[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			clones[i] = (Coordinate) coordinates[i].clone();
		}
		return new LineStringImpl(mathService, clones);
	}

	/**
	 * Create a new {@link MultiLineString}, given an array of LineStrings.
	 * 
	 * @param lineStrings
	 *            An array of {@link LineString} objects.
	 * @return Returns a {@link MultiLineString} object.
	 */
	public MultiLineString createMultiLineString(LineString[] lineStrings) {
		if (lineStrings == null) {
			return new MultiLineStringImpl(mathService, null);
		}
		LineString[] clones = new LineString[lineStrings.length];
		for (int i = 0; i < lineStrings.length; i++) {
			clones[i] = createLineString(lineStrings[i].getCoordinates());
		}
		return new MultiLineStringImpl(mathService, clones);
	}

	/**
	 * Create a new {@link LinearRing}, given an array of coordinates.
	 * 
	 * @param coordinates
	 *            An array of {@link Coordinate} objects. This function checks if the array is closed, and does so
	 *            itself if needed.
	 * @return Returns a {@link LinearRing} object.
	 */
	public LinearRing createLinearRing(Coordinate[] coordinates) {
		if (coordinates == null || coordinates.length == 0) {
			return new LinearRingImpl(mathService, null);
		}
		boolean isClosed = true;
		if (coordinates.length == 1 || !coordinates[0].equals(coordinates[coordinates.length - 1])) {
			isClosed = false;
		}

		Coordinate[] clones;
		if (isClosed) {
			clones = new Coordinate[coordinates.length];
		} else {
			clones = new Coordinate[coordinates.length + 1];
		}
		for (int i = 0; i < coordinates.length; i++) {
			clones[i] = (Coordinate) coordinates[i].clone();
		}
		if (!isClosed) {
			clones[coordinates.length] = (Coordinate) clones[0].clone();
		}
		return new LinearRingImpl(mathService, clones);
	}

	/**
	 * Create a new {@link LinearRing} from a {@link Bbox}.
	 * 
	 * @param bbox
	 *            Bounding box to convert into a {@link LinearRing}.
	 * @return Returns a {@link LinearRing} object.
	 */
	public LinearRing createLinearRing(Bbox bbox) {
		Coordinate tl = new Coordinate(bbox.getX(), bbox.getY());
		Coordinate tr = new Coordinate(bbox.getX() + bbox.getWidth(), bbox.getY());
		Coordinate br = new Coordinate(bbox.getX() + bbox.getWidth(), bbox.getY() + bbox.getHeight());
		Coordinate bl = new Coordinate(bbox.getX(), bbox.getY() + bbox.getHeight());
		return new LinearRingImpl(mathService, new Coordinate[] { tl, tr, br, bl, tl });
	}

	/**
	 * Create a new {@link Polygon}, given a shell and and array of holes.
	 * 
	 * @param exteriorRing
	 *            A {@link LinearRing} object that represents the outer ring.
	 * @param interiorRings
	 *            An array of {@link LinearRing} objects representing the holes.
	 * @return Returns a {@link Polygon} object.
	 */
	public Polygon createPolygon(LinearRing exteriorRing, LinearRing[] interiorRings) {
		if (exteriorRing == null) {
			return new PolygonImpl(mathService, null, null);
		}
		LinearRing[] clones = null;
		if (interiorRings != null) {
			clones = new LinearRing[interiorRings.length];
			for (int i = 0; i < interiorRings.length; i++) {
				clones[i] = createLinearRing(interiorRings[i].getCoordinates());
			}
		}
		return new PolygonImpl(mathService, createLinearRing(exteriorRing.getCoordinates()), clones);
	}

	/**
	 * Create a new {@link Polygon} from a {@link Bbox}.
	 * 
	 * @param bbox
	 *            Bounding box to convert into a {@link Polygon}.
	 * @return Returns a {@link Polygon} object.
	 */
	public Polygon createPolygon(Bbox bbox) {
		Coordinate tl = new Coordinate(bbox.getX(), bbox.getY());
		Coordinate tr = new Coordinate(bbox.getX() + bbox.getWidth(), bbox.getY());
		Coordinate br = new Coordinate(bbox.getX() + bbox.getWidth(), bbox.getY() + bbox.getHeight());
		Coordinate bl = new Coordinate(bbox.getX(), bbox.getY() + bbox.getHeight());
		return new PolygonImpl(mathService, new LinearRingImpl(mathService, new Coordinate[] { tl, tr, br, bl, tl }),
				null);
	}

	/**
	 * Create a new {@link MultiPolygon}, given an array of polygons.
	 * 
	 * @param polygons
	 *            An array of {@link Polygon} objects .
	 * @return Returns a {@link MultiPolygon} object.
	 */
	public MultiPolygon createMultiPolygon(Polygon[] polygons) {
		if (polygons == null) {
			return new MultiPolygonImpl(mathService, null);
		}
		Polygon[] clones = new Polygon[polygons.length];
		for (int i = 0; i < polygons.length; i++) {
			clones[i] = createPolygon(polygons[i].getExteriorRing(), ((PolygonImpl) polygons[i]).getInteriorRings());
		}
		return new MultiPolygonImpl(mathService, clones);
	}

	/**
	 * Create a new {@link MultiPoint}, given an array of points.
	 * 
	 * @param points
	 *            An array of {@link Point} objects.
	 * @return Returns a {@link MultiPoint} geometry.
	 */
	public MultiPoint createMultiPoint(Point[] points) {
		if (points == null) {
			return new MultiPointImpl(mathService, null);
		}
		Point[] clones = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			clones[i] = createPoint(points[i].getCoordinate());
		}
		return new MultiPointImpl(mathService, clones);
	}

	/**
	 * Create a new geometry from an existing geometry. This will basically create a clone.
	 * 
	 * @param geometry
	 *            The original geometry.
	 * @return Returns a clone.
	 */
	public Geometry createGeometry(Geometry geometry) {
		if (geometry instanceof Point) {
			return createPoint(geometry.getCoordinate());
		} else if (geometry instanceof LinearRing) {
			return createLinearRing(geometry.getCoordinates());
		} else if (geometry instanceof LineString) {
			return createLineString(geometry.getCoordinates());
		} else if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			LinearRing exteriorRing = createLinearRing(polygon.getExteriorRing().getCoordinates());
			LinearRing[] interiorRings = new LinearRing[polygon.getNumInteriorRing()];
			for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
				interiorRings[n] = createLinearRing(polygon.getInteriorRingN(n).getCoordinates());
			}
			return new PolygonImpl(mathService, exteriorRing, interiorRings);
		} else if (geometry instanceof MultiPoint) {
			Point[] clones = new Point[geometry.getNumGeometries()];
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				clones[n] = createPoint(geometry.getGeometryN(n).getCoordinate());
			}
			return new MultiPointImpl(mathService, clones);
		} else if (geometry instanceof MultiLineString) {
			LineString[] clones = new LineString[geometry.getNumGeometries()];
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				clones[n] = createLineString(geometry.getGeometryN(n).getCoordinates());
			}
			return new MultiLineStringImpl(mathService, clones);
		} else if (geometry instanceof MultiPolygon) {
			Polygon[] clones = new Polygon[geometry.getNumGeometries()];
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				clones[n] = (Polygon) createGeometry(geometry.getGeometryN(n));
			}
			return new MultiPolygonImpl(mathService, clones);
		}
		return null;
	}

	public Bbox createBbox(double x, double y, double width, double height) {
		return new BboxImpl(x, y, width, height);
	}

	public Bbox createBbox(Bbox original) {
		return new BboxImpl(original.getX(), original.getY(), original.getWidth(), original.getHeight());
	}

	public Bbox createBbox(org.geomajas.geometry.Bbox original) {
		return new BboxImpl(original.getX(), original.getY(), original.getWidth(), original.getHeight());
	}
}