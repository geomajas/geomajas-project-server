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
import org.geomajas.layer.Layer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheFactory;
import org.geomajas.plugin.caching.service.CacheService;

/**
 * {@link CacheFactory} implementation which caches nothing.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class NoCacheCacheFactory implements CacheFactory {

	private final NoCacheCacheService cacheService = new NoCacheCacheService();

	@Override
	public CacheService create(Layer layer, CacheCategory category) {
		return cacheService;
	}
}
