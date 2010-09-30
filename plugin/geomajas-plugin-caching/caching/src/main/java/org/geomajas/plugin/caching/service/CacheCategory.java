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

package org.geomajas.plugin.caching.service;

import org.geomajas.global.Api;

/**
 * Immutable objects which represent cache categories. These a just strings which are made type safe.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class CacheCategory {

	public static final CacheCategory VML = new CacheCategory("vml");
	public static final CacheCategory SVG = new CacheCategory("svg");
	public static final CacheCategory RASTER = new CacheCategory("raster");
	public static final CacheCategory TILE = new CacheCategory("tile");
	public static final CacheCategory REBUILD = new CacheCategory("rebuild");
	public static final CacheCategory FEATURE = new CacheCategory("feature");

	private String name;

	public CacheCategory(String name) {
		if (null == name) {
			throw new IllegalStateException("Null CacheCategory is not allowed");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CacheCategory)) {
			return false;
		}

		CacheCategory cacheCategory = (CacheCategory) o;

		return name.equals(cacheCategory.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
