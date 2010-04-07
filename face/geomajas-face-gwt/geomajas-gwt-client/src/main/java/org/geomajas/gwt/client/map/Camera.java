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

package org.geomajas.gwt.client.map;

import org.geomajas.geometry.Coordinate;

/**
 * <p>
 * This object determines the current position on the map. The position is expressed in world coordinates (the map's
 * CRS), but after transformation to viewspace, it is supposed to lie in the center of the map.
 * </p>
 *
 * @author Pieter De Graef
 */
public class Camera {

	/**
	 * The position of the Camera in world space. This position is expressed in the coordinate system of the map.
	 */
	private Coordinate position;

	/**
	 * Radial angle of the map.
	 */
	private double alpha;

	// Constructors:

	/**
	 * Default constructor that initializes all values to 0.
	 */
	public Camera() {
		position = new Coordinate(0, 0);
		alpha = 0;
	}

	// Transformation functions:

	/**
	 * Move the camera along 2 axis.
	 *
	 * @param x
	 *            Translation along the X-axis.
	 * @param y
	 *            Translation along the Y-axis.
	 */
	public void translate(double x, double y) {
		position.setX(position.getX() + x);
		position.setY(position.getY() + y);
	}

	/**
	 * Rotate the camera over the given angle.
	 *
	 * @param angle
	 *            Should be a double. (radial, not degrees)
	 */
	public void rotate(double angle) {
		this.alpha += angle;
	}

	// Other functions:

	/**
	 * A nice and decent toString function never hurts.
	 */
	public String toString() {
		return "Camera: x=" + position.getX() + ", y=" + position.getY() + ", a=" + this.alpha;
	}

	// Getters and setters:

	/**
	 * Return the current camera position in world coordinates.
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * Set a new position for the camera in world coordinates.
	 *
	 * @param position position
	 */
	public void setPosition(Coordinate position) {
		this.position = position;
	}

	/**
	 * Return the current radial angle.
	 *
	 * @return
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * Apply a new radial angle.
	 *
	 * @param alpha
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * Retrieve the coordinate along the X-axis.
	 *
	 * @return
	 */
	public double getX() {
		return position.getX();
	}

	/**
	 * Retrieve the coordinate along the Y-axis.
	 *
	 * @return
	 */
	public double getY() {
		return position.getY();
	}
}
