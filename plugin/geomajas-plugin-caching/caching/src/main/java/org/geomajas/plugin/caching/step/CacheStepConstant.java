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
