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
package org.geomajas.configuration;

import java.io.Serializable;

import org.geomajas.global.Api;

/**
 * Information about how to access and how to render the label attribute.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FontStyleInfo implements Serializable {

	private static final long serialVersionUID = 160L;

	private int size = -1;

	private String family;

	private String weight;

	private String style;

	private String color;

	private float opacity = -1;

	/**
	 * Get the font size.
	 *
	 * @return font size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Set the font size.
	 * 
	 * @param size
	 *            The new font size.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Get the font family.
	 *
	 * @return font family
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * Set the font family ("Verdana", "Arial", ...).
	 * 
	 * @param family
	 *            The new font family.
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * Get the font weight.
	 *
	 * @return font weight ("normal", "bold")
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * Set the font weight ("normal", "bold").
	 * 
	 * @param weight
	 *            The new font weight.
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * Get the font style.
	 *
	 * @return font style ("normal", "italic", ...)
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Set the font style ("normal", "italic", ...).
	 * 
	 * @param style
	 *            The new font style.
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Get the font color.
	 *
	 * @return font color (as HTML color)
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Set the font color (as HTML color).
	 * 
	 * @param color
	 *            The new color.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Get the font opacity.
	 *
	 * @return font opacity (between 0 and 1)
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Set the font opacity (between 0 and 1).
	 * 
	 * @param opacity
	 *            The new opacity.
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
}
