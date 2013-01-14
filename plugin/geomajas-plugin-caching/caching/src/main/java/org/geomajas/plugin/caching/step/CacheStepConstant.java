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

package org.geomajas.plugin.caching.step;

/**
 * Pipeline constants for caching steps.
 *
 * @author Joachim Van der Auwera
 */
public interface CacheStepConstant {

	/** Pipeline key for the bounds. */
	String CACHE_BOUNDS_KEY = "CacheBounds-key";
	/** Pipeline key for the bounds caching context. */
	String CACHE_BOUNDS_CONTEXT = "CacheBounds-context";

	/** Pipeline key for the feature. */
	String CACHE_FEATURES_KEY = "CacheFeatures-key";
	/** Pipeline key for the features caching context. */
	String CACHE_FEATURES_CONTEXT = "CacheFeatures-context";

	/** Pipeline key for the tile. */
	String CACHE_TILE_KEY = "CacheTile-key";
	/** Pipeline key for the tile caching context. */
	String CACHE_TILE_CONTEXT = "CacheTile-context";
}
