/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.service.LayerInvalidationService;
import org.geomajas.service.TestRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to assure cached entries are invalidated when
 * {@link org.geomajas.service.ConfigurationService@invalidateLayer} is used.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class CacheLayerInvalidateService implements LayerInvalidationService {

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private TestRecorder recorder;
	
	public void invalidateLayer(Layer layer) throws GeomajasException {
		String layerId = "--null-layer--";
		if (null != layer) {
			layerId = layer.getId();
		}
		recorder.record(layerId, "Layer invalidated");
		cacheManager.drop(layer);
	}
}
