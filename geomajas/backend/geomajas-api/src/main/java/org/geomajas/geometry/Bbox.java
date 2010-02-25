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

import org.geomajas.global.Json;

import java.io.Serializable;

/**
 * DTO bounding box definition.
 * 
 * @author Joachim Van der Auwera
 */
public class Bbox implements Serializable {

	private static final long serialVersionUID = 151L;

	private double x;

	private double y;

	private double width;

	private double height;

	// huge bbox, should cover coordinate space of all known crs-es
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
	 * @param x x origin
	 * @param y y origin
	 * @param width width of bounding box, should be positive
	 * @param height height of bounding box, should be positive
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
	 * @return height of the bbox
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
	 * @return width of the bbox
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
	 * Get the lowest x boundary of the bbox.
	 *
	 * @return lowest x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the lowest x boundary for the bbox.
	 *
	 * @param x lowest x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the lowest y boundary of the bbox.
	 *
	 * @return lowest y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the lowest y boundary for the bbox.
	 *
	 * @param y lowest y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Get the highest x boundary of the bbox.
	 *
	 * @return highest x
	 */
	@Json(serialize = false)
	public double getMaxX() {
		return getX() + getWidth();
	}

	/**
	 * Get the highest y boundary of the bbox.
	 *
	 * @return highest y
	 */
	@Json(serialize = false)
	public double getMaxY() {
		return getY() + getHeight();
	}

	/**
	 * Convert to readable string.
	 *
	 * @return readable string for bbox
	 */
	public String toString() {
		return "Bbox[" + x + " " + y + " " + width + " " + height + "]";
	}
}
