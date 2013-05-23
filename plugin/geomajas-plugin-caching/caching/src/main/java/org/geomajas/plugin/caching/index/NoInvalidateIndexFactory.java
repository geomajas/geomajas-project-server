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
 * Spatial index implementation which does not really index. It never says that something needs to be invalidated.
 * <p/>
 * This is used for the rebuild cache as this does not contain the actual data, only how to get it.
 *
 * @author Joachim Van der Auwera
 */
@FutureApi
public class NoInvalidateIndexFactory implements CacheIndexFactory {

	private static final NoInvalidateIndexService INSTANCE = new NoInvalidateIndexService();

	@Override
	public CacheIndexService create(Layer layer, CacheCategory category) {
		return INSTANCE;
	}
}
