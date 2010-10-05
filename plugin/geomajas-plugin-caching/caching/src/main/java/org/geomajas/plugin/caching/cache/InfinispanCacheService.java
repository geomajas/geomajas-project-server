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

package org.geomajas.plugin.caching.cache;

import org.geomajas.plugin.caching.service.CacheService;
import org.infinispan.Cache;

/**
 * Geomajas cache based on infinispan.
 *
 * @author Joachim Van der Auwera
 */
public class InfinispanCacheService implements CacheService {

	private Cache<String, Object> cache;

	public InfinispanCacheService(Cache<String, Object> cache) {
		this.cache = cache;
	}

	public void put(String key, Object object) {
		cache.putAsync(key, object);
	}

	public Object get(String key) {
		return cache.get(key);
	}

	public <TYPE> TYPE get(String key, Class<TYPE> type) {
		Object res = get(key);
		if (type.isInstance(res)) {
			return (TYPE) res;
		}
		return null;
	}

	public void remove(String key) {
		cache.remove(key);
	}

	public void clear() {
		cache.clear();
	}

	public void drop() {
		clear();
	}
}
