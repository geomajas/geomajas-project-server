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

package org.geomajas.smartgwt.client.gfx.style;

import org.geomajas.configuration.FeatureStyleInfo;

import java.io.Serializable;

/**
 * <p>
 * Style object for shapes and geometries on the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ShapeStyle implements Style, Serializable {

	private static final long serialVersionUID = 150L;

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
	 *
	 * @param fillColor fill color
	 * @param fillOpacity fill opacity
	 * @param strokeColor stroke color
	 * @param strokeOpacity stroke opacity
	 * @param strokeWidth stroke width
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
	 * @param other
	 *            The other style object from which to copy the field values.
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
	 * @param info
	 *            {@link FeatureStyleInfo} object to read the style from
	 */
	public ShapeStyle(FeatureStyleInfo info) {
		if (null != info) {
			this.fillColor = info.getFillColor();
			this.fillOpacity = info.getFillOpacity();
			this.strokeColor = info.getStrokeColor();
			this.strokeOpacity = info.getStrokeOpacity();
			this.strokeWidth = info.getStrokeWidth();
		}
	}

	// Other functions:

	/**
	 * Scales only the strokewidth.
	 */
	public void scale(double scale) {
		strokeWidth = (int) Math.ceil(strokeWidth * scale);
	}

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
	public Style clone() { // NOSONAR super.clone() not supported by GWT
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