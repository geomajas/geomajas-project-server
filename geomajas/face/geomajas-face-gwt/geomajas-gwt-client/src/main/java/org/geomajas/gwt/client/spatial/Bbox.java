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

package org.geomajas.gwt.client.spatial;

import org.geomajas.geometry.Coordinate;

/**
 * <p>
 * Definition of an Axis Aligned Bounding Box. Determined by an x-ordinate an y-ordinate, it's width and it's height.
 * Notice the the name says "Axis Aligned"! This implies that this type of bounding box cannot rotate!
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Bbox {

	/**
	 * The ordinate along the X-axis.
	 */
	private double x;

	/**
	 * The ordinate along the Y-axis.
	 */
	private double y;

	/**
	 * The bounding box' width.
	 */
	private double width;

	/**
	 * The bounding box' height.
	 */
	private double height;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor setting the position to (0, 0), and a width and height of 1.
	 */
	public Bbox() {
		this(0, 0, 1, 1);
	}

	/**
	 * constructor that immediately applies values to all the fields.
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
	public Bbox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct a bbox by copying another one.
	 * 
	 * @param bounds
	 *            Another bounding box instance.
	 */
	public Bbox(Bbox bounds) {
		x = bounds.getX();
		y = bounds.getY();
		width = bounds.getWidth();
		height = bounds.getHeight();
	}

	/**
	 * Constructor that transforms the DTO bounding box to the GWT bounding box.
	 * 
	 * @param bounds
	 *            A DTO bounding box instance.
	 */
	public Bbox(org.geomajas.geometry.Bbox bounds) {
		x = bounds.getX();
		y = bounds.getY();
		width = bounds.getWidth();
		height = bounds.getHeight();
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Create a clone of the object.
	 */
	public Object clone() {
		return new Bbox(x, y, width, height);
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
		return new Coordinate(x + width / 2, y + height / 2);
	}

	/**
	 * Get the endpoint of the bounding box as a Coordinate.
	 */
	public Coordinate getEndPoint() {
		return new Coordinate(x + width, y + height);
	}

	/**
	 * Get the coordinates of the bounding box as an array.
	 * 
	 * @return Returns 5 coordinates so that the array is closed. This can be useful when using this array to creating a
	 *         <code>LinearRing</code>.
	 */
	public Coordinate[] getCoordinates() {
		Coordinate[] result = new Coordinate[5];

		result[0] = new Coordinate(x, y);
		result[1] = new Coordinate(x + width, y);
		result[2] = new Coordinate(x + width, y + height);
		result[3] = new Coordinate(x, y + height);
		result[4] = new Coordinate(x, y);
		return result;
	}

	/**
	 * Does this bounding box contain the given bounding box?
	 * 
	 * @param other
	 *            Another Bbox.
	 * @return true if the other is completely surrounded by this one, false otherwise.
	 */
	public boolean contains(Bbox other) {
		if (other.getX() < this.getX()) {
			return false;
		}
		if (other.getY() < this.getY()) {
			return false;
		}
		if (other.getEndPoint().getX() > this.getEndPoint().getX()) {
			return false;
		}
		if (other.getEndPoint().getY() > this.getEndPoint().getY()) {
			return false;
		}
		return true;
	}

	/**
	 * Does this bounding box intersect the given bounding box?
	 * 
	 * @param other
	 *            Another Bbox.
	 * @return true if the other intersects this one, false otherwise.
	 */
	public boolean intersects(Bbox other) {
		if (other.getX() > this.getEndPoint().getY()) {
			return false;
		}
		if (other.getY() > this.getEndPoint().getY()) {
			return false;
		}
		if (other.getEndPoint().getX() < this.getX()) {
			return false;
		}
		if (other.getEndPoint().getY() < this.getY()) {
			return false;
		}
		return true;
	}

	/**
	 * Computes the intersection of this bounding box with the specified bounding box.
	 * 
	 * @param other
	 *            Another Bbox.
	 * @return bounding box of intersection or null if they do not intersect.
	 */
	public Bbox intersection(Bbox other) {
		if (!this.intersects(other)) {
			return null;
		} else {
			double minx = other.getX() > this.getX() ? other.getX() : this.getX();
			double maxx = other.getEndPoint().getX() < this.getEndPoint().getX() ? other.getEndPoint().getX() : this
					.getEndPoint().getX();
			double miny = other.getY() > this.getY() ? other.getY() : this.getY();
			double maxy = other.getEndPoint().getY() < this.getEndPoint().getY() ? other.getEndPoint().getY() : this
					.getEndPoint().getY();
			return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
		}
	}

	/**
	 * Calculates the union of 2 bounding boxes.
	 * 
	 * @param other
	 *            The other Bbox. Can be a bounding box with width and height equal to 0.
	 */
	public Bbox union(Bbox other) {
		if (other.getWidth() == 0 || other.getHeight() == 0) {
			return (Bbox) this.clone();
		}
		if (this.width == 0 || this.height == 0) {
			return (Bbox) other.clone();
		}
		double minx = other.getX() < this.getX() ? other.getX() : this.getX();
		double maxx = other.getEndPoint().getX() > this.getEndPoint().getX() ? other.getEndPoint().getX() : this
				.getEndPoint().getX();
		double miny = other.getY() < this.getY() ? other.getY() : this.getY();
		double maxy = other.getEndPoint().getY() > this.getEndPoint().getY() ? other.getEndPoint().getY() : this
				.getEndPoint().getY();
		return new Bbox(minx, miny, (maxx - minx), (maxy - miny));
	}

	/**
	 * Return a new bounding box that has increased in size by adding a range to this bounding box.
	 * 
	 * @param range
	 *            Must be a positive number, otherwise null will be returned.
	 * @return
	 */
	public Bbox buffer(double range) {
		if (range > 0) {
			double r2 = range * 2;
			return new Bbox(x - range, y - range, width + r2, height + r2);
		}
		return null;
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
		this.x = this.x + dx;
		this.y = this.y + dy;
	}

	/**
	 * Moves center to the specified coordinate.
	 * 
	 * @param coordinate
	 *            new center point
	 */
	public void setCenterPoint(Coordinate coordinate) {
		this.x = coordinate.getX() - 0.5 * this.width;
		this.y = coordinate.getY() - 0.5 * this.height;
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

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getMaxX() {
		return getX() + getWidth();
	}

	public double getMaxY() {
		return getY() + getHeight();
	}

	public boolean equals(Bbox other, double delta) {
		return equals(this.x, other.x, delta) && equals(this.y, other.y, delta)
				&& equals(this.width, other.width, delta) && equals(this.height, other.height, delta);
	}

	protected boolean equals(double d1, double d2, double delta) {
		return Math.abs(d1 - d2) <= delta;
	}
}