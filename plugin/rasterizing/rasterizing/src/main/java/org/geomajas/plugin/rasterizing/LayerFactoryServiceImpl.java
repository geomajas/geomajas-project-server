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
package org.geomajas.plugin.rasterizing;

import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.LayerFactoryService;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link LayerFactoryService}. Iterates over all configured factories.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * 
 */
@Component
public class LayerFactoryServiceImpl implements LayerFactoryService {

	@Autowired
	private List<LayerFactory> factories;

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		for (LayerFactory factory : factories) {
			if (factory.canCreateLayer(mapContext, clientLayerInfo)) {
				return factory.createLayer(mapContext, clientLayerInfo);
			}
		}
		throw new RasterException(RasterException.MISSING_LAYER_FACTORY, clientLayerInfo.getLabel());
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo)
			throws GeomajasException {
		for (LayerFactory factory : factories) {
			if (factory.canCreateLayer(mapContext, clientLayerInfo)) {
				return factory.getLayerUserData(mapContext, clientLayerInfo);
			}
		}
		throw new RasterException(RasterException.MISSING_LAYER_FACTORY, clientLayerInfo.getLabel());
	}
}
