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
import org.geomajas.annotation.FutureApi;

/**
 * General service for calculating mathematical properties of geometries.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface MathService {

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
	boolean intersectsLineSegment(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4);

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
	 * Distance between 2 points.
	 * 
	 * @param c1
	 *            First coordinate
	 * @param c2
	 *            Second coordinate
	 */
	double distance(Coordinate c1, Coordinate c2);

	/**
	 * Distance between a point and a line segment. This method looks at the line segment c1-c2, it does not regard it
	 * as a line. This means that the distance to c is calculated to a point between c1 and c2.
	 * 
	 * @param c1
	 *            First coordinate of the line segment.
	 * @param c2
	 *            Second coordinate of the line segment.
	 * @param coordinate
	 *            Coordinate to calculate distance to line from.
	 */
	double distance(Coordinate c1, Coordinate c2, Coordinate coordinate);

	/**
	 * Calculate which point on a line segment is nearest to the given coordinate. Will be perpendicular or one of the
	 * end-points.
	 * 
	 * @param c1
	 *            First coordinate of the line segment.
	 * @param c2
	 *            Second coordinate of the line segment.
	 * @param coordinate
	 *            The coordinate to search the nearest point for.
	 * @return The point on the line segment nearest to the given coordinate.
	 */
	Coordinate nearest(Coordinate c1, Coordinate c2, Coordinate coordinate);

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