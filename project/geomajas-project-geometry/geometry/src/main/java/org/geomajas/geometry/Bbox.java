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

package org.geomajas.geometry;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.global.Json;

/**
 * DTO bounding box definition.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class Bbox implements Serializable {

	private static final long serialVersionUID = 151L;
	/**
	 * the min x boundary of the bbox.
	 */
	private double x;
	/**
	 * the min y boundary of the bbox.
	 */
	private double y;

	private double width;

	private double height;

	/** Huge bbox, should cover coordinate space of all known CRSes. */
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
	 * @param x min x origin
	 * @param y min y origin
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
	 * Get the min x boundary of the bbox.
	 *
	 * @return min x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the min x boundary for the bbox.
	 *
	 * @param x min x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the min y boundary of the bbox.
	 *
	 * @return y min y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the min y boundary for the bbox.
	 *
	 * @param y min y
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
	 * Set the highest x boundary for the bbox.
	 * Attention, order is important, setMaxY() must always be called after setY().
	 * 
	 * @param x highest x
	 * @since 1.8.0
	 */
	public void setMaxX(double x) {
		setWidth(x - this.x);
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
	 * Set the highest y boundary for the bbox. 
	 * Attention, order is important, setMaxY() must always be called after setY().
	 *
	 * @param y highest y
	 * @since 1.8.0
	 */
	public void setMaxY(double y) {
		setHeight(y - this.y);
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
