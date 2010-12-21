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

/**
 * <p>
 * Style object used for images on the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class PictureStyle implements Style {

	/**
	 * Value between 0 and 1, where 0 is completely transparent, and 1 means no transparency at all. This transparency
	 * is used for the picture on which this style is applied.
	 */
	private double opacity;

	/** Display property. */
	private String display;

	// Constructors:

	/**
	 * Initialize this style object immediately with it's fields.
	 * 
	 * @param opacity
	 *            The transparency level for the pictures on which this style is applied.
	 */
	public PictureStyle(double opacity) {
		this.opacity = opacity;
	}

	/**
	 * Initialize this style object with the values of another.
	 * 
	 * @param other
	 *            The other style object from who to copy the field values.
	 */
	public PictureStyle(PictureStyle other) {
		this.opacity = other.opacity;
		this.display = other.display;
	}

	// Other functions:

	/** Does nothing. */
	public void scale(double scale) {
	}

	/** Return a clone of this style object. */
	public Style clone() {
		return new PictureStyle(this);
	}

	// Getters and setters:

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}