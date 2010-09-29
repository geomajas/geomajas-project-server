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

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.layer.Layer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link CacheManagerService} implementation.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class CacheManagerImpl implements CacheManagerService {

	private Map<String, Map<CacheCategory, IndexedCache>> caches =
			new ConcurrentHashMap<String, Map<CacheCategory, IndexedCache>>();

	public void put(Layer layer, CacheCategory category, String key, Object object, Geometry geometry) {
		getCache(layer, category).put(key, object, geometry);
	}

	public Object get(Layer layer, CacheCategory category, String key) {
		return getCache(layer, category).get(key, Object.class);
	}

	public <TYPE> TYPE get(Layer layer, CacheCategory category, String key, Class<TYPE> type) {
		return getCache(layer, category).get(key, type);
	}

	public void remove(Layer layer, CacheCategory category, String key) {
		getCache(layer, category).remove(key);
	}

	public void drop(Layer layer, CacheCategory category) {
		IndexedCache cache = getCache(layer, category, false);
		if (null != cache) {
			cache.drop();
		}
	}

	public void drop(Layer layer) {
		List<IndexedCache> layerCaches = getCaches(layer);
		for (IndexedCache cache : layerCaches) {
			cache.drop();
		}
		caches.remove(layer.getId());
	}

	public void invalidate(Layer layer, CacheCategory category, Geometry geometry) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void invalidate(Layer layer, Geometry geometry) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	private IndexedCache getCache(Layer layer, CacheCategory cacheCategory) {
		return getCache(layer, cacheCategory, true);
	}

	private IndexedCache getCache(Layer layer, CacheCategory cacheCategory, boolean createIfNotExists) {
		String layerId = layer.getId();
		Map<CacheCategory, IndexedCache> layerCaches = caches.get(layerId);
		IndexedCache cache;
		if (null == layerCaches) {
			layerCaches = new ConcurrentHashMap<CacheCategory, IndexedCache>();
			caches.put(layerId, layerCaches);
			layerCaches = caches.get(layerId); // request again for concurrency?
		}
		cache = layerCaches.get(cacheCategory);
		if (null == cache && createIfNotExists) {
			// @todo create cache
		}
		return cache;
	}

	private List<IndexedCache> getCaches(Layer layer) {
		return null; // @todo implement
	}
}
