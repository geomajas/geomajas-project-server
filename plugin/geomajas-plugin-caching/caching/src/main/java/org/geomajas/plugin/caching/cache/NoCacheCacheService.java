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

	/** {@inheritDoc} */
	public void put(String key, Object object) {
		// do nothing
	}

	/** {@inheritDoc} */
	public Object get(String key) {
		// do nothing
		return null;
	}

	/** {@inheritDoc} */
	public <TYPE> TYPE get(String key, Class<TYPE> type) {
		// do nothing
		return null;
	}

	/** {@inheritDoc} */
	public void remove(String key) {
		// do nothing
	}

	/** {@inheritDoc} */
	public void clear() {
		// do nothing
	}

	/** {@inheritDoc} */
	public void drop() {
		// do nothing
	}
}
