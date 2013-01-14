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
package org.geomajas.geometry;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * Definition of a mutable coordinate class. This type of coordinate is meant as a Data Transfer Object (DTO) within
 * Java environments, and especially within GWT environments.
 * 
 * @author Pieter De Graef
 * @since GBE-1.6.0
 */
@Api(allMethods = true)
public class Coordinate implements Comparable<Coordinate>, Cloneable, Serializable {

	private static final long serialVersionUID = 100L;

	private static final double EQUALS_DELTA = 1e-12;

	private static final int HASH_BASE = 17;

	private static final int HASH_FACTOR = 37;

	/** The x-coordinate. */
	private double x;

	/** The y-coordinate. */
	private double y;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Constructs a <code>Coordinate</code> at (x,y).
	 * 
	 * @param x
	 *            the x-value
	 * @param y
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
	 * @param c
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
	 * @param other
	 *            The <code>Coordinate</code> to copy
	 */
	public void setCoordinate(Coordinate other) {
		x = other.x;
		y = other.y;
	}

	/**
	 * Return <code>true</code> if <code>other</code> has the same values for the x and y ordinates.
	 * 
	 * @param other
	 *            A <code>Coordinate</code> with which to do the comparison.
	 * @return <code>true</code> if <code>other</code> is a <code>Coordinate</code> with the same values for the x and y
	 *         ordinates.
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Coordinate)) {
			return false;
		}
		Coordinate c = (Coordinate) other;
		return (Math.abs(x - c.x) < EQUALS_DELTA) && (Math.abs(y - c.y) < EQUALS_DELTA);
	}

	/**
	 * Comparison using a tolerance for the equality check.
	 * 
	 * @param coordinate
	 *            coordinate to compare with
	 * @param delta
	 *            maximum deviation (along one axis, the actual maximum distance is sqrt(2*delta^2))
	 * @return true
	 */
	public boolean equalsDelta(Coordinate coordinate, double delta) {
		return null != coordinate &&
				(Math.abs(this.x - coordinate.x) < delta && Math.abs(this.y - coordinate.y) < delta);
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
	 * @param other
	 *            the <code>Coordinate</code> with which this <code>Coordinate</code> is being compared. Can not be
	 *            null.
	 * @return -1, zero, or 1 as this <code>Coordinate</code> is less than, equal to, or greater than the specified
	 *         <code>Coordinate</code>
	 */
	public int compareTo(Coordinate other) {
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
	 * @return a <code>String</code> of the form <I>(x,y)</I>
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Clone the coordinate.
	 * 
	 * @return cloned coordinate
	 */
	public Object clone() { // NOSONAR super.clone() not supported by GWT
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
		// Algorithm from Effective Java by tJoshua Bloch [Jon Aquino]
		int result = HASH_BASE;
		result = HASH_FACTOR * result + hashCode(x);
		result = HASH_FACTOR * result + hashCode(y);
		return result;
	}

	/**
	 * Computes a hash code for a double value, using the algorithm from Joshua Bloch's book <i>Effective Java"</i>.
	 * 
	 * @param d
	 *            value to calculate hash for
	 * @return a hashcode for the double value
	 */
	private int hashCode(double d) {
		return ((Double) d).hashCode();
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get x component of the coordinate.
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the x component for the coordinate.
	 * 
	 * @param x
	 *            x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the y component of the coordinate.
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the y component of the coordinate.
	 * 
	 * @param y
	 *            y
	 */
	public void setY(double y) {
		this.y = y;
	}
}