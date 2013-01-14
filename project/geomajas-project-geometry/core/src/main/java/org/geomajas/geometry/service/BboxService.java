/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry.service;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

/**
 * Service definition for operations on the {@link Bbox} class.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class BboxService {

	private BboxService() {
		// Final class should have a private no-argument constructor.
	}

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
	public static boolean equals(Bbox one, Bbox two, double delta) {
		return equals(one.getX(), two.getX(), delta) && equals(one.getY(), two.getY(), delta)
				&& equals(one.getWidth(), two.getWidth(), delta) && equals(one.getHeight(), two.getHeight(), delta);
	}

	/**
	 * Get the origin of the given bounding box. The origin is the original X,Y.
	 * 
	 * @param bbox
	 *            The bounding box to get the origin for.
	 * @return Return the origin (x, y) as a Coordinate.
	 */
	public static Coordinate getOrigin(Bbox bbox) {
		return new Coordinate(bbox.getX(), bbox.getY());
	}

	/**
	 * Get the center of the bounding box as a Coordinate.
	 * 
	 * @param bbox
	 *            The bounding box to get the center point for.
	 * @return The center of the given bounding box as a new coordinate.
	 */
	public static Coordinate getCenterPoint(Bbox bbox) {
		double centerX = (bbox.getWidth() == 0 ? bbox.getX() : bbox.getX() + bbox.getWidth() / 2);
		double centerY = (bbox.getHeight() == 0 ? bbox.getY() : bbox.getY() + bbox.getHeight() / 2);
		return new Coordinate(centerX, centerY);
	}

	/**
	 * Translate a bounding box by applying a new center point.
	 * 
	 * @param bbox
	 *            The original bounding box to translate. This one will remain untouched.
	 * @param center
	 *            The new center point.
	 * @return The result as a new bounding box.
	 */
	public static Bbox setCenterPoint(Bbox bbox, Coordinate center) {
		double x = center.getX() - 0.5 * bbox.getWidth();
		double y = center.getY() - 0.5 * bbox.getHeight();
		return new Bbox(x, y, bbox.getWidth(), bbox.getHeight());
	}

	/**
	 * Get the end-point (maxX, maxY) of the bounding box as a Coordinate.
	 * 
	 * @param bbox
	 *            The bounding box to get the end point for.
	 * @return The end-point of the given bounding box.
	 */
	public static Coordinate getEndPoint(Bbox bbox) {
		return new Coordinate(bbox.getMaxX(), bbox.getMaxY());
	}

	/**
	 * Does one bounding box contain another?
	 * 
	 * @param parent
	 *            The parent bounding box in the relation. Does this one contain the child?
	 * @param child
	 *            The child bounding box in the relation. Is this one contained within the parent?
	 * @return true if the child is completely contained(surrounded) by the parent, false otherwise.
	 */
	public static boolean contains(Bbox parent, Bbox child) {
		if (child.getX() < parent.getX()) {
			return false;
		}
		if (child.getY() < parent.getY()) {
			return false;
		}
		if (child.getMaxX() > parent.getMaxX()) {
			return false;
		}
		if (child.getMaxY() > parent.getMaxY()) {
			return false;
		}
		return true;
	}

	/**
	 * Is the given coordinate contained within the bounding box or not? If the coordinate is on the bounding box
	 * border, it is considered outside.
	 * 
	 * @param bbox
	 *            The bounding box.
	 * @param coordinate
	 *            The coordinate to check.
	 * @return True if the coordinate is within the bounding box, false otherwise.
	 * @since 1.1.0
	 */
	public static boolean contains(Bbox bbox, Coordinate coordinate) {
		if (bbox.getX() >= coordinate.getX()) {
			return false;
		}
		if (bbox.getY() >= coordinate.getY()) {
			return false;
		}
		if (bbox.getMaxX() <= coordinate.getX()) {
			return false;
		}
		if (bbox.getMaxY() <= coordinate.getY()) {
			return false;
		}
		return true;
	}

	/**
	 * Does one bounding box intersect another?
	 * 
	 * @param one
	 *            The first bounding box.
	 * @param two
	 *            The second bounding box.
	 * @return true if the both bounding boxes intersect, false otherwise.
	 */
	public static boolean intersects(Bbox one, Bbox two) {
		if (two.getX() > one.getMaxX()) {
			return false;
		}
		if (two.getY() > one.getMaxY()) {
			return false;
		}
		if (two.getMaxX() < one.getX()) {
			return false;
		}
		if (two.getMaxY() < one.getY()) {
			return false;
		}
		return true;
	}

	/**
	 * Calculates the intersection between 2 bounding boxes.
	 * 
	 * @param one
	 *            The first bounding box.
	 * @param two
	 *            The second bounding box.
	 * @return A new bounding box representing intersection or null if they do not intersect.
	 */
	public static Bbox intersection(Bbox one, Bbox two) {
		if (!intersects(one, two)) {
			return null;
		} else {
			double minx = two.getX() > one.getX() ? two.getX() : one.getX();
			double maxx = two.getMaxX() < one.getMaxX() ? two.getMaxX() : one.getMaxX();
			double miny = two.getY() > one.getY() ? two.getY() : one.getY();
			double maxy = two.getMaxY() < one.getMaxY() ? two.getMaxY() : one.getMaxY();
			return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
		}
	}

	/**
	 * Calculates the union of 2 bounding boxes.
	 * 
	 * @param one
	 *            The first bounding box.
	 * @param two
	 *            The second bounding box.
	 * @return A new bounding box representing the union.
	 */
	public static Bbox union(Bbox one, Bbox two) {
		if (two.getWidth() == 0 && two.getHeight() == 0 && two.getX() == 0 && two.getY() == 0) {
			return new Bbox(one.getX(), one.getY(), one.getWidth(), one.getHeight());
		}
		if (one.getWidth() == 0 && one.getHeight() == 0 && one.getX() == 0 && one.getY() == 0) {
			return new Bbox(two.getX(), two.getY(), two.getWidth(), two.getHeight());
		}

		double minx = two.getX() < one.getX() ? two.getX() : one.getX();
		double maxx = two.getMaxX() > one.getMaxX() ? two.getMaxX() : one.getMaxX();
		double miny = two.getY() < one.getY() ? two.getY() : one.getY();
		double maxy = two.getMaxY() > one.getMaxY() ? two.getMaxY() : one.getMaxY();
		return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
	}

	/**
	 * Return a new bounding box that has increased in size by adding a range to a given bounding box.
	 * 
	 * @param bbox
	 *            The original bounding box to calculate a buffer for. This one will remain untouched.
	 * @param range
	 *            Must be a positive number, otherwise null will be returned.
	 * @return The result as a new bounding box.
	 */
	public static Bbox buffer(Bbox bbox, double range) {
		if (range >= 0) {
			double r2 = range * 2;
			return new Bbox(bbox.getX() - range, bbox.getY() - range, bbox.getWidth() + r2, bbox.getHeight() + r2);
		}
		throw new IllegalArgumentException("Buffer range must always be positive.");
	}

	/**
	 * Return a new bounding box which has the same center position but has been scaled with the specified factor.
	 * 
	 * @param bbox
	 *            The original bounding box to scale. This one will remain untouched.
	 * @param factor
	 *            The scale factor (must be > 0).
	 * @return The result as a new bounding box.
	 */
	public static Bbox scale(Bbox bbox, double factor) {
		if (factor > 0) {
			double scaledWidth = bbox.getWidth() * factor;
			double scaledHeight = bbox.getHeight() * factor;
			Coordinate center = getCenterPoint(bbox);
			return new Bbox(center.getX() - scaledWidth / 2, center.getY() - scaledHeight / 2, scaledWidth,
					scaledHeight);
		}
		throw new IllegalArgumentException("Scale factor must always be strictly positive.");
	}

	/**
	 * Translate the given bounding box.
	 * 
	 * @param bbox
	 *            The original bounding box to scale. This one will remain untouched.
	 * @param deltaX
	 *            Translation factor along the X-axis.
	 * @param deltaY
	 *            Translation factor along the Y-axis.
	 * @return The result as a new bounding box.
	 */
	public static Bbox translate(Bbox bbox, double deltaX, double deltaY) {
		return new Bbox(bbox.getX() + deltaX, bbox.getY() + deltaY, bbox.getWidth(), bbox.getHeight());
	}

	/**
	 * Returns whether or not the given bounding box is empty. A bounding box is considered empty when either the width
	 * or the height is equal to zero.
	 * 
	 * @param bbox
	 *            The bounding box to check.
	 * @return True or false.
	 */
	public static boolean isEmpty(Bbox bbox) {
		return bbox.getWidth() == 0 || bbox.getHeight() == 0;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static boolean equals(double d1, double d2, double delta) {
		return Math.abs(d1 - d2) <= delta;
	}
}