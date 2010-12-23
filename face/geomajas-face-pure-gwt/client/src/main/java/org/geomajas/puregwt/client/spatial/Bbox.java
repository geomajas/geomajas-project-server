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
import org.geomajas.global.Api;

/**
 * <p>
 * Definition of an (axis aligned) Bounding Box. Determined by an x-ordinate an y-ordinate, it's width and it's height.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Bbox {

	/**
	 * Is this bounding box equal to the other?
	 * 
	 * @param other
	 *            The other bounding box to compare against.
	 * @param delta
	 *            The delta to use as precision measure.
	 * @return Returns true if all properties are the same.
	 */
	boolean equals(Bbox other, double delta);

	/**
	 * Return the origin (x, y) as a Coordinate.
	 */
	Coordinate getOrigin();

	/**
	 * Get the center of the bounding box as a Coordinate.
	 */
	Coordinate getCenterPoint();

	/**
	 * Moves center to the specified coordinate.
	 * 
	 * @param center
	 *            new center point
	 */
	void setCenterPoint(Coordinate center);

	/**
	 * Get the end-point of the bounding box as a Coordinate.
	 */
	Coordinate getEndPoint();

	/**
	 * Does this bounding box contain the given bounding box?
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return true if the other is completely surrounded by this one, false otherwise.
	 */
	boolean contains(Bbox other);

	/**
	 * Does this bounding box intersect the given bounding box?
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return true if the other intersects this one, false otherwise.
	 */
	boolean intersects(Bbox other);

	/**
	 * Computes the intersection of this bounding box with the specified bounding box.
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return bounding box of intersection or null if they do not intersect.
	 */
	Bbox intersection(Bbox other);

	/**
	 * Calculates the union of 2 bounding boxes.
	 * 
	 * @param other
	 *            The other bounding box. Can be a bounding box with width and height equal to 0.
	 */
	Bbox union(Bbox other);

	/**
	 * Return a new bounding box that has increased in size by adding a range to this bounding box.
	 * 
	 * @param range
	 *            Must be a positive number, otherwise null will be returned.
	 * @return
	 */
	Bbox buffer(double range);

	/**
	 * Return a new bounding box which has the same center position but has been scaled with the specified factor.
	 * 
	 * @param factor
	 *            The scale factor (must be > 0).
	 * @return
	 */
	Bbox scale(double factor);

	/**
	 * Translates this bounds with displacement dx and dy.
	 * 
	 * @param dx
	 *            x displacement
	 * @param dy
	 *            y displacement
	 */
	void translate(double dx, double dy);

	/**
	 * Returns whether or not this bounding box is empty. A bounding box is considered empty when either the width or
	 * the height is equal to zero.
	 */
	boolean isEmpty();

	/**
	 * Return the height of the bounding box.
	 * 
	 * @return The bounding box height.
	 */
	double getHeight();

	/**
	 * Return the width of the bounding box.
	 * 
	 * @return The bounding box width.
	 */
	double getWidth();

	/**
	 * Return the X-ordinate of the bounding box' origin.
	 * 
	 * @return Returns the X-ordinate.
	 */
	double getX();

	/**
	 * Return the Y-ordinate of the bounding box' origin.
	 * 
	 * @return Returns the Y-ordinate.
	 */
	double getY();

	/**
	 * Return the maximum X value of this bounding box. This is the X-ordinate of the origin plus the width.
	 * 
	 * @return Returns the maximum X value of this bounding box.
	 */
	double getMaxX();

	/**
	 * Return the maximum Y value of this bounding box. This is the Y-ordinate of the origin plus the height.
	 * 
	 * @return Returns the maximum Y value of this bounding box.
	 */
	double getMaxY();
}