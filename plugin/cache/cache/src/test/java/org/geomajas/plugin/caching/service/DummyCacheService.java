/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Dummy cache for testing.
 *
 * @author Joachim Van der Auwera
 */
public class DummyCacheService implements CacheService {

	private Map<String, Object> map = new HashMap<String, Object>();

	public void put(String key, Object object) {
		map.put(key, object);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public <TYPE> TYPE get(String key, Class<TYPE> type) {
		Object res = get(key);
		if (type.isInstance(res)) {
			return (TYPE) res;
		}
		return null;
	}

	public void remove(String key) {
		map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public void drop() {
		clear();
	}

	public long size() {
		return map.size();
	}

	public String getKey() {
		if (1 == size()) {
			return map.entrySet().iterator().next().getKey();
		}
		return null;
	}

	public Object getObject() {
		if (1 == size()) {
			return map.entrySet().iterator().next().getValue();
		}
		return null;
	}
}
