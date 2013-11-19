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
 * Rectangle configuration information.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class RectInfo implements IsInfo, CacheableObject {

	private static final int PRIME = 31;

	private static final long serialVersionUID = 151L;
	private float h;
	private float w;

	/**
	 * Get height for the rectangle.
	 *
	 * @return rectangle height
	 */
	public float getH() {
		return h;
	}

	/**
	 * Set rectangle height.
	 *
	 * @param value rectangle height
	 */
	public void setH(float value) {
		this.h = value;
	}

	/**
	 * Get rectangle width.
	 *
	 * @return rectangle width
	 */
	public float getW() {
		return w;
	}

	/**
	 * Set rectangle width.
	 *
	 * @param value rectangle width
	 */
	public void setW(float value) {
		this.w = value;
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
		return "RectInfo{" +
				"h=" + h +
				", w=" + w +
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
		if (!(o instanceof RectInfo)) { return false; }

		RectInfo rectInfo = (RectInfo) o;

		return Float.compare(rectInfo.h, h) == 0 && Float.compare(rectInfo.w, w) == 0;

	}

	/**
	 * Calculate object hash code.
	 *
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		int result = (h != +0.0f ? Float.valueOf(h).hashCode() : 0);
		result = PRIME * result + (w != +0.0f ? Float.valueOf(w).hashCode() : 0);
		return result;
	}
}
