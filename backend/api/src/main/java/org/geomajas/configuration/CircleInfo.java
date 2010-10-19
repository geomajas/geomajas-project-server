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

import org.geomajas.global.Api;
import org.geomajas.global.CacheableObject;

import java.io.Serializable;

/**
 * Circle configuration information.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class CircleInfo implements Serializable, CacheableObject {

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

		if (Float.compare(that.r, r) != 0) { return false; }

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
		return (r != +0.0f ? new Float(r).hashCode() : 0);
	}
}
