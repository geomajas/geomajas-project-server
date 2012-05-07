/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.jsapi.client.spatial;

import org.geomajas.annotation.Api;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable implementation of a Bounding Box.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("Bbox")
@ExportPackage("org.geomajas.jsapi.spatial")
@Api(allMethods = true)
public class Bbox implements ExportOverlay<org.geomajas.geometry.Bbox>, Exportable {

	/**
	 * Bbox constructor.
	 *
	 * @param x x origin
	 * @param y y origin
	 * @param width width
	 * @param height height
	 * @return bbox with requested origin and dimensions
	 */
	@ExportConstructor
	public static org.geomajas.geometry.Bbox constructor(double x, double y, double width, double height) {
		return new org.geomajas.geometry.Bbox(x, y, width, height);
	}

	/**
	 * Return the height for the bounding box. This will always be a positive value.
	 * 
	 * @return height of the bbox
	 */
	public double getHeight() {
		return 0;
	}

	/**
	 * Set the height for the bounding box. The height should always be positive. When a negative height is set, the x
	 * origin is adjusted to compensate and the height made positive.
	 * 
	 * @param height
	 *            height for the bounding box
	 */
	public void setHeight(double height) {
	}

	/**
	 * Return the width for the bounding box. This will always be a positive value.
	 * 
	 * @return width of the bbox
	 */
	public double getWidth() {
		return 0;
	}

	/**
	 * Set the width for the bounding box. The width should always be positive. When a negative width is set, the x
	 * origin is adjusted to compensate and the width made positive.
	 * 
	 * @param width
	 *            width for the bounding box
	 */
	public void setWidth(double width) {
	}

	/**
	 * Get the lowest x boundary of the bbox.
	 * 
	 * @return lowest x
	 */
	public double getX() {
		return 0;
	}

	/**
	 * Set the lowest x boundary for the bbox.
	 * 
	 * @param x
	 *            lowest x
	 */
	public void setX(double x) {
	}

	/**
	 * Get the lowest y boundary of the bbox.
	 * 
	 * @return lowest y
	 */
	public double getY() {
		return 0;
	}

	/**
	 * Set the lowest y boundary for the bbox.
	 * 
	 * @param y
	 *            lowest y
	 */
	public void setY(double y) {
	}

	/**
	 * Get the highest x boundary of the bbox.
	 * 
	 * @return highest x
	 */
	public double getMaxX() {
		return 0;
	}

	/**
	 * Set the highest x boundary for the bbox. Attention, order is important, setMaxY() must always be called after
	 * setY().
	 * 
	 * @param x
	 *            highest x
	 * @since 1.8.0
	 */
	public void setMaxX(double x) {
	}

	/**
	 * Get the highest y boundary of the bbox.
	 * 
	 * @return highest y
	 */
	public double getMaxY() {
		return 0;
	}

	/**
	 * Set the highest y boundary for the bbox. Attention, order is important, setMaxY() must always be called after
	 * setY().
	 * 
	 * @param y
	 *            highest y
	 * @since 1.8.0
	 */
	public void setMaxY(double y) {
	}

	/**
	 * Convert to readable string.
	 *
	 * @return readable string for bbox
	 */
	public String toString() {
		return "";
	}
}