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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;

/**
 * The main factory interface for creating geometry objects on the GWT client.
 * 
 * @author Pieter De Graef
 */
public interface GeometryFactory {

	/**
	 * Initialization method for this factory.
	 * 
	 * @param srid
	 *            The spatial reference ID to use when creating geometries.
	 * @param precision
	 *            The precision to use when creating geometries.
	 */
	// TODO: replace with assisted inject of upcoming gin release
	public void init(int srid, int precision);

	/**
	 * Return the spatial reference ID that this factory uses when creating new geometries.
	 * 
	 * @return The spatial reference ID.
	 */
	public int getSrid();

	/**
	 * Return the precision that this factory uses when creating new geometries. Number of digits after the comma.
	 * 
	 * @return The precision.
	 */
	public int getPrecision();

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
	public Bbox createBbox(double x, double y, double width, double height);

	/**
	 * Return a delta value for precision comparing.
	 */
	public double getDelta();

	/**
	 * Create a clone of the given bounding box.
	 * 
	 * @param original
	 *            The original bounding box to clone.
	 * @return Returns a clone of the original bounding box.
	 */
	public Bbox createBbox(Bbox original);

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