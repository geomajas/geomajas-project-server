/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import org.geomajas.annotation.Api;

/**
 * Immutable objects which represent cache categories. These a just strings which are made type safe.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class CacheCategory {

	/** Cache for raster images. */
	public static final CacheCategory RASTER = new CacheCategory("raster");
	/** Cache for vector tiles. */
	public static final CacheCategory TILE = new CacheCategory("tile");
	/** Cache for rebuild data, allowing rebuild of raster data. */
	public static final CacheCategory REBUILD = new CacheCategory("rebuild");
	/** Cache for features. */
	public static final CacheCategory FEATURE = new CacheCategory("feature");
	/** Cache for layer bounds. */
	public static final CacheCategory BOUNDS = new CacheCategory("bounds");

	private final String name;

	/**
	 * Create a cache category for the given name.
	 *
	 * @param name cache category name
	 */
	public CacheCategory(String name) {
		if (null == name) {
			throw new IllegalArgumentException("Null CacheCategory is not allowed");
		}
		this.name = name;
	}

	/**
	 * Get cache category name.
	 *
	 * @return cache category name
	 */
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	public String toString() {
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
