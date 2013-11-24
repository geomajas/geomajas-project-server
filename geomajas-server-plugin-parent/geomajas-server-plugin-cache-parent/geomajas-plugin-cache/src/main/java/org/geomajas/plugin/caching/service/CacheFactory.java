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

package org.geomajas.plugin.caching.service;

import org.geomajas.annotation.Api;
import org.geomajas.layer.Layer;

/**
 * Factory for creating {@link CacheService} instances.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CacheFactory {

	/**
	 * Create a cache instance.
	 *
	 * @param layer layer to create cache for
	 * @param category category to create cache for
	 * @return cache instance
	 */
	CacheService create(Layer layer, CacheCategory category);
}
