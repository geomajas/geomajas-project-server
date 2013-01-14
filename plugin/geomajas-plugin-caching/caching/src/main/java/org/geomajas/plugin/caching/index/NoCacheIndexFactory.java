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

package org.geomajas.plugin.caching.index;

import org.geomajas.annotation.FutureApi;
import org.geomajas.layer.Layer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheIndexFactory;
import org.geomajas.plugin.caching.service.CacheIndexService;

/**
 * Create a spatial index which does not index anything (thus always forces everything to be invalidated).
 *
 * @author Joachim Van der Auwera
 */
@FutureApi
public class NoCacheIndexFactory implements CacheIndexFactory {

	private static final NoCacheIndexService INSTANCE = new NoCacheIndexService();

	/** {@inheritDoc} */
	public CacheIndexService create(Layer layer, CacheCategory category) {
		return INSTANCE;
	}
}
