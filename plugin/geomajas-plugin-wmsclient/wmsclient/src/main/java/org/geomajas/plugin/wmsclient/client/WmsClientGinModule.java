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

import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayerImpl;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerFactory;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerImpl;
import org.geomajas.plugin.wmsclient.client.render.WmsLayerRenderer;
import org.geomajas.plugin.wmsclient.client.render.WmsLayerRendererFactory;
import org.geomajas.plugin.wmsclient.client.render.WmsLayerRendererImpl;
import org.geomajas.plugin.wmsclient.client.render.WmsTiledScaleRenderer;
import org.geomajas.plugin.wmsclient.client.render.WmsTiledScaleRendererFactory;
import org.geomajas.plugin.wmsclient.client.render.WmsTiledScaleRendererImpl;
import org.geomajas.plugin.wmsclient.client.service.WmsTileService;
import org.geomajas.plugin.wmsclient.client.service.WmsTileServiceImpl;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.plugin.wmsclient.client.service.WmsServiceImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * Gin binding module for the WMS client plugin.
 * 
 * @author Pieter De Graef
 */
public class WmsClientGinModule extends AbstractGinModule {

	protected void configure() {
		// Rendering package:
		install(new GinFactoryModuleBuilder().implement(WmsLayerRenderer.class, WmsLayerRendererImpl.class).build(
				WmsLayerRendererFactory.class));
		install(new GinFactoryModuleBuilder().implement(WmsTiledScaleRenderer.class, WmsTiledScaleRendererImpl.class)
				.build(WmsTiledScaleRendererFactory.class));

		// Service package:
		bind(WmsTileService.class).to(WmsTileServiceImpl.class).in(Singleton.class);
		bind(WmsService.class).to(WmsServiceImpl.class).in(Singleton.class);

		// Layer package:
		install(new GinFactoryModuleBuilder()
				.implement(WmsLayer.class, Names.named(WmsLayerFactory.BASE_WMS_LAYER), WmsLayerImpl.class)
				.implement(FeaturesSupportedWmsLayer.class, Names.named(WmsLayerFactory.FEATURESSUPPORTED_WMS_LAYER),
						FeaturesSupportedWmsLayerImpl.class).build(WmsLayerFactory.class));
	}
}