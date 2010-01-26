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

package org.geomajas.gwt.client.gfx.style;

import org.geomajas.configuration.StyleInfo;

/**
 * <p>
 * Style object for shapes and geometries on the map.
 * </p>
 *
 * @author Pieter De Graef
 */
public class ShapeStyle implements Style {

	/**
	 * The color to be used as background color. Must be an HTML color code (red = #FF0000).
	 */
	private String fillColor;

	/**
	 * Value between 0 and 1, where 0 is completely transparent, and 1 means no transparency at all. This transparency
	 * is used for the background of the object on which it is applied.
	 */
	private float fillOpacity = -1;

	/**
	 * The color to be used as color for the edges. Must be an HTML color code (red = #FF0000).
	 */
	private String strokeColor;

	/**
	 * Value between 0 and 1, where 0 is completely transparent, and 1 means no transparency at all. This transparency
	 * is used for the edges/lines of the object on which it is applied.
	 */
	private float strokeOpacity = -1;

	/**
	 * The width in pixels of the edges of the object on which this style is to be applied.
	 */
	private float strokeWidth = -1;

	// Constructors:

	/**
	 * The default constructor. Leaves every field as is.
	 */
	public ShapeStyle() {
		// do nothing
	}

	/**
	 * Initialize this style object with all it's fields.
	 */
	public ShapeStyle(String fillColor, float fillOpacity, String strokeColor, float strokeOpacity, int strokeWidth) {
		this.fillColor = fillColor;
		this.fillOpacity = fillOpacity;
		this.strokeColor = strokeColor;
		this.strokeOpacity = strokeOpacity;
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Initialize this style object with the values of another style object.
	 *
	 * @param other The other style object from who to copy the field values.
	 */
	public ShapeStyle(ShapeStyle other) {
		this.fillColor = other.fillColor;
		this.fillOpacity = other.fillOpacity;
		this.strokeColor = other.strokeColor;
		this.strokeOpacity = other.strokeOpacity;
		this.strokeWidth = other.strokeWidth;
	}

	/**
	 * Initialize this style object with the values of a style configuration object.
	 *
	 * @param info {@link StyleInfo} object to read the style from
	 */
	public ShapeStyle(StyleInfo info) {
		if (null != info) {
			this.fillColor = info.getFillColor();
			this.fillOpacity = info.getFillOpacity();
			this.strokeColor = info.getStrokeColor();
			this.strokeOpacity = info.getStrokeOpacity();
			this.strokeWidth = info.getStrokeWidth();
		}
	}

	// Other functions:

	public void merge(ShapeStyle style) {
		if (style.getFillColor() != null) {
			fillColor = style.getFillColor();
		}
		setFillOpacity(style.getFillOpacity());
		if (style.getStrokeColor() != null) {
			strokeColor = style.getStrokeColor();
		}
		setStrokeOpacity(style.getStrokeOpacity());
		setStrokeWidth(style.getStrokeWidth());
	}

	/**
	 * Return a clone of this style object.
	 */
	public Object clone() {
		return new ShapeStyle(this);
	}

	// Getters and setters:

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public float getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(float fillOpacity) {
		if (fillOpacity >= 0 && fillOpacity <= 1) {
			this.fillOpacity = fillOpacity;
		}
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public float getStrokeOpacity() {
		return strokeOpacity;
	}

	public void setStrokeOpacity(float strokeOpacity) {
		if (strokeOpacity >= 0 && strokeOpacity <= 1) {
			this.strokeOpacity = strokeOpacity;
		}
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		if (strokeWidth >= 0) {
			this.strokeWidth = strokeWidth;
		}
	}
}