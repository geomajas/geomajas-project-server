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

import java.io.Serializable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;

/**
 * Gwt client-side Geometry representation.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
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
	 * @return a {@link Coordinate} which is a vertex of this <code>Geometry</code>, null if this Geometry is empty
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