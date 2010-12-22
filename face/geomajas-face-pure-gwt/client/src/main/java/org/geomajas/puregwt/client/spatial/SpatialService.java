package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;

import com.vividsolutions.jts.geom.LinearRing;

/**
 * General service for calculating mathematical properties of geometries.
 * 
 * @author Pieter De Graef
 */
public interface SpatialService {

	public static final double ZERO = 0.00001;

	/**
	 * Calculates whether or not 2 line-segments intersect.
	 * 
	 * @param c1
	 *            First coordinate of the first line-segment.
	 * @param c2
	 *            Second coordinate of the first line-segment.
	 * @param c3
	 *            First coordinate of the second line-segment.
	 * @param c4
	 *            Second coordinate of the second line-segment.
	 * @return Returns true or false.
	 */
	boolean lineIntersects(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4);

	/**
	 * Calculates the intersection point of 2 lines.
	 * 
	 * @param c1
	 *            First coordinate of the first line.
	 * @param c2
	 *            Second coordinate of the first line.
	 * @param c3
	 *            First coordinate of the second line.
	 * @param c4
	 *            Second coordinate of the second line.
	 * @return Returns a coordinate.
	 */
	Coordinate lineIntersection(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4);

	/**
	 * Calculates the intersection point of 2 line segments.
	 * 
	 * @param c1
	 *            Start point of the first line segment.
	 * @param c2
	 *            End point of the first line segment.
	 * @param c3
	 *            Start point of the second line segment.
	 * @param c4
	 *            End point of the second line segment.
	 * @return Returns a coordinate or null if not a single intersection point.
	 */
	Coordinate lineSegmentIntersection(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4);

	/**
	 * Return the minimal distance between this coordinate and any vertex of the geometry.
	 * 
	 * @return Return the minimal distance
	 */
	double getDistance(Geometry geometry, Coordinate coordinate);

	/**
	 * Return the minimal distance between this coordinate and any vertex of the geometry.
	 * 
	 * @return Return the minimal distance
	 */
	double getDistance(Geometry geometry1, Geometry geometry2);

	/**
	 * Distance between 2 points.
	 * 
	 * @param c1
	 *            First coordinate
	 * @param c2
	 *            Second coordinate
	 */
	double distance(Coordinate c1, Coordinate c2);

	/**
	 * Distance between a point and a line.
	 * 
	 * @param c1
	 *            First coordinate of the line.
	 * @param c2
	 *            Second coordinate of the line.
	 * @param c3
	 *            Coordinate to calculate distance to line from.
	 */
	double distance(Coordinate c1, Coordinate c2, Coordinate c3);

	/**
	 * Does a certain coordinate touch a given geometry?
	 * 
	 * @param geometry
	 *            The geometry to check against.
	 * @param coordinate
	 *            The position to check.
	 * @return Returns true if the coordinate touches the geometry.
	 */
	boolean touches(Geometry geometry, Coordinate coordinate);

	/**
	 * Is a certain coordinate within a given geometry?
	 * 
	 * @param geometry
	 *            The geometry to check against. Only if it has {@link LinearRing}'s, can the coordinate be inside.
	 * @param coordinate
	 *            The position that is possibly within the geometry.
	 * @return Returns true if the coordinate is within the geometry.
	 */
	boolean isWithin(Geometry geometry, Coordinate coordinate);
}
