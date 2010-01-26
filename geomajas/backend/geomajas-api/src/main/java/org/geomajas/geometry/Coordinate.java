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
package org.geomajas.geometry;

import java.io.Serializable;
import java.util.Comparator;

/**
 * General coordinate class. It is based upon the Coordinate class from JTS4GWT (LGPL).
 *
 * @author Pieter De Graef
 */
public class Coordinate implements Comparable<Coordinate>, Cloneable, Serializable {

	private static final long serialVersionUID = 6683108902428366910L;

	/**
	 * The x-coordinate.
	 */
	private double x;

	/**
	 * The y-coordinate.
	 */
	private double y;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Constructs a <code>Coordinate</code> at (x,y).
	 *
	 *@param x
	 *            the x-value
	 *@param y
	 *            the y-value
	 */
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a <code>Coordinate</code> at (0,0,NaN).
	 */
	public Coordinate() {
		this(0.0, 0.0);
	}

	/**
	 * Constructs a <code>Coordinate</code> having the same (x,y,z) values as <code>other</code>.
	 *
	 *@param c
	 *            the <code>Coordinate</code> to copy.
	 */
	public Coordinate(Coordinate c) {
		this(c.x, c.y);
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Set this <code>Coordinate</code>s (x,y) values to that of <code>other</code>.
	 *
	 *@param other
	 *            The <code>Coordinate</code> to copy
	 */
	public void setCoordinate(Coordinate other) {
		x = other.x;
		y = other.y;
	}

	/**
	 * Return <code>true</code> if <code>other</code> has the same values for the x and y ordinates.
	 *
	 *@param other
	 *            A <code>Coordinate</code> with which to do the comparison.
	 *@return <code>true</code> if <code>other</code> is a <code>Coordinate</code> with the same values for the x and y
	 *         ordinates.
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Coordinate)) {
			return false;
		}
		Coordinate c = (Coordinate) other;
		if (x != c.x) {
			return false;
		}

		if (y != c.y) {
			return false;
		}

		return true;
	}

	public boolean equalsDelta(Coordinate coordinate, double delta) {
		return (Math.abs(this.x - coordinate.x) < delta && Math.abs(this.y - coordinate.y) < delta);
	}

	/**
	 * Compares this {@link Coordinate} with the specified {@link Coordinate} for order. Returns:
	 * <UL>
	 * <LI>-1 : this.x < other.x || ((this.x == other.x) && (this.y < other.y))
	 * <LI>0 : this.x == other.x && this.y = other.y
	 * <LI>1 : this.x > other.x || ((this.x == other.x) && (this.y > other.y))
	 *
	 * </UL>
	 * Note: This method assumes that ordinate values are valid numbers. NaN values are not handled correctly.
	 *
	 *@param o
	 *            the <code>Coordinate</code> with which this <code>Coordinate</code> is being compared
	 *@return -1, zero, or 1 as this <code>Coordinate</code> is less than, equal to, or greater than the specified
	 *         <code>Coordinate</code>
	 */
	public int compareTo(Coordinate o) {
		Coordinate other = (Coordinate) o;

		if (x < other.x) {
			return -1;
		}
		if (x > other.x) {
			return 1;
		}
		if (y < other.y) {
			return -1;
		}
		if (y > other.y) {
			return 1;
		}
		return 0;
	}

	/**
	 * Return a <code>String</code> of the form <I>(x,y)</I> .
	 *
	 *@return a <code>String</code> of the form <I>(x,y)</I>
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public Object clone() {
		return new Coordinate(x, y);
	}

	/**
	 * Computes the 2-dimensional Euclidean distance to another location.
	 *
	 * @param c
	 *            Another coordinate
	 * @return the 2-dimensional Euclidean distance between the locations
	 */
	public double distance(Coordinate c) {
		double dx = x - c.x;
		double dy = y - c.y;

		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Get a hashcode for this coordinate.
	 *
	 * @return a hashcode for this coordinate
	 */
	public int hashCode() {
		// Algorithm from Effective Java by Joshua Bloch [Jon Aquino]
		int result = 17;
		result = 37 * result + hashCode(x);
		result = 37 * result + hashCode(y);
		return result;
	}

	/**
	 * Computes a hash code for a double value, using the algorithm from Joshua Bloch's book <i>Effective Java"</i>.
	 *
	 * @return a hashcode for the double value
	 */
	public static int hashCode(double x) {
		// long f = Double.doubleToRawLongBits(x);
		long f = (long) x;
		return (int) (f ^ (f >>> 32));
	}

	/**
	 * Compares two {@link Coordinate}s, allowing for either a 2-dimensional or 3-dimensional comparison, and handling
	 * NaN values correctly.
	 */
	@SuppressWarnings("unchecked")
	public static class CoordinateComparator implements Comparator {

		/**
		 * Compare two <code>double</code>s, allowing for NaN values. NaN is treated as being less than any valid
		 * number.
		 *
		 * @param a
		 *            a <code>double</code>
		 * @param b
		 *            a <code>double</code>
		 * @return -1, 0, or 1 depending on whether a is less than, equal to or greater than b
		 */
		public static int compare(double a, double b) {
			if (a < b) {
				return -1;
			}
			if (a > b) {
				return 1;
			}

			if (Double.isNaN(a)) {
				if (Double.isNaN(b)) {
					return 0;
				}
				return -1;
			}

			if (Double.isNaN(b)) {
				return 1;
			}
			return 0;
		}

		/**
		 * Creates a comparator for 2 dimensional coordinates.
		 */
		public CoordinateComparator() {
		}

		/**
		 * Compares two {@link Coordinate}s along to the number of dimensions specified.
		 *
		 * @param o1
		 *            a {@link Coordinate}
		 * @param o2
		 *            a {link Coordinate}
		 * @return -1, 0, or 1 depending on whether o1 is less than, equal to, or greater than 02
		 *
		 */
		public int compare(Object o1, Object o2) {
			Coordinate c1 = (Coordinate) o1;
			Coordinate c2 = (Coordinate) o2;

			int compX = compare(c1.x, c2.x);
			if (compX != 0) {
				return compX;
			}

			int compY = compare(c1.y, c2.y);
			return compY;
		}
	}

	// Getters and setters:

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
}