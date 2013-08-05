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

/**
 * <p>
 * Style object used for images on the map.
 * </p>
 *
 * @author Pieter De Graef
 */
public class PictureStyle implements Style {

	/**
	 * Value between 0 and 1, where 0 is completely transparent, and 1 means no
	 * transparency at all. This transparency is used for the picture on which
	 * this style is applied.
	 */
	private double opacity;
	
	/**
	 * display property
	 */
	private String display;
	// Constructors:

	/**
	 * Initialize this style object immediately with it's fields.
	 *
	 * @param opacity
	 *            The transparency level for the pictures on which this style is
	 *            applied.
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

	/**
	 * Does nothing.
	 */
	public void scale(double scale) {
	}

	/**
	 * Return a clone of this style object.
	 */
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