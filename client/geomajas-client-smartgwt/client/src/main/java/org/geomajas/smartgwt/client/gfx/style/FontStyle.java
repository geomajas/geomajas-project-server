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

import org.geomajas.configuration.FontStyleInfo;

/**
 * <p>
 * Style object used for fonts on the map.
 * </p>
 * <p>
 * TODO: should be an extension of ShapeStyle, because it needs a fill, fill-opacity, stroke, stroke-opacity,....
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FontStyle implements Style {

	/**
	 * The color to be used as background color. Must be an HTML color code (red = #FF0000).
	 */
	private String fillColor;

	/**
	 * Integer size of the fonts drawn with this style.
	 */
	private int fontSize = 1;

	/**
	 * Type of font. i.e. "Arial", "Verdana", ...
	 */
	private String fontFamily;

	/**
	 * Either "normal" or "bold".
	 */
	private String fontWeight;

	/**
	 * The style of the font: "normal", "italic", or "oblique".
	 */
	private String fontStyle;

	/**
	 * Style object for graphics fonts. Initialize it immediately with all it's fields.
	 * 
	 * @param fillColor
	 *            HTML color code determining font color.
	 * @param fontSize
	 *            Integer value determining font size.
	 * @param fontFamily
	 *            String determining the font.
	 * @param fontWeight
	 *            Weight: normal, bold.
	 * @param fontStyle
	 *            The font style: normal, italic, oblique.
	 */
	public FontStyle(String fillColor, int fontSize, String fontFamily, String fontWeight, String fontStyle) {
		this.fillColor = fillColor;
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
		this.fontWeight = fontWeight;
		this.fontStyle = fontStyle;
	}

	/**
	 * Initialize this style object with another, copying it's values.
	 * 
	 * @param other
	 *            The other style object from who to copy the field values.
	 */
	public FontStyle(FontStyle other) {
		this.fillColor = other.fillColor;
		this.fontSize = other.fontSize;
		this.fontFamily = other.fontFamily;
		this.fontWeight = other.fontWeight;
		this.fontStyle = other.fontStyle;
	}

	// Other functions:

	public FontStyle(FontStyleInfo info) {
		this.fillColor = info.getColor();
		this.fontSize = info.getSize();
		this.fontFamily = info.getFamily();
		this.fontWeight = info.getWeight();
		this.fontStyle = info.getStyle();
	}

	/**
	 * Scale only the font-size.
	 */
	public void scale(double scale) {
		fontSize = (int) Math.ceil(fontSize * scale);
	}

	/**
	 * Return a clone of this style object.
	 */
	public Style clone() {
		return new FontStyle(this);
	}

	// Getters and setters:

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		if (fontSize > 0) {
			this.fontSize = fontSize;
		}
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public String getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
}