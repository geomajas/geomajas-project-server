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
package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.geomajas.service.CacheService;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the caches service that works in-memory. Note that this service does not distribute objects
 * to different nodes in a load balancing environment.
 * 
 * @author Oliver May
 * 
 */
@Component
public class DefaultCacheService implements CacheService {

	private Map<String, Map<Object, CachedObject>> caches = new ConcurrentHashMap<String, Map<Object, CachedObject>>();

	@Override
	public void put(String cacheId, Object key, Object value) {
		put(cacheId, key, value, DEFAULT_TTL);
	}

	@Override
	public void put(String cacheId, Object key, Object value, long timeToLive) {
		getOrCreateCache(cacheId).put(key, new CachedObject(value, System.currentTimeMillis() + timeToLive));
	}

	@Override
	public Object get(String cacheId, Object key) {
		CachedObject co = getOrCreateCache(cacheId).get(key); 
		return co != null ? co.getObject() : null;
	}

	@Override
	public void cleanUp() {
		List<Object> toRemove = new ArrayList<Object>();
		for (Map<Object, CachedObject> cache : caches.values()) {
			for (Entry<Object, CachedObject> cachedObject : cache.entrySet()) {
				if (cachedObject.getValue().getExpireTime() < System.currentTimeMillis()) {
					toRemove.add(cachedObject.getKey());
				}
			}
			cache.keySet().removeAll(toRemove);
		}
	}

	private synchronized Map<Object, CachedObject> getOrCreateCache(String cacheId) {
		if (caches.containsKey(cacheId)) {
			return caches.get(cacheId);
		} else {
			Map<Object, CachedObject> newCache = new ConcurrentHashMap<Object, DefaultCacheService.CachedObject>();
			caches.put(cacheId, newCache);
			return newCache;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <TYPE> TYPE get(String cache, Object key, Class<TYPE> type) {
		Object o = get(cache, key);
		if (type.isInstance(o)) {
			return (TYPE) o;
		} else {
			return null;
		}
	}

	@Override
	public void remove(String cache, Object key) {
		getOrCreateCache(cache).remove(key);
	}

	@Override
	public void clear(String cache) {
		getOrCreateCache(cache).clear();
	}

	/**
	 * Helper class that stores extra data (expire time) about an object in the cache.
	 * 
	 * @author Oliver May
	 */
	private class CachedObject {
		private Object object;
		private long expireTime;
		public CachedObject(Object object, long expireTime) {
			this.object = object;
			this.expireTime = expireTime;
		}

		public Object getObject() {
			return object;
		}

		public long getExpireTime() {
			return expireTime;
		}

	}

}
