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

package org.geomajas.plugin.wmsclient.client;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.GeomajasGinModule;
import org.geomajas.gwt2.client.GeomajasGinjector;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerFactory;
import org.geomajas.plugin.wmsclient.client.service.WmsService;

import com.google.gwt.inject.client.GinModules;

/**
 * Ginjector specific for the WMS client plugin. It provides access to the most important singletons in this plugin.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@GinModules({ GeomajasGinModule.class, WmsClientGinModule.class })
public interface WmsClientGinjector extends GeomajasGinjector {

	/**
	 * Get the WMS client service singleton.
	 * 
	 * @return Get the WMS client service singleton.
	 */
	WmsService getWmsService();

	/**
	 * Get the factory singleton for creating {@link org.geomajas.plugin.wmsclient.client.layer.WmsLayer} and
	 * {@link org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer} instances.
	 * 
	 * @return The factory singleton for creating {@link org.geomajas.plugin.wmsclient.client.layer.WmsLayer} and
	 *         {@link org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer} instances.
	 */
	WmsLayerFactory getWmsLayerFactory();
}