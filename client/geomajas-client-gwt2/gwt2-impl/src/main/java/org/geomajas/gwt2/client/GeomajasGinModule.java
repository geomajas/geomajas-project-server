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

package org.geomajas.gwt2.client;

import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt2.client.controller.MapEventParserFactory;
import org.geomajas.gwt2.client.controller.MapEventParserImpl;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.gfx.GfxUtilImpl;
import org.geomajas.gwt2.client.gfx.HtmlImage;
import org.geomajas.gwt2.client.gfx.HtmlImageFactory;
import org.geomajas.gwt2.client.gfx.HtmlImageImpl;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.MapPresenterImpl;
import org.geomajas.gwt2.client.map.ViewPort;
import org.geomajas.gwt2.client.map.ViewPortImpl;
import org.geomajas.gwt2.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.gwt2.client.map.feature.Feature;
import org.geomajas.gwt2.client.map.feature.FeatureFactory;
import org.geomajas.gwt2.client.map.feature.FeatureImpl;
import org.geomajas.gwt2.client.map.feature.FeatureService;
import org.geomajas.gwt2.client.map.feature.FeatureServiceFactory;
import org.geomajas.gwt2.client.map.feature.FeatureServiceImpl;
import org.geomajas.gwt2.client.map.layer.LayerFactory;
import org.geomajas.gwt2.client.map.layer.LayersModel;
import org.geomajas.gwt2.client.map.layer.LayersModelImpl;
import org.geomajas.gwt2.client.map.layer.RasterServerLayer;
import org.geomajas.gwt2.client.map.layer.RasterServerLayerImpl;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayerImpl;
import org.geomajas.gwt2.client.map.render.LayerRenderer;
import org.geomajas.gwt2.client.map.render.LayerScaleRenderer;
import org.geomajas.gwt2.client.map.render.LayerScalesRendererFactory;
import org.geomajas.gwt2.client.map.render.LayerScalesRendererImpl;
import org.geomajas.gwt2.client.map.render.MapRenderer;
import org.geomajas.gwt2.client.map.render.MapRendererFactory;
import org.geomajas.gwt2.client.map.render.MapRendererImpl;
import org.geomajas.gwt2.client.map.render.RasterLayerScaleRenderer;
import org.geomajas.gwt2.client.map.render.TiledScaleRendererFactory;
import org.geomajas.gwt2.client.map.render.VectorLayerScaleRenderer;
import org.geomajas.gwt2.client.service.CommandService;
import org.geomajas.gwt2.client.service.CommandServiceImpl;
import org.geomajas.gwt2.client.service.EndPointService;
import org.geomajas.gwt2.client.service.EndPointServiceImpl;
import org.geomajas.gwt2.client.widget.MapWidgetImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Gin binding module. All bindings defined in here are used by the GeomajasGinjector.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class GeomajasGinModule extends AbstractGinModule {

	protected void configure() {
		// Map related interfaces:
		bind(MapPresenter.class).to(MapPresenterImpl.class);
		bind(LayersModel.class).to(LayersModelImpl.class);
		bind(ViewPort.class).to(ViewPortImpl.class);
		bind(MapWidget.class).to(MapWidgetImpl.class);
		install(new GinFactoryModuleBuilder().implement(Feature.class, FeatureImpl.class).build(FeatureFactory.class));
		install(new GinFactoryModuleBuilder().implement(MapEventParser.class, MapEventParserImpl.class).build(
				MapEventParserFactory.class));
		install(new GinFactoryModuleBuilder().implement(FeatureService.class, FeatureServiceImpl.class).build(
				FeatureServiceFactory.class));
		install(new GinFactoryModuleBuilder().implement(MapRenderer.class, MapRendererImpl.class).build(
				MapRendererFactory.class));
		install(new GinFactoryModuleBuilder().implement(VectorServerLayer.class, VectorServerLayerImpl.class)
				.implement(RasterServerLayer.class, RasterServerLayerImpl.class).build(LayerFactory.class));
		install(new GinFactoryModuleBuilder().implement(LayerRenderer.class, LayerScalesRendererImpl.class)
				.build(LayerScalesRendererFactory.class));
		install(new GinFactoryModuleBuilder()
				.implement(LayerScaleRenderer.class, Names.named(TiledScaleRendererFactory.VECTOR_NAME),
						VectorLayerScaleRenderer.class)
				.implement(LayerScaleRenderer.class, Names.named(TiledScaleRendererFactory.RASTER_NAME),
						RasterLayerScaleRenderer.class).build(TiledScaleRendererFactory.class));
		install(new GinFactoryModuleBuilder().implement(HtmlImage.class, HtmlImageImpl.class).build(
				HtmlImageFactory.class));

		// Other:
		bind(CommandService.class).to(CommandServiceImpl.class).in(Singleton.class);
		bind(EndPointService.class).to(EndPointServiceImpl.class).in(Singleton.class);
		bind(GfxUtil.class).to(GfxUtilImpl.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}
}