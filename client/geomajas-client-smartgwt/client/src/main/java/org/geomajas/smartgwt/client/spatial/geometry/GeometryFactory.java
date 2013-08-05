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

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.Bbox;

/**
 * The main factory class for creating geometry objects on the GWT client.
 * 
 * @author Pieter De Graef
 */
public class GeometryFactory {

	public static final int PARAM_DEFAULT_PRECISION = 5;

	private int srid;

	private int precision;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public GeometryFactory(int srid, int precision) {
		this.srid = srid;
		this.precision = precision;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public int getSrid() {
		return srid;
	}

	/**
	 * Set a new Spatial Reference id for this factory.
	 */
	public void setSrid(int srid) {
		this.srid = srid;
	}

	public int getPrecision() {
		return precision;
	}

	/**
	 * Set a new precision for this factory.
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
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
			return new Point(srid, precision);
		}
		return new Point(srid, precision, coordinate.getX(), coordinate.getY());
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
			return new LineString(srid, precision);
		}
		Coordinate[] clones = new Coordinate[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			clones[i] = (Coordinate) coordinates[i].clone();
		}
		return new LineString(srid, precision, clones);
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
			return new MultiLineString(srid, precision);
		}
		LineString[] clones = new LineString[lineStrings.length];
		for (int i = 0; i < lineStrings.length; i++) {
			clones[i] = (LineString) lineStrings[i].clone();
		}
		return new MultiLineString(srid, precision, clones);
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
			return new LinearRing(srid, precision);
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
		return new LinearRing(srid, precision, clones);
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
		return new LinearRing(srid, precision, new Coordinate[] { tl, tr, br, bl, tl });
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
			return new Polygon(srid, precision);
		}
		LinearRing[] clones = null;
		if (interiorRings != null) {
			clones = new LinearRing[interiorRings.length];
			for (int i = 0; i < interiorRings.length; i++) {
				clones[i] = (LinearRing) interiorRings[i].clone();
			}
		}
		return new Polygon(srid, precision, (LinearRing) exteriorRing.clone(), clones);
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
		return new Polygon(srid, precision, new LinearRing(srid, precision, new Coordinate[] { tl, tr, br, bl, tl }),
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
			return new MultiPolygon(srid, precision);
		}
		Polygon[] clones = new Polygon[polygons.length];
		for (int i = 0; i < polygons.length; i++) {
			clones[i] = (Polygon) polygons[i].clone();
		}
		return new MultiPolygon(srid, precision, clones);
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
			return new MultiPoint(srid, precision);
		}
		Point[] clones = new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			clones[i] = (Point) points[i].clone();
		}
		return new MultiPoint(srid, precision, clones);
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
			return new Polygon(srid, precision, exteriorRing, interiorRings);
		} else if (geometry instanceof MultiPoint) {
			Point[] clones = new Point[geometry.getNumGeometries()];
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				clones[n] = createPoint(geometry.getGeometryN(n).getCoordinate());
			}
			return new MultiPoint(srid, precision, clones);
		} else if (geometry instanceof MultiLineString) {
			LineString[] clones = new LineString[geometry.getNumGeometries()];
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				clones[n] = createLineString(geometry.getGeometryN(n).getCoordinates());
			}
			return new MultiLineString(srid, precision, clones);
		} else if (geometry instanceof MultiPolygon) {
			Polygon[] clones = new Polygon[geometry.getNumGeometries()];
			for (int n = 0; n < geometry.getNumGeometries(); n++) {
				clones[n] = (Polygon) createGeometry(geometry.getGeometryN(n));
			}
			return new MultiPolygon(srid, precision, clones);
		}
		return null;
	}
}
