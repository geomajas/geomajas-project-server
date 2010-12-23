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

package org.geomajas.puregwt.client.util;

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.spatial.Geometry;

/**
 * General service for calculating mathematical properties of geometries.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface SpatialService {

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