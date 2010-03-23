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

import java.io.Serializable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * Gwt client-side Geometry representation.
 *
 * @author Pieter De Graef
 */
public interface Geometry extends Serializable {

	/**
	 * Create a new identical geometry.
	 *
	 * @return
	 */
	Object clone();

	/**
	 * Return the spatial reference ID.
	 *
	 * @return Returns the srid as an integer.
	 */
	int getSrid();

	/**
	 * Return the precision used in this geometry as an integer.
	 *
	 * @return
	 */
	int getPrecision();

	/**
	 * Return the {@link GeometryFactory} object that corresponds to this geometry.
	 */
	GeometryFactory getGeometryFactory();

	/**
	 * Get an array of coordinates. Never returns null! Where there are no coordinates, an empty array is returned.
	 */
	Coordinate[] getCoordinates();

	/**
	 * Return a vertex of this <code>Geometry</code>. Usually it will be the first one.
	 *
	 *@return a {@link Coordinate} which is a vertex of this <code>Geometry</code>, null if this Geometry is empty
	 */
	Coordinate getCoordinate();

	/**
	 * Return the closest Bbox around the geometry.
	 */
	Bbox getBounds();

	/**
	 * Return the number of coordinates.
	 */
	int getNumPoints();

	/**
	 * Return a sub-geometry or null, or the geometry itself. Each implementation of this class should override it.
	 *
	 * @param n
	 *            Index in the geometry. This can be an integer value or an array of values.
	 * @return A geometry object.
	 */
	Geometry getGeometryN(int n);

	/**
	 * Return the number of direct sub-geometries.
	 */
	int getNumGeometries();

	/**
	 * Return the geometry type. Can be one of the following:
	 * <ul>
	 * <li>POINT</li>
	 * <li>LINESTRING</li>
	 * <li>LINEARRING</li>
	 * <li>POLYGON</li>
	 * <li>MULTILINESTRING</li>
	 * <li>MULTIPOLYGON</li>
	 * </ul>
	 */
	int getGeometryType();

	/**
	 * This geometry is empty if there are no geometries/coordinates stored inside.
	 *
	 * @return true or false.
	 */
	boolean isEmpty();

	/**
	 * Basically this function checks if the geometry is self-intersecting or not.
	 *
	 * @return True or false. True if there are no self-intersections in the geometry.
	 */
	boolean isSimple();

	/**
	 * Is the geometry a valid one? Different rules apply to different geometry types. Each geometry class should
	 * override this!
	 */
	boolean isValid();

	/**
	 * Calculate whether or not this geometry intersects with another.
	 *
	 * @param geometry
	 *            The other geometry to check for intersection.
	 * @return Returns true or false.
	 */
	boolean intersects(Geometry geometry);

	/**
	 * Return the area of the geometry.
	 */
	double getArea();

	/**
	 * Return the length of the geometry.
	 */
	double getLength();

	/**
	 * The centroid is also known as the "center of gravity" or the "center of mass".
	 *
	 * @return Return the center point.
	 */
	Coordinate getCentroid();

	/**
	 * Return the minimal distance between this coordinate and any vertex of the geometry.
	 *
	 * @return Return the minimal distance
	 */
	double getDistance(Coordinate coordinate);

	/**
	 * Create a Well Known Text representation of the geometry.
	 *
	 * @return The geometry's WKT.
	 */
	String toWkt();
}