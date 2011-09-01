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

/**
 * <p>
 * A very simple matrix class, that is actually nothing more then a POJO object.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MatrixImpl implements Matrix {

	private double xx;

	private double xy;

	private double yx;

	private double yy;

	private double dx;

	private double dy;

	// Constructors:

	/**
	 * The default constructor initializes everything to 0.
	 */
	public MatrixImpl() {
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
	 * @param xy
	 * @param yx
	 * @param yy
	 * @param dx
	 * @param dy
	 */
	public MatrixImpl(double xx, double xy, double yx, double yy, double dx, double dy) {
		this.xx = xx;
		this.xy = xy;
		this.yx = yx;
		this.yy = yy;
		this.dx = dx;
		this.dy = dy;
	}

	// Public methods:

	public String toString() {
		return "[" + xx + ", " + xy + ", " + yx + ", " + yy + ", " + dx + ", " + dy + "]";
	}

	// Getters:

	public double getXx() {
		return xx;
	}

	public double getXy() {
		return xy;
	}

	public double getYx() {
		return yx;
	}

	public double getYy() {
		return yy;
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
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
		MatrixImpl other = (MatrixImpl) obj;
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

}