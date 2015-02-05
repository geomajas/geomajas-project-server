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
package org.geomajas.plugin.runtimeconfig.service;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link LayerFactoryService}.
 * 
 * @author Jan De Moerloose
 */
@Component
public class LayerFactoryServiceImpl implements LayerFactoryService {

	private Map<Class<?>, LayerFactory<?>> factories = new HashMap<Class<?>, LayerFactory<?>>();

	public <T> T createLayer(Resource resource, Class<T> layerClass) {
		if (factories.containsKey(layerClass)) {
			return (T) factories.get(layerClass).createLayer(resource);
		} else {
			return null;
		}
	}

	public <T extends RasterLayer> void registerRasterLayerFactory(RasterLayerFactory<T> factory) {
		Class<?>[] args = GenericTypeResolver.resolveTypeArguments(factory.getClass(), LayerFactory.class);
		factories.put(args[0], factory);
	}

	public <T extends VectorLayer> void registerVectorLayerFactory(VectorLayerFactory<T> factory) {
		Class<?>[] args = GenericTypeResolver.resolveTypeArguments(factory.getClass(), LayerFactory.class);
		factories.put(args[0], factory);
	}
}
