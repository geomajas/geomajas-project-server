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

package org.geomajas.gwt.client.spatial.geometry;

import java.io.Serializable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.LayerType;

/**
 * Gwt client-side Geometry representation.
 *
 * @author Pieter De Graef
 */
public interface Geometry extends Serializable {

	/**
	 * Create a new identical geometry.
	 *
	 * @return cloned object
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
	 * @return precision
	 */
	int getPrecision();

	/**
	 * Return the {@link GeometryFactory} object that corresponds to this geometry.
	 *
	 * @return geometry factory
	 */
	GeometryFactory getGeometryFactory();

	/**
	 * Get an array of coordinates. Never returns null! Where there are no coordinates, an empty array is returned.
	 *
	 * @return coordinates
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
	 *
	 * @return bounds
	 */
	Bbox getBounds();

	/**
	 * Return the number of coordinates.
	 *
	 * @return number of points
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
	 *
	 * @return number of geometries
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
	 *
	 * @return geometry type
	 */
	int getGeometryType();

	/**
	 * Return the layer/geometry type of the geometry. This is the actual type of the geometry itself expressed
	 * as a {@link LayerType} object.
	 *
	 * @return the layer type (vector).
	 */
	LayerType getLayerType();

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
	 *
	 * @return true when this geometry is valid
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
	 *
	 * @return area of geometry in CRS units
	 */
	double getArea();

	/**
	 * Return the length of the geometry.
	 *
	 * @return length of geometry in CRS units
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
	 * @param coordinate coordinate to calculate distance of
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