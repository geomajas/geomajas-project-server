/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import org.geomajas.annotation.Api;

import javax.validation.constraints.NotNull;

/**
 * Configuration of a cache.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class CacheServiceInfo extends LayerCategoryInfo {

	@NotNull
	private CacheFactory cacheFactory;

	/**
	 * Get the factory for creating caches.
	 *
	 * @return cache factory
	 */
	public CacheFactory getCacheFactory() {
		return cacheFactory;
	}

	/**
	 * Set the factory for creating caches.
	 *
	 * @param cacheFactory cache factory
	 */
	public void setCacheFactory(CacheFactory cacheFactory) {
		this.cacheFactory = cacheFactory;
	}
	
}
