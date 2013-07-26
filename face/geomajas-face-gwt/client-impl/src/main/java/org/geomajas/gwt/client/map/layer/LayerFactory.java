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

package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.map.MapEventBus;
import org.geomajas.gwt.client.map.ViewPort;

/**
 * Gin factory for {@link VectorServerLayer} and {@link RasterServerLayer} layers.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface LayerFactory {

	/**
	 * Create a vector layer for the specified configuration and view port.
	 * 
	 * @param clientVectorLayerInfo the configuration info
	 * @param viewPort the view port
	 * @param eventBus the map event bus
	 * @return the layer
	 */
	VectorServerLayer createVectorLayer(ClientVectorLayerInfo clientVectorLayerInfo, ViewPort viewPort,
			MapEventBus eventBus);
	
	/**
	 * Create a raster layer for the specified configuration and view port.
	 * 
	 * @param clientRasterLayerInfo the configuration info
	 * @param viewPort the view port
	 * @param eventBus the map event bus
	 * @return the layer
	 */
	RasterServerLayer createRasterLayer(ClientRasterLayerInfo clientRasterLayerInfo, ViewPort viewPort,
			MapEventBus eventBus);
}
