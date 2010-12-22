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

/**
 * <p>
 * A very simple matrix class, that is actually nothing more then a POJO object.
 * </p>
 *
 * @author Pieter De Graef
 */
public class Matrix {

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
	 * @param xy
	 * @param yx
	 * @param yy
	 * @param dx
	 * @param dy
	 */
	public Matrix(double xx, double xy, double yx, double yy, double dx, double dy) {
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
}