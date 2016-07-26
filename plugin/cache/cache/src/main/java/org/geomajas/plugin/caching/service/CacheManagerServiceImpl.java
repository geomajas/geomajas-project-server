/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.layer.Layer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(CacheManagerServiceImpl.class);
	
	@Autowired
	private ApplicationContext applicationContext;

	private final Map<String, Map<CacheCategory, IndexedCache>> caches =
			new ConcurrentHashMap<String, Map<CacheCategory, IndexedCache>>();

	@Override
	public void put(Layer layer, CacheCategory category, String key, Object object, Envelope envelope) {
		if (log.isDebugEnabled()) {
			log.debug("Put: {} {} {}", new String[] {getLogLayerId(layer), category.toString(), key});
		}
		getCache(layer, category).put(key, object, envelope);
	}

	@Override
	public Object get(Layer layer, CacheCategory category, String key) {
		Object o = getCache(layer, category).get(key, Object.class);
		if (log.isDebugEnabled()) {
			log.debug("Get {}: {} {} {}", new String[] {
				o != null ? "Hit" : "Miss", getLogLayerId(layer), category.toString(), key});
		}
		return o;
	}

	@Override
	public <TYPE> TYPE get(Layer layer, CacheCategory category, String key, Class<TYPE> type) {
		TYPE o = getCache(layer, category).get(key, type); 
		if (log.isDebugEnabled()) {
			log.debug("Get {}: {} {} {}", new String[] {
					o != null ? "Hit" : "Miss", getLogLayerId(layer), category.toString(), key});
		}
		return o;
	}

	@Override
	public void remove(Layer layer, CacheCategory category, String key) {
		if (log.isDebugEnabled()) {
			log.debug("Remove: {} {} {}", new String[] {getLogLayerId(layer), category.toString(), key});
		}
		getCache(layer, category).remove(key);
	}

	@Override
	public void drop(Layer layer, CacheCategory category) {
		if (log.isDebugEnabled()) {
			log.debug("Drop: {} {}", getLogLayerId(layer), category.toString());
		}
		IndexedCache cache = getCache(layer, category, false);
		if (null != cache) {
			cache.drop();
		}
		if (null != layer) {
			IndexedCache metaCache = getCache(null, category, false);
			if (null != metaCache) {
				metaCache.clear();
			}
		}
	}

	@Override
	public void drop(Layer layer) {
		if (log.isDebugEnabled()) {
			log.debug("Drop: {}", getLogLayerId(layer));
		}
		if (null != layer) {
			// drop requested layer if not null
			List<IndexedCache> layerCaches = getCaches(layer);
			for (IndexedCache cache : layerCaches) {
				cache.drop();
			}
			caches.remove(getLayerId(layer));
		}
		// clear meta-layer
		List<IndexedCache> layerCaches = getCaches(null);
		for (IndexedCache cache : layerCaches) {
			cache.clear();
		}
	}

	@Override
	public void invalidate(Layer layer, CacheCategory category, Envelope envelope) {
		if (log.isDebugEnabled()) {
			log.debug("Invalidate: {} {}", getLogLayerId(layer), category.toString());
		}
		IndexedCache cache = getCache(layer, category, false);
		if (null != cache) {
			cache.invalidate(envelope);
		}
		if (null != layer) {
			IndexedCache metaCache = getCache(null, category, false);
			if (null != metaCache) {
				metaCache.invalidate(envelope);
			}
		}
	}

	@Override
	public void invalidate(Layer layer, Envelope envelope) {
		if (log.isDebugEnabled()) {
			log.debug("Invalidate: {}", getLogLayerId(layer));
		}
		for (IndexedCache cache : getCaches(layer)) {
			cache.invalidate(envelope);
		}
		if (null != layer) {
			for (IndexedCache cache : getCaches(null)) {
				cache.invalidate(envelope);
			}
		}
	}

	@Override
	public void invalidate(Layer layer) {
		if (log.isDebugEnabled()) {
			log.debug("Invalidate: {}", getLogLayerId(layer));
		}
		for (IndexedCache cache : getCaches(layer)) {
			cache.clear();
		}
		if (null != layer) {
			for (IndexedCache cache : getCaches(null)) {
				cache.clear();
			}
		}
	}

	private IndexedCache getCache(Layer layer, CacheCategory cacheCategory) {
		return getCache(layer, cacheCategory, true);
	}

	/**
	 * Get {@link CacheService} instance to allow testing content.
	 *
	 * @param layerId layer id
	 * @param cacheCategory cache category
	 * @return cache service
	 */
	public CacheService getCacheForTesting(String layerId, CacheCategory cacheCategory) {
		Map<CacheCategory, IndexedCache> layerCaches = caches.get(layerId);
		if (null == layerCaches) {
			layerCaches = caches.get("");
		}
		if (null != layerCaches) {
			IndexedCache cache = layerCaches.get(cacheCategory);
			if (null != cache) {
				return cache.getCacheForTesting();
			}
		}
		return null;
	}

	IndexedCache getCache(Layer layer, CacheCategory cacheCategory, boolean createIfNotExists) {
		String layerId = getLayerId(layer);
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
		String layerId = getLayerId(layer);
		List<IndexedCache> list = new ArrayList<IndexedCache>();
		Map<CacheCategory, IndexedCache> layerCaches = caches.get(layerId);
		if (null != layerCaches) {
			list.addAll(layerCaches.values());
		}
		return list;
	}

	IndexedCache createCache(Layer layer, CacheCategory cacheCategory) {
		String layerId = getLayerId(layer);
		CacheServiceInfo cacheServiceInfo = getInfo(layerId, cacheCategory, CacheServiceInfo.class);
		CacheIndexInfo cacheIndexInfo = getInfo(layerId, cacheCategory, CacheIndexInfo.class);
		return new IndexedCache(cacheServiceInfo.getCacheFactory().create(layer, cacheCategory),
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

	private String getLayerId(Layer layer) {
		String layerId = "";
		if (null != layer) {
			layerId = layer.getId();
		}
		return layerId;
	}

	private String getLogLayerId(Layer layer) {
		String layerId = "null";
		if (null != layer) {
			layerId = layer.getId();
		}
		return layerId;
	}

}
