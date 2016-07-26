/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Symbol configuration information. Choose one the options (either circle, rectangle or image).
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SymbolInfo implements IsInfo, CacheableObject {

	private static final int PRIME = 31;

	private static final long serialVersionUID = 151L;

	private CircleInfo circle;

	private RectInfo rect;

	private ImageInfo image;
	
	
	/**
	 * No args constructor for GWT.
	 */
	public SymbolInfo() {
		// NOSONAR nothing to do
	}
	
	/**
	 * Copy constructor. Creates a deep copy of the specified {@link SymbolInfo} object.
	 * 
	 * @param other the symbol to copy
	 * @since 1.10.0
	 */
	public SymbolInfo(SymbolInfo other) {
		if (other.getCircle() != null) {
			setCircle(new CircleInfo());
			getCircle().setR(other.getCircle().getR());
		}
		if (other.getRect() != null) {
			setRect(new RectInfo());
			getRect().setH(other.getRect().getH());
			getRect().setW(other.getRect().getW());
		}
		if (other.getImage() != null) {
			setImage(new ImageInfo());
			getImage().setHeight(other.getImage().getHeight());
			getImage().setWidth(other.getImage().getWidth());
			getImage().setHref(other.getImage().getHref());
			getImage().setSelectionHref(other.getImage().getSelectionHref());
		}
	}

	/**
	 * Get the circle for the symbol.
	 * 
	 * @return circle
	 */
	public CircleInfo getCircle() {
		return circle;
	}

	/**
	 * Set circle for the symbol.
	 * 
	 * @param circle
	 *            circle
	 */
	public void setCircle(CircleInfo circle) {
		this.circle = circle;
	}

	/**
	 * Get the rectangle for this symbol.
	 * 
	 * @return rectangle
	 */
	public RectInfo getRect() {
		return rect;
	}

	/**
	 * Set the rectangle for this symbol.
	 * 
	 * @param rect
	 *            rectangle
	 */
	public void setRect(RectInfo rect) {
		this.rect = rect;
	}

	/**
	 * Get the image for this symbol.
	 *
	 * @return  Get the image for this symbol.
	 */
	public ImageInfo getImage() {
		return image;
	}

	/**
	 * Set the image for this symbol.
	 * 
	 * @param image
	 *            the image symbol.
	 */
	public void setImage(ImageInfo image) {
		this.image = image;
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 *
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "SymbolInfo{" +
				"circle=" + circle +
				", rect=" + rect +
				", image=" + image +
				'}';
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
	 * @param o object to compare
	 * @return true when objects are equal
	 * @since 1.8.0
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof SymbolInfo)) { return false; }

		SymbolInfo that = (SymbolInfo) o;

		if (circle != null ? !circle.equals(that.circle) : that.circle != null) { return false; }
		if (image != null ? !image.equals(that.image) : that.image != null) { return false; }
		if (rect != null ? !rect.equals(that.rect) : that.rect != null) { return false; }

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
		int result = circle != null ? circle.hashCode() : 0;
		result = PRIME * result + (rect != null ? rect.hashCode() : 0);
		result = PRIME * result + (image != null ? image.hashCode() : 0);
		return result;
	}
}
