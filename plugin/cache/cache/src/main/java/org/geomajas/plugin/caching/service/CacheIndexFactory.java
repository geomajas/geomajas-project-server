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

import org.geomajas.annotation.FutureApi;
import org.geomajas.layer.Layer;

/**
 * Create a {@link CacheIndexService} instance.
 *
 * @author Joachim Van der Auwera
 */
@FutureApi(allMethods = true)
public interface CacheIndexFactory {

	/**
	 * Create a cache index instance.
	 *
	 * @param layer layer to create cache for
	 * @param category category to create cache for
	 * @return cache index instance
	 */
	CacheIndexService create(Layer layer, CacheCategory category);

}
