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

package org.geomajas.plugin.wmsclient.client.layer;

import org.geomajas.plugin.wmsclient.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.client.layer.config.WmsTileConfiguration;

import com.google.inject.name.Named;

/**
 * GIN factory that creates {@link WmsLayer} or {@link FeaturesSupportedWmsLayer} instances.
 * 
 * TODO Should we not remove the ViewPort and EventBus parameters? These should be added when the layer is added to a
 * map.
 * 
 * @author Pieter De Graef
 */
public interface WmsLayerFactory {

	String BASE_WMS_LAYER = "wms-layer";

	String FEATURESSUPPORTED_WMS_LAYER = "featuressupported-wms-layer";

	/**
	 * Create a basic {@link WmsLayer} instance.
	 * 
	 * @param title
	 *            The layer title.
	 * @param wmsConfig
	 *            The WMS configuration.
	 * @param tileConfig
	 *            The tile configuration.
	 * @return The {@link WmsLayer} instance.
	 */
	@Named(BASE_WMS_LAYER)
	WmsLayer createWmsLayer(String title, WmsLayerConfiguration wmsConfig, WmsTileConfiguration tileConfig);

	/**
	 * Create a basic WMS layer instance that also support GetFeatureInfo requests.
	 * 
	 * @param title
	 *            The layer title.
	 * @param wmsConfig
	 *            The WMS configuration.
	 * @param tileConfig
	 *            The tile configuration.
	 * @return The {@link WmsLayer} instance.
	 */
	@Named(FEATURESSUPPORTED_WMS_LAYER)
	FeaturesSupportedWmsLayer createFeaturesSupportedWmsLayer(String title, WmsLayerConfiguration wmsConfig,
			WmsTileConfiguration tileConfig);
}