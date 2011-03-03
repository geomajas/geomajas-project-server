/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing;

import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.LayerFactoryService;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geomajas.plugin.rasterizing.dto.LayerMetadata;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link LayerFactoryService}. Iterates over all configured factories.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class LayerFactoryServiceImpl implements LayerFactoryService {

	@Autowired
	private List<LayerFactory> factories;

	public Layer createLayer(MapContext mapContext, LayerMetadata metadata) throws GeomajasException {
		for (LayerFactory factory : factories) {
			if (factory.canCreateLayer(mapContext, metadata)) {
				return factory.createLayer(mapContext, metadata);
			}
		}
		throw new RasterException(RasterException.MISSING_LAYER_FACTORY, metadata.getLayerId());
	}

}
