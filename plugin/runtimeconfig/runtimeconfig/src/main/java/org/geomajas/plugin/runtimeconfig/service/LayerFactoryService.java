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
package org.geomajas.plugin.runtimeconfig.service;

import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.springframework.core.io.Resource;

/**
 * Service to create layers based on Spring resources.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface LayerFactoryService {

	/**
	 * Create a layer of the specified class from the specified resource.
	 * 
	 * @param <T>
	 * @param resource the resource (shape file, image, etc...)
	 * @param layerClass the layer class
	 * @return the layer
	 */
	<T> T createLayer(Resource resource, Class<T> layerClass);

	/**
	 * Register a factory for vector layers with this service.
	 * 
	 * @param <T>
	 * @param factory the factory to register.
	 */
	<T extends VectorLayer> void registerVectorLayerFactory(VectorLayerFactory<T> factory);

	/**
	 * Register a factory for raster layers with this service.
	 * 
	 * @param <T>
	 * @param factory the factory to register.
	 */
	<T extends RasterLayer> void registerRasterLayerFactory(RasterLayerFactory<T> factory);
}
