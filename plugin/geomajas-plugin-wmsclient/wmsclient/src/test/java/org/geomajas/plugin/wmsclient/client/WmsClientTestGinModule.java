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
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.plugin.wmsclient.client.service.WmsServiceImpl;
import org.geomajas.plugin.wmsclient.client.service.WmsTileService;
import org.geomajas.plugin.wmsclient.client.service.WmsTileServiceImpl;
import org.geomajas.puregwt.client.controller.MapEventParserFactory;
import org.geomajas.puregwt.client.controller.MockMapEventParserFactory;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.GfxUtilImpl;
import org.geomajas.puregwt.client.gfx.HtmlImage;
import org.geomajas.puregwt.client.gfx.HtmlImageFactory;
import org.geomajas.puregwt.client.gfx.HtmlImageImpl;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.MapPresenterImpl;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.ViewPortImpl;
import org.geomajas.puregwt.client.map.feature.FeatureFactory;
import org.geomajas.puregwt.client.map.feature.FeatureServiceFactory;
import org.geomajas.puregwt.client.map.feature.MockFeatureFactory;
import org.geomajas.puregwt.client.map.feature.MockFeatureServiceFactory;
import org.geomajas.puregwt.client.map.layer.LayerFactory;
import org.geomajas.puregwt.client.map.layer.LayersModel;
import org.geomajas.puregwt.client.map.layer.LayersModelImpl;
import org.geomajas.puregwt.client.map.layer.RasterServerLayer;
import org.geomajas.puregwt.client.map.layer.RasterServerLayerImpl;
import org.geomajas.puregwt.client.map.layer.VectorServerLayer;
import org.geomajas.puregwt.client.map.layer.VectorServerLayerImpl;
import org.geomajas.puregwt.client.map.render.MapRendererFactory;
import org.geomajas.puregwt.client.map.render.LayerScalesRendererFactory;
import org.geomajas.puregwt.client.map.render.MockMapRendererFactory;
import org.geomajas.puregwt.client.map.render.MockMapScalesRendererFactory;
import org.geomajas.puregwt.client.map.render.RasterLayerScaleRenderer;
import org.geomajas.puregwt.client.map.render.LayerScaleRenderer;
import org.geomajas.puregwt.client.map.render.TiledScaleRendererFactory;
import org.geomajas.puregwt.client.map.render.VectorLayerScaleRenderer;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.service.EndPointService;
import org.geomajas.puregwt.client.service.EndPointServiceImpl;
import org.geomajas.puregwt.client.service.MockCommandService;
import org.geomajas.puregwt.client.widget.MapWidgetTestImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Gin binding module for the WMS client plugin.
 * 
 * @author Pieter De Graef
 */
public class WmsClientTestGinModule extends AbstractModule {

	protected void configure() {
		// Original Geomajas bindings:
		bind(MapPresenter.class).to(MapPresenterImpl.class);
		bind(LayersModel.class).to(LayersModelImpl.class);
		install(new FactoryModuleBuilder().implement(VectorServerLayer.class, VectorServerLayerImpl.class)
				.implement(RasterServerLayer.class, RasterServerLayerImpl.class).build(LayerFactory.class));
		bind(ViewPort.class).to(ViewPortImpl.class);
		bind(MapWidget.class).to(MapWidgetTestImpl.class);
		bind(FeatureFactory.class).to(MockFeatureFactory.class);
		bind(MapEventParserFactory.class).to(MockMapEventParserFactory.class);
		bind(FeatureServiceFactory.class).to(MockFeatureServiceFactory.class);
		bind(MapRendererFactory.class).to(MockMapRendererFactory.class);
		bind(LayerScalesRendererFactory.class).to(MockMapScalesRendererFactory.class);

		install(new FactoryModuleBuilder()
				.implement(LayerScaleRenderer.class, Names.named(TiledScaleRendererFactory.VECTOR_NAME),
						VectorLayerScaleRenderer.class)
				.implement(LayerScaleRenderer.class, Names.named(TiledScaleRendererFactory.RASTER_NAME),
						RasterLayerScaleRenderer.class).build(TiledScaleRendererFactory.class));
		install(new FactoryModuleBuilder().implement(HtmlImage.class, HtmlImageImpl.class)
				.build(HtmlImageFactory.class));
		bind(GfxUtil.class).to(GfxUtilImpl.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(CommandService.class).to(MockCommandService.class).in(Singleton.class);
		bind(EndPointService.class).to(EndPointServiceImpl.class).in(Singleton.class);

		// Rendering package:
		install(new FactoryModuleBuilder().implement(WmsLayerRenderer.class, WmsLayerRendererImpl.class).build(
				WmsLayerRendererFactory.class));
		install(new FactoryModuleBuilder().implement(WmsTiledScaleRenderer.class, WmsTiledScaleRendererImpl.class)
				.build(WmsTiledScaleRendererFactory.class));

		// Service package:
		bind(WmsTileService.class).to(WmsTileServiceImpl.class).in(Singleton.class);
		bind(WmsService.class).to(WmsServiceImpl.class).in(Singleton.class);

		// Layer package:
		install(new FactoryModuleBuilder()
				.implement(WmsLayer.class, Names.named(WmsLayerFactory.BASE_WMS_LAYER), WmsLayerImpl.class)
				.implement(FeaturesSupportedWmsLayer.class, Names.named(WmsLayerFactory.FEATURESSUPPORTED_WMS_LAYER),
						FeaturesSupportedWmsLayerImpl.class).build(WmsLayerFactory.class));
	}
}