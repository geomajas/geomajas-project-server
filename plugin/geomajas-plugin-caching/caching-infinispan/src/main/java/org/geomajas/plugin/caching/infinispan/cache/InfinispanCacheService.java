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

package org.geomajas.plugin.caching.infinispan.cache;

import org.geomajas.plugin.caching.service.CacheService;
import org.infinispan.Cache;

/**
 * Geomajas cache based on infinispan.
 *
 * @author Joachim Van der Auwera
 */
public class InfinispanCacheService implements CacheService {

	private final Cache<String, Object> cache;

	/**
	 * Create a {@link InfinispanCacheService}.
	 *
	 * @param cache cache
	 */
	public InfinispanCacheService(Cache<String, Object> cache) {
		this.cache = cache;
	}

	/** {@inheritDoc} */
	public void put(String key, Object object) {
		cache.putAsync(key, object);
	}

	/** {@inheritDoc} */
	public Object get(String key) {
		return cache.get(key);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public <TYPE> TYPE get(String key, Class<TYPE> type) {
		Object res = get(key);
		if (type.isInstance(res)) {
			return (TYPE) res;
		}
		return null;
	}

	/** {@inheritDoc} */
	public void remove(String key) {
		cache.remove(key);
	}

	/** {@inheritDoc} */
	public void clear() {
		cache.clear();
	}

	/** {@inheritDoc} */
	public void drop() {
		clear();
	}
}
