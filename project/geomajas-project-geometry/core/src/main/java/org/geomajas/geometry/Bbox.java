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
 * Basic axis aligned bounding box definition. This type of coordinate is meant as a Data Transfer Object (DTO) within
 * Java environments, and especially within GWT environments.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since GBE-1.6.0
 */
@Api(allMethods = true)
public class Bbox implements Serializable {

	private static final long serialVersionUID = 151L;

	/**
	 * the minimum X boundary of the bounding box.
	 */
	private double x;

	/**
	 * the minimum Y boundary of the bounding box.
	 */
	private double y;

	private double width;

	private double height;

	/** Huge bounding box, should cover coordinate space of all known CRSes. */
	public static final Bbox ALL = new Bbox(-1E20, -1E20, 2E20, 2E20);

	/**
	 * Create a zero-size bounding box.
	 */
	public Bbox() {
		this(0, 0, 0, 0);
	}

	/**
	 * Create bounding box.
	 * 
	 * @param x
	 *            minimum X origin
	 * @param y
	 *            minimum Y origin
	 * @param width
	 *            width of bounding box, should be positive
	 * @param height
	 *            height of bounding box, should be positive
	 */
	public Bbox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		setWidth(width);
		setHeight(height);
	}

	/**
	 * Return the height for the bounding box. This will always be a positive value.
	 * 
	 * @return height of the bounding box
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Set the height for the bounding box. The height should always be positive. When a negative height is set, the x
	 * origin is adjusted to compensate and the height made positive.
	 * 
	 * @param height
	 *            height for the bounding box
	 */
	public void setHeight(double height) {
		if (height < 0) {
			this.height = -height;
			y += height;
		} else {
			this.height = height;
		}
	}

	/**
	 * Return the width for the bounding box. This will always be a positive value.
	 * 
	 * @return width of the bounding box
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Set the width for the bounding box. The width should always be positive. When a negative width is set, the x
	 * origin is adjusted to compensate and the width made positive.
	 * 
	 * @param width
	 *            width for the bounding box
	 */
	public void setWidth(double width) {
		if (width < 0) {
			this.width = -width;
			x += width;
		} else {
			this.width = width;
		}
	}

	/**
	 * Get the minimum X boundary of the bounding box.
	 * 
	 * @return minimum X value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the minimum X boundary for the bounding box.
	 * 
	 * @param x
	 *            minimum X value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the minimum y boundary of the bounding box.
	 * 
	 * @return y minimum Y value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the minimum Y boundary for the bounding box.
	 * 
	 * @param y
	 *            minimum Y value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Get the highest X boundary (X + width) of the bounding box.
	 * 
	 * @return highest X value
	 */
	public double getMaxX() {
		return getX() + getWidth();
	}

	/**
	 * Set the highest X boundary for the bounding box.<br/>
	 * Attention, order is important, setMaxX() must always be called after setX().
	 * 
	 * @param x
	 *            highest X value
	 * @since GBE-1.8.0
	 */
	public void setMaxX(double x) {
		setWidth(x - this.x);
	}

	/**
	 * Get the highest Y boundary (Y + height) of the bounding box.
	 * 
	 * @return highest y value
	 */
	public double getMaxY() {
		return getY() + getHeight();
	}

	/**
	 * Set the highest Y boundary for the bounding box.<br/>
	 * Attention, order is important, setMaxY() must always be called after setY().
	 * 
	 * @param y
	 *            highest Y value.
	 * @since GBE-1.8.0
	 */
	public void setMaxY(double y) {
		setHeight(y - this.y);
	}

	/**
	 * Convert to readable string.
	 * 
	 * @return readable string for Bounding box.
	 */
	public String toString() {
		return "Bbox[" + x + " " + y + " " + width + " " + height + "]";
	}
}