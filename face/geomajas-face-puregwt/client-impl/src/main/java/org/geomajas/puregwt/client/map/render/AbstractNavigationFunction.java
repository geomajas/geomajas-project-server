/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.render;

/**
 * Abstract definition of a mathematical function that determines the route from one location to another.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractNavigationFunction {

	protected double beginX;

	protected double beginY;

	protected double beginZ;

	protected double endX;

	protected double endY;

	protected double endZ;

	/**
	 * Set the start location.
	 * 
	 * @param x
	 *            The value along the X-axis.
	 * @param y
	 *            The value along the Y-axis.
	 * @param z
	 *            The scale value.
	 */
	public void setBeginLocation(double x, double y, double z) {
		this.beginX = x;
		this.beginY = y;
		this.beginZ = z;
	}

	/**
	 * Set the target location.
	 * 
	 * @param x
	 *            The value along the X-axis.
	 * @param y
	 *            The value along the Y-axis.
	 * @param z
	 *            The scale value.
	 */
	public void setEndLocation(double x, double y, double z) {
		this.endX = x;
		this.endY = y;
		this.endZ = z;
	}

	/**
	 * Get an intermediate location in between the begin and end locations, determined by the progress parameter.
	 * 
	 * @param progress
	 *            Value between 0 and 1. If progress equals 0, this method must return the begin location. If progress
	 *            equals 1, this method must return the end location. All other values must return a location that on
	 *            route between begin and end.
	 * @return Returns the location coupled to the progress that's made.
	 */
	public abstract double[] getLocation(double progress);
}