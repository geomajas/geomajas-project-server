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

package org.geomajas.plugin.caching.cache;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.caching.service.CacheService;

/**
 * {@link CacheService} which caches nothing.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class NoCacheCacheService implements CacheService {

	@Override
	public void put(String key, Object object) {
		// do nothing
	}

	@Override
	public Object get(String key) {
		// do nothing
		return null;
	}

	@Override
	public <TYPE> TYPE get(String key, Class<TYPE> type) {
		// do nothing
		return null;
	}

	@Override
	public void remove(String key) {
		// do nothing
	}

	@Override
	public void clear() {
		// do nothing
	}

	@Override
	public void drop() {
		// do nothing
	}
}
