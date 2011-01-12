/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
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

	String CACHE_BOUNDS_KEY = "CacheBounds-key";
	String CACHE_BOUNDS_CONTEXT = "CacheBounds-context";
	String CACHE_BOUNDS_USED = "CacheBounds-used";

	String CACHE_FEATURES_KEY = "CacheFeatures-key";
	String CACHE_FEATURES_CONTEXT = "CacheFeatures-context";
	String CACHE_FEATURES_USED = "CacheFeatures-used";

	String CACHE_TILE_KEY = "CacheTile-key";
	String CACHE_TILE_CONTEXT = "CacheTile-context";
	String CACHE_TILE_USED = "CacheTile-used";

	String CACHE_TILE_CONTENT_KEY = "CacheTileContent-key";
	String CACHE_TILE_CONTENT_CONTEXT = "CacheTileContent-context";
	String CACHE_TILE_CONTENT_USED = "CacheTileContent-used";
}
