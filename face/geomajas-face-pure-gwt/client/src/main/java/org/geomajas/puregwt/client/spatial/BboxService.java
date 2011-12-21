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

/**
 * Service definition for operations on bounding boxes.
 * 
 * @author Pieter De Graef
 */
public interface BboxService {

	/**
	 * Is one bounding box equal to the other?
	 * 
	 * @param one
	 *            The first bounding box to compare against.
	 * @param two
	 *            The second bounding box to compare to the first.
	 * @param delta
	 *            The delta to use as precision measure.
	 * @return Returns true if all properties are the same.
	 */
	boolean equals(Bbox one, Bbox two, double delta);

	/**
	 * Get the origin of the given bounding box. The origin is the original X,Y.
	 * 
	 * @param bbox
	 *            The bounding box to get the origin for.
	 * @return Return the origin (x, y) as a Coordinate.
	 */
	Coordinate getOrigin(Bbox bbox);

	/**
	 * Get the center of the bounding box as a Coordinate.
	 * 
	 * @param bbox
	 *            The bounding box to get the center point for.
	 */
	Coordinate getCenterPoint(Bbox bbox);

	/**
	 * Translate a bounding box by applying a new center point.
	 * 
	 * @param bbox
	 *            The original bounding box to translate. This one will remain untouched.
	 * @param center
	 *            The new center point.
	 * @return The result as a bounding box.
	 */
	Bbox setCenterPoint(Bbox bbox, Coordinate center);

	/**
	 * Get the end-point (maxX, maxY) of the bounding box as a Coordinate.
	 * 
	 * @param bbox
	 *            The bounding box to get the end point for.
	 */
	Coordinate getEndPoint(Bbox bbox);

	/**
	 * Does one bounding box contain another?
	 * 
	 * @param parent
	 *            The parent bounding box in the relation. Does this one contain the child?
	 * @param child
	 *            The child bounding box in the relation. Is this one contained within the parent?
	 * @return true if the child is completely contained(surrounded) by the parent, false otherwise.
	 */
	boolean contains(Bbox parent, Bbox child);

	/**
	 * Does one bounding box intersect another?
	 * 
	 * @param one
	 *            The first bounding box.
	 * @param two
	 *            The second bounding box.
	 * @return true if the both bounding boxes intersect, false otherwise.
	 */
	boolean intersects(Bbox one, Bbox two);

	/**
	 * Calculates the intersection between 2 bounding boxes.
	 * 
	 * @param one
	 *            The first bounding box.
	 * @param two
	 *            The second bounding box.
	 * @return bounding box representing intersection or null if they do not intersect.
	 */
	Bbox intersection(Bbox parent, Bbox child);

	/**
	 * Calculates the union of 2 bounding boxes.
	 * 
	 * @param one
	 *            The first bounding box.
	 * @param two
	 *            The second bounding box.
	 * @return bounding box representing the union.
	 */
	Bbox union(Bbox parent, Bbox child);

	/**
	 * Return a new bounding box that has increased in size by adding a range to a given bounding box.
	 * 
	 * @param bbox
	 *            The original bounding box to calculate a buffer for. This one will remain untouched.
	 * @param range
	 *            Must be a positive number, otherwise null will be returned.
	 * @return The result as a new bounding box.
	 */
	Bbox buffer(final Bbox bbox, double range);

	/**
	 * Return a new bounding box which has the same center position but has been scaled with the specified factor.
	 * 
	 * @param bbox
	 *            The original bounding box to scale. This one will remain untouched.
	 * @param factor
	 *            The scale factor (must be > 0).
	 * @return The result as a bounding box.
	 */
	Bbox scale(Bbox bbox, double factor);

	/**
	 * Translate the given bounding box.
	 * 
	 * @param bbox
	 *            The original bounding box to scale. This one will remain untouched.
	 * @param deltaX
	 *            Translation factor along the X-axis.
	 * @param deltaY
	 *            Translation factor along the Y-axis.
	 * @return The result as a bounding box.
	 */
	Bbox translate(Bbox bbox, double deltaX, double deltaY);

	/**
	 * Returns whether or not the given bounding box is empty. A bounding box is considered empty when either the width
	 * or the height is equal to zero.
	 * 
	 * @param bbox
	 *            The bounding box to check.
	 */
	boolean isEmpty(Bbox bbox);
}