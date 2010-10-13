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

package org.geomajas.global;

/**
 * Interface to implement for cache-context friendly objects.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface CacheableObject {

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 * <p/>
	 * It is hoped that identical object (or objects which could result in identical results) have the save cacheId.
	 * <p/>
	 * This is similar to {@link Object#toString()} but with additional guarantees. This is similar to
	 * {@link Object#hashCode()} but using String type.
	 *
	 * @return id which can be used for caching purposes
	 * @since 1.8.0
	 */
	String getCacheId();
}
