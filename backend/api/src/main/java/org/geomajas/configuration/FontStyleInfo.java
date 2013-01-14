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
package org.geomajas.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.global.CacheableObject;

/**
 * Information about how to access and how to render the label attribute.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FontStyleInfo implements IsInfo, CacheableObject {

	private static final int PRIME = 31;

	private static final long serialVersionUID = 160L;

	private int size = -1;

	private String family;

	private String weight;

	private String style;

	private String color;

	private float opacity = -1;

	/**
	 * Applies default values to all properties that have not been set.
	 * 
	 * @since 1.8.0
	 */
	public void applyDefaults() {
		if (size == -1) {
			size = 8;
		}
		if (family == null) {
			family = "Verdana";
		}
		if (weight == null) {
			weight = "normal";
		}
		if (style == null) {
			style = "normal";
		}
		if (color == null) {
			color = "#000000"; // black
		}
		if (opacity == -1) {
			opacity = 1;
		}
	}

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

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash codes (like
	 * MD5, SHA1, SHA2 etc) of (collections of) objects.
	 * 
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "FontStyleInfo{" + "size=" + size + ", family='" + family + '\'' + ", weight='" + weight + '\''
				+ ", style='" + style + '\'' + ", color='" + color + '\'' + ", opacity=" + opacity + '}';
	}

	/**
	 * String representation of object.
	 * 
	 * @return string representation of object
	 * @since 1.8.0
	 */
	@Override
	public String toString() {
		return getCacheId();
	}

	/**
	 * Are the two objects equal?
	 * 
	 * @param o
	 *            object to compare
	 * @return true when objects are equal
	 * @since 1.8.0
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof FontStyleInfo)) {
			return false;
		}

		FontStyleInfo that = (FontStyleInfo) o;

		if (Float.compare(that.opacity, opacity) != 0) {
			return false;
		}
		if (size != that.size) {
			return false;
		}
		if (color != null ? !color.equals(that.color) : that.color != null) {
			return false;
		}
		if (family != null ? !family.equals(that.family) : that.family != null) {
			return false;
		}
		if (style != null ? !style.equals(that.style) : that.style != null) {
			return false;
		}
		if (weight != null ? !weight.equals(that.weight) : that.weight != null) {
			return false;
		}

		return true;
	}

	/**
	 * Calculate object hash code.
	 * 
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		int result = size;
		result = PRIME * result + (family != null ? family.hashCode() : 0);
		result = PRIME * result + (weight != null ? weight.hashCode() : 0);
		result = PRIME * result + (style != null ? style.hashCode() : 0);
		result = PRIME * result + (color != null ? color.hashCode() : 0);
		result = PRIME * result + (opacity != +0.0f ? (int) (opacity * 10000) : 0);
		return result;
	}
}
