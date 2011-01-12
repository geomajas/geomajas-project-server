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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.Layer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link CacheManagerService} implementation.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class CacheManagerServiceImpl implements CacheManagerService {

	@Autowired
	private ApplicationContext applicationContext;

	private Map<String, Map<CacheCategory, IndexedCache>> caches =
			new ConcurrentHashMap<String, Map<CacheCategory, IndexedCache>>();

	public void put(Layer layer, CacheCategory category, String key, Object object, Envelope envelope) {
		getCache(layer, category).put(key, object, envelope);
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

	public void invalidate(Layer layer, CacheCategory category, Envelope envelope) {
		IndexedCache cache = getCache(layer, category, false);
		if (null != cache) {
			cache.invalidate(envelope);
		}
	}

	public void invalidate(Layer layer, Envelope envelope) {
		for (IndexedCache cache : getCaches(layer)) {
			cache.invalidate(envelope);
		}
	}

	public void invalidate(Layer layer) {
		for (IndexedCache cache : getCaches(layer)) {
			cache.clear();
		}
	}

	private IndexedCache getCache(Layer layer, CacheCategory cacheCategory) {
		return getCache(layer, cacheCategory, true);
	}

	public CacheService getCacheForTesting(String layerId, CacheCategory cacheCategory) {
		Map<CacheCategory, IndexedCache> layerCaches = caches.get(layerId);
		if (null != layerCaches) {
			IndexedCache cache = layerCaches.get(cacheCategory);
			if (null != cache) {
				return cache.getCacheForTesting();
			}
		}
		return null;
	}

	IndexedCache getCache(Layer layer, CacheCategory cacheCategory, boolean createIfNotExists) {
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
			cache = createCache(layer, cacheCategory);
			layerCaches.put(cacheCategory, cache);
		}
		return cache;
	}

	List<IndexedCache> getCaches(Layer layer) {
		List<IndexedCache> list = new ArrayList<IndexedCache>();
		Map<CacheCategory, IndexedCache> layerCaches = caches.get(layer.getId());
		if (null != layerCaches) {
			list.addAll(layerCaches.values());
		}
		return list;
	}

	IndexedCache createCache(Layer layer, CacheCategory cacheCategory) {
		String layerId = layer.getId();
		CacheInfo cacheInfo = getInfo(layerId, cacheCategory, CacheInfo.class);
		CacheIndexInfo cacheIndexInfo = getInfo(layerId, cacheCategory, CacheIndexInfo.class);
		return new IndexedCache(cacheInfo.getCacheFactory().create(layer, cacheCategory),
				cacheIndexInfo.getCacheIndexFactory().create(layer, cacheCategory));
	}

	/**
	 * Find the info object for the layer/category pair. It uses the last defined, either with matching layer and
	 * category, with matching layer and null category, with null layer and matching category, with both null, in that
	 * order.
	 *
	 * @param layerId layer to search for
	 * @param cacheCategory category to search for
	 * @param type type of info object to find
	 * @param <TYPE> type of info object to find
	 * @return configuration object of requested type or null if not found
	 */
	<TYPE extends LayerCategoryInfo> TYPE getInfo(String layerId, CacheCategory cacheCategory, Class<TYPE> type) {
		TYPE specific = null;
		TYPE semiLayer = null;
		TYPE semiCategory = null;
		TYPE fallback = null;
		Collection<TYPE> options = applicationContext.getBeansOfType(type).values();
		for (TYPE option : options) {
			String optLayer = option.getLayerId();
			CacheCategory optCategory = option.getCategory();
			if (null != optLayer) {
				if (optLayer.equals(layerId)) {
					if (null != optCategory) {
						if (optCategory.equals(cacheCategory)) {
							specific = option;
						}
					} else {
						semiLayer = option;
					}
				}
			} else {
				if (null != optCategory) {
					if (optCategory.equals(cacheCategory)) {
						semiCategory = option;
					}
				} else {
					fallback = option;
				}
			}
		}
		if (null != specific) {
			return specific;
		}
		if (null != semiLayer) {
			return semiLayer;
		}
		if (null != semiCategory) {
			return semiCategory;
		}
		return fallback;
	}
}
