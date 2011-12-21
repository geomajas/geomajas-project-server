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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * Service definition for operations on geometries.
 * 
 * @author Pieter De Graef
 */
public interface GeometryService {

	/**
	 * Create a clone of the given geometry.
	 * 
	 * @param geometry
	 *            The geometry to clone.
	 * @return The new clone geometry.
	 */
	Geometry clone(Geometry geometry);

	/**
	 * Get the closest {@link Bbox} around the geometry.
	 * 
	 * @param geometry
	 *            The geometry to calculate a bounding box for.
	 */
	Bbox getBounds(Geometry geometry);

	/**
	 * Transform the given bounding box into a polygon geometry.
	 * 
	 * @param bounds
	 *            The bounding box to transform.
	 * @return Returns the polygon equivalent of the given bounding box.
	 */
	Geometry toPolygon(Bbox bounds);

	/**
	 * Return the total number of coordinates within the geometry. This add up all coordinates within the
	 * sub-geometries.
	 * 
	 * @param geometry
	 *            The geometry to calculate the total number of points for.
	 */
	int getNumPoints(Geometry geometry);

	/**
	 * This geometry is empty if there are no geometries/coordinates stored inside.
	 * 
	 * @param geometry
	 *            The geometry to check.
	 * @return true or false.
	 */
	boolean isEmpty(Geometry geometry);

	/**
	 * Basically this function checks if the geometry is self-intersecting or not.
	 * 
	 * @param geometry
	 *            The geometry to check.
	 * @return True or false. True if there are no self-intersections in the geometry.
	 */
	boolean isSimple(Geometry geometry);

	/**
	 * Is the geometry a valid one? Different rules apply to different geometry types.
	 * 
	 * @param geometry
	 *            The geometry to check.
	 * @return True or false.
	 */
	boolean isValid(Geometry geometry);

	/**
	 * Calculate whether or not two given geometries intersect each other.
	 * 
	 * @param one
	 *            The first geometry to check for intersection with the second.
	 * @param two
	 *            The second geometry to check for intersection with the first.
	 * @return Returns true or false.
	 */
	boolean intersects(Geometry one, Geometry two);

	/**
	 * Return the area of the geometry.
	 * 
	 * @param geometry
	 *            The other geometry to calculate the area for.
	 */
	double getArea(Geometry geometry);

	/**
	 * Return the length of the geometry.
	 * 
	 * @param geometry
	 *            The other geometry to calculate the length for.
	 */
	double getLength(Geometry geometry);

	/**
	 * The centroid is also known as the "center of gravity" or the "center of mass".
	 * 
	 * @param geometry
	 *            The other geometry to calculate the centroid for.
	 * 
	 * @return Return the center point.
	 */
	Coordinate getCentroid(Geometry geometry);

	/**
	 * Return the minimal distance between a coordinate and any vertex of a geometry.
	 * 
	 * @param geometry
	 *            The other geometry to calculate the distance for.
	 * @param coordinate
	 *            The coordinate for which to calculate the distance to the geometry.
	 * @return Return the minimal distance
	 */
	double getDistance(Geometry geometry, Coordinate coordinate);

	/**
	 * Create a Well Known Text representation of the geometry.
	 * 
	 * @param geometry
	 *            The other geometry to parse.
	 * @return The geometry's WKT.
	 */
	String toWkt(Geometry geometry);
}