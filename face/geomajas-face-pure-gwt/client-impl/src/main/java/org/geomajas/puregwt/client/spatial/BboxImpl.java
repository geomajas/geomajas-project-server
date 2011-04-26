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

/**
 * <p>
 * Definition of an (axis aligned) Bounding Box. Determined by an x-ordinate an y-ordinate, it's width and it's height.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class BboxImpl implements Bbox {

	/** The ordinate along the X-axis. */
	private double x;

	/** The ordinate along the Y-axis. */
	private double y;

	/** The bounding box' width. */
	private double width;

	/** The bounding box' height. */
	private double height;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Protected constructor that immediately applies values to all the fields.
	 * 
	 * @param x
	 *            The ordinate along the X-axis.
	 * @param y
	 *            The ordinate along the Y-axis.
	 * @param width
	 *            The bounding box' width.
	 * @param height
	 *            The bounding box' height.
	 */
	protected BboxImpl(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Create a clone of the object.
	 * 
	 * @return Returns a new bounding box that equals this one.
	 */
	public Object clone() {
		return new BboxImpl(x, y, width, height);
	}

	/**
	 * Return the origin (x, y) as a Coordinate.
	 */
	public Coordinate getOrigin() {
		return new Coordinate(x, y);
	}

	/**
	 * Get the center of the bounding box as a Coordinate.
	 */
	public Coordinate getCenterPoint() {
		double centerX = (width == 0 ? x : x + width / 2);
		double centerY = (height == 0 ? y : y + height / 2);
		return new Coordinate(centerX, centerY);
	}

	/**
	 * Get the end-point of the bounding box as a Coordinate.
	 */
	public Coordinate getEndPoint() {
		return new Coordinate(x + width, y + height);
	}

	/**
	 * Does this bounding box contain the given bounding box?
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return true if the other is completely surrounded by this one, false otherwise.
	 */
	public boolean contains(Bbox other) {
		if (other.getX() < x) {
			return false;
		}
		if (other.getY() < y) {
			return false;
		}
		if (other.getMaxX() > getMaxX()) {
			return false;
		}
		if (other.getMaxY() > getMaxY()) {
			return false;
		}
		return true;
	}

	/**
	 * Does this bounding box intersect the given bounding box?
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return true if the other intersects this one, false otherwise.
	 */
	public boolean intersects(Bbox other) {
		if (other.getX() > getMaxX()) {
			return false;
		}
		if (other.getY() > getMaxY()) {
			return false;
		}
		if (other.getMaxX() < x) {
			return false;
		}
		if (other.getMaxY() < y) {
			return false;
		}
		return true;
	}

	/**
	 * Computes the intersection of this bounding box with the specified bounding box.
	 * 
	 * @param other
	 *            Another bounding box.
	 * @return bounding box of intersection or null if they do not intersect.
	 */
	public Bbox intersection(Bbox other) {
		if (!intersects(other)) {
			return null;
		} else {
			double minx = other.getX() > x ? other.getX() : x;
			double maxx = other.getMaxX() < getMaxX() ? other.getMaxX() : getMaxX();
			double miny = other.getY() > y ? other.getY() : y;
			double maxy = other.getMaxY() < getMaxY() ? other.getMaxY() : getMaxY();
			return new BboxImpl(minx, miny, (maxx - minx), (maxy - miny));
		}
	}

	/**
	 * Calculates the union of 2 bounding boxes.
	 * 
	 * @param other
	 *            The other bounding box. Can be a bounding box with width and height equal to 0.
	 */
	public Bbox union(Bbox other) {
		if (other.getWidth() == 0 && other.getHeight() == 0 && other.getX() == 0 && other.getY() == 0) {
			return (BboxImpl) clone();
		}
		if (width == 0 && height == 0 && x == 0 && y == 0) {
			return (Bbox) ((BboxImpl) other).clone();
		}

		double minx = other.getX() < x ? other.getX() : x;
		double maxx = other.getMaxX() > getMaxX() ? other.getMaxX() : getMaxX();
		double miny = other.getY() < y ? other.getY() : y;
		double maxy = other.getMaxY() > getMaxY() ? other.getMaxY() : getMaxY();
		return new BboxImpl(minx, miny, (maxx - minx), (maxy - miny));
	}

	/**
	 * Return a new bounding box that has increased in size by adding a range to this bounding box.
	 * 
	 * @param range
	 *            Must be a positive number, otherwise null will be returned.
	 * @return
	 */
	public Bbox buffer(double range) {
		if (range >= 0) {
			double r2 = range * 2;
			return new BboxImpl(x - range, y - range, width + r2, height + r2);
		}
		throw new IllegalArgumentException("Buffer range must always be positive.");
	}

	/**
	 * Return a new bounding box which has the same center position but has been scaled with the specified factor.
	 * 
	 * @param factor
	 *            The scale factor (must be > 0).
	 * @return
	 */
	public Bbox scale(double factor) {
		if (factor > 0) {
			double scaledWidth = width * factor;
			double scaledHeight = height * factor;
			Coordinate center = getCenterPoint();
			return new BboxImpl(center.getX() - scaledWidth / 2, center.getY() - scaledHeight / 2, scaledWidth,
					scaledHeight);
		}
		throw new IllegalArgumentException("Scale factor must always be strictly positive.");
	}

	/**
	 * Translates this bounds with displacement dx and dy.
	 * 
	 * @param dx
	 *            x displacement
	 * @param dy
	 *            y displacement
	 */
	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
	}

	/**
	 * Moves center to the specified coordinate.
	 * 
	 * @param coordinate
	 *            new center point
	 */
	public void setCenterPoint(Coordinate coordinate) {
		x = coordinate.getX() - 0.5 * width;
		y = coordinate.getY() - 0.5 * height;
	}

	/**
	 * Returns whether or not this bounding box is empty. A bounding box is considered empty when either the width or
	 * the height is equal to zero.
	 */
	public boolean isEmpty() {
		return width == 0 || height == 0;
	}

	/**
	 * Return a nice print of this bounding box.
	 */
	public String toString() {
		return "Bbox[" + x + " " + y + ", " + width + " " + height + "]";
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getMaxX() {
		return x + width;
	}

	public double getMaxY() {
		return y + height;
	}

	public boolean equals(Object other) {
		if (!(other instanceof Bbox)) {
			return false;
		}
		Bbox otherBbox = (Bbox) other;
		return x == otherBbox.getX() && y == otherBbox.getY() && width == otherBbox.getWidth()
				&& height == otherBbox.getHeight();
	}

	public int hashCode() {
		return new Double(x * y * width * height / 31).hashCode();
	}

	public boolean equals(Bbox other, double delta) {
		return equals(x, other.getX(), delta) && equals(y, other.getY(), delta)
				&& equals(width, other.getWidth(), delta) && equals(height, other.getHeight(), delta);
	}

	// ------------------------------------------------------------------------
	// Protected or private methods:
	// ------------------------------------------------------------------------

	protected boolean equals(double d1, double d2, double delta) {
		return Math.abs(d1 - d2) <= delta;
	}
}