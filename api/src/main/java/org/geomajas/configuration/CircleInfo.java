/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Circle configuration information.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class CircleInfo implements IsInfo, CacheableObject {

	private static final long serialVersionUID = 151L;
	private float r;

	/**
	 * Get circle radius.
	 *
	 * @return radius
	 */
	public float getR() {
		return r;
	}

	/**
	 * Set circle radius.
	 *
	 * @param value radius
	 */
	public void setR(float value) {
		this.r = value;
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
		return "CircleInfo{" +
				"r=" + r +
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
		if (!(o instanceof CircleInfo)) { return false; }

		CircleInfo that = (CircleInfo) o;

		return Float.compare(that.r, r) == 0;

	}

	/**
	 * Calculate object hash code.
	 *
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		return (r != +0.0f ? Float.valueOf(r).hashCode() : 0);
	}
}
