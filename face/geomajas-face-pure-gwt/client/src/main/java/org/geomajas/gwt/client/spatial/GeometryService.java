package org.geomajas.gwt.client.spatial;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

public interface GeometryService {

	/**
	 * Create a new identical geometry.
	 * 
	 * @return
	 */
	Geometry clone(Geometry geometry);

	/**
	 * Return a vertex of this <code>Geometry</code>. Usually it will be the first one.
	 * 
	 * @return a {@link Coordinate} which is a vertex of this <code>Geometry</code>, null if this Geometry is empty
	 */
	Coordinate getCoordinate(Geometry geometry);

	/**
	 * Return a sub-geometry or null, or the geometry itself. Each implementation of this class should override it.
	 * 
	 * @param n
	 *            Index in the geometry. This can be an integer value or an array of values.
	 * @return A geometry object.
	 */
	Geometry getGeometryN(Geometry geometry, int n);

	/**
	 * Return the number of direct sub-geometries.
	 */
	int getNumGeometries(Geometry geometry);

	/**
	 * Return the closest Bbox around the geometry.
	 */
	Bbox bounds(Geometry geometry);

	/**
	 * Return the number of coordinates.
	 */
	int numPoints(Geometry geometry);

	/**
	 * This geometry is empty if there are no geometries/coordinates stored inside.
	 * 
	 * @return true or false.
	 */
	boolean isEmpty(Geometry geometry);

	/**
	 * Basically this function checks if the geometry is self-intersecting or not.
	 * 
	 * @return True or false. True if there are no self-intersections in the geometry.
	 */
	boolean isSimple(Geometry geometry);

	/**
	 * Is the geometry a valid one? Different rules apply to different geometry types. Each geometry class should
	 * override this!
	 */
	boolean isValid(Geometry geometry);

	/**
	 * Calculate whether or not this geometry intersects with another.
	 * 
	 * @param geometry1
	 *            The first geometry to check for intersection.
	 * @param geometry2
	 *            The second geometry to check for intersection with the first.
	 * @return Returns true or false.
	 */
	boolean intersects(Geometry geometry1, Geometry geometry2);

	/**
	 * Return the area of the geometry.
	 */
	double area(Geometry geometry);

	/**
	 * Return the length of the geometry.
	 */
	double length(Geometry geometry);

	/**
	 * The centroid is also known as the "center of gravity" or the "center of mass".
	 * 
	 * @return Return the center point.
	 */
	Coordinate centroid(Geometry geometry);

	/**
	 * Create a Well Known Text representation of the geometry.
	 * 
	 * @return The geometry's WKT.
	 */
	String toWkt();
}