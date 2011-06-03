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
import org.geomajas.global.FutureApi;

/**
 * The main factory interface for creating geometry objects on the GWT client.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface GeometryFactory {

	int PARAM_DEFAULT_PRECISION = 5;

	double PARAM_DEFAULT_DELTA = Math.pow(10.0, -PARAM_DEFAULT_PRECISION);

	/**
	 * Return a delta value for precision comparing.
	 * 
	 * @return The delta value for precision comparing.
	 */
	double getDelta();

	/**
	 * Create a new bounding box, using the given parameters.
	 * 
	 * @param x
	 *            The X origin.
	 * @param y
	 *            The Y origin.
	 * @param width
	 *            The width of the bounding box.
	 * @param height
	 *            The height of the bounding box.
	 * @return
	 */
	Bbox createBbox(double x, double y, double width, double height);

	/**
	 * Create a clone of the given bounding box.
	 * 
	 * @param original
	 *            The original bounding box to clone.
	 * @return Returns a clone of the original bounding box.
	 */
	Bbox createBbox(Bbox original);

	/**
	 * Create a GWT copy of the given DTO bounding box.
	 * 
	 * @param original
	 *            The original DTO bounding box to copy.
	 * @return Returns a GWT copy of the original DTO bounding box.
	 */
	Bbox createBbox(org.geomajas.geometry.Bbox original);

	/**
	 * Create a new {@link Point}, given a coordinate.
	 * 
	 * @param coordinate
	 *            The {@link Coordinate} object that positions the point.
	 * @return Returns a {@link Point} object.
	 */
	Point createPoint(Coordinate coordinate);

	/**
	 * Create a new {@link LineString}, given an array of coordinates.
	 * 
	 * @param coordinates
	 *            An array of {@link Coordinate} objects.
	 * @return Returns a {@link LineString} object.
	 */
	LineString createLineString(Coordinate[] coordinates);

	/**
	 * Create a new {@link MultiLineString}, given an array of LineStrings.
	 * 
	 * @param lineStrings
	 *            An array of {@link LineString} objects.
	 * @return Returns a {@link MultiLineString} object.
	 */
	MultiLineString createMultiLineString(LineString[] lineStrings);

	/**
	 * Create a new {@link LinearRing}, given an array of coordinates.
	 * 
	 * @param coordinates
	 *            An array of {@link Coordinate} objects. This function checks if the array is closed, and does so
	 *            itself if needed.
	 * @return Returns a {@link LinearRing} object.
	 */
	LinearRing createLinearRing(Coordinate[] coordinates);

	/**
	 * Create a new {@link LinearRing} from a {@link Bbox}.
	 * 
	 * @param bbox
	 *            Bounding box to convert into a {@link LinearRing}.
	 * @return Returns a {@link LinearRing} object.
	 */
	LinearRing createLinearRing(Bbox bbox);

	/**
	 * Create a new {@link Polygon}, given a shell and and array of holes.
	 * 
	 * @param exteriorRing
	 *            A {@link LinearRing} object that represents the outer ring.
	 * @param interiorRings
	 *            An array of {@link LinearRing} objects representing the holes.
	 * @return Returns a {@link Polygon} object.
	 */
	Polygon createPolygon(LinearRing exteriorRing, LinearRing[] interiorRings);

	/**
	 * Create a new {@link Polygon} from a {@link Bbox}.
	 * 
	 * @param bbox
	 *            Bounding box to convert into a {@link Polygon}.
	 * @return Returns a {@link Polygon} object.
	 */
	Polygon createPolygon(Bbox bbox);

	/**
	 * Create a new {@link MultiPolygon}, given an array of polygons.
	 * 
	 * @param polygons
	 *            An array of {@link Polygon} objects .
	 * @return Returns a {@link MultiPolygon} object.
	 */
	MultiPolygon createMultiPolygon(Polygon[] polygons);

	/**
	 * Create a new {@link MultiPoint}, given an array of points.
	 * 
	 * @param points
	 *            An array of {@link Point} objects.
	 * @return Returns a {@link MultiPoint} geometry.
	 */
	MultiPoint createMultiPoint(Point[] points);

	/**
	 * Create a new geometry from an existing geometry. This will basically create a clone.
	 * 
	 * @param geometry
	 *            The original geometry.
	 * @return Returns a clone.
	 */
	Geometry createGeometry(Geometry geometry);
}