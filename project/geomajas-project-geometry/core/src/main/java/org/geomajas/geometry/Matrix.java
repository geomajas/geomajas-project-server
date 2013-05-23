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

import org.geomajas.annotation.Api;

/**
 * <p>
 * Immutable and 3-dimensional matrix class.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class Matrix {

	/** The identity matrix. */
	public static final Matrix IDENTITY = new Matrix(1, 0, 0, 1, 0, 0);

	private final double xx;

	private final double xy;

	private final double yx;

	private final double yy;

	private final double dx;

	private final double dy;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** The default constructor initializes everything to 0. */
	public Matrix() {
		this.xx = 0;
		this.xy = 0;
		this.yx = 0;
		this.yy = 0;
		this.dx = 0;
		this.dy = 0;
	}

	/**
	 * Initialize this matrix with all it's fields.
	 * 
	 * @param xx
	 *            xx-value.
	 * @param xy
	 *            xy-value.
	 * @param yx
	 *            yx-value.
	 * @param yy
	 *            yy-value.
	 * @param dx
	 *            delta value for the X-axis.
	 * @param dy
	 *            delta value for the Y-axis.
	 */
	public Matrix(double xx, double xy, double yx, double yy, double dx, double dy) {
		this.xx = xx;
		this.xy = xy;
		this.yx = yx;
		this.yy = yy;
		this.dx = dx;
		this.dy = dy;
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return "[" + xx + ", " + xy + ", " + yx + ", " + yy + ", " + dx + ", " + dy + "]";
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Matrix other = (Matrix) obj;
		if (Double.compare(dx, other.dx) != 0) {
			return false;
		}
		if (Double.compare(dy, other.dy) != 0) {
			return false;
		}
		if (Double.compare(xx, other.xx) != 0) {
			return false;
		}
		if (Double.compare(xy, other.xy) != 0) {
			return false;
		}
		if (Double.compare(yx, other.yx) != 0) {
			return false;
		}
		if (Double.compare(yy, other.yy) != 0) {
			return false;
		}
		return true;
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	/**
	 * Get the XX value.
	 * 
	 * @return The XX value.
	 */
	public double getXx() {
		return xx;
	}

	/**
	 * Get the XY value.
	 * 
	 * @return The XY value.
	 */
	public double getXy() {
		return xy;
	}

	/**
	 * Get the YX value.
	 * 
	 * @return The YX value.
	 */
	public double getYx() {
		return yx;
	}

	/**
	 * Get the YY value.
	 * 
	 * @return The YY value.
	 */
	public double getYy() {
		return yy;
	}

	/**
	 * Get the DX value.
	 * 
	 * @return The DX value.
	 */
	public double getDx() {
		return dx;
	}

	/**
	 * Get the DY value.
	 * 
	 * @return The DY value.
	 */
	public double getDy() {
		return dy;
	}
}