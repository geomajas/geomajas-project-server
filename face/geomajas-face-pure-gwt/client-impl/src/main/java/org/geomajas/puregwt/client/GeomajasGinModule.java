/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.puregwt.client.controller.MapEventParserFactory;
import org.geomajas.puregwt.client.controller.MapEventParserImpl;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.GfxUtilImpl;
import org.geomajas.puregwt.client.map.DefaultMapGadgetFactory;
import org.geomajas.puregwt.client.map.LayersModel;
import org.geomajas.puregwt.client.map.LayersModelImpl;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.MapPresenterImpl;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.ViewPortImpl;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.map.feature.FeatureFactory;
import org.geomajas.puregwt.client.map.feature.FeatureImpl;
import org.geomajas.puregwt.client.map.feature.FeatureService;
import org.geomajas.puregwt.client.map.feature.FeatureServiceFactory;
import org.geomajas.puregwt.client.map.feature.FeatureServiceImpl;
import org.geomajas.puregwt.client.map.gadget.DefaultMapGadgetFactoryImpl;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.map.layer.RasterLayerFactory;
import org.geomajas.puregwt.client.map.layer.VectorLayer;
import org.geomajas.puregwt.client.map.layer.VectorLayerFactory;
import org.geomajas.puregwt.client.map.render.LayerScalesRenderer;
import org.geomajas.puregwt.client.map.render.MapRenderer;
import org.geomajas.puregwt.client.map.render.MapRendererFactory;
import org.geomajas.puregwt.client.map.render.MapRendererImpl;
import org.geomajas.puregwt.client.map.render.MapScalesRenderer;
import org.geomajas.puregwt.client.map.render.MapScalesRendererFactory;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.service.CommandServiceImpl;
import org.geomajas.puregwt.client.service.EndPointService;
import org.geomajas.puregwt.client.service.EndPointServiceImpl;
import org.geomajas.puregwt.client.widget.MapWidgetImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
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
		bind(DefaultMapGadgetFactory.class).to(DefaultMapGadgetFactoryImpl.class);
		install(new GinFactoryModuleBuilder().implement(Feature.class, FeatureImpl.class).build(FeatureFactory.class));
		install(new GinFactoryModuleBuilder().implement(MapEventParser.class, MapEventParserImpl.class).build(
				MapEventParserFactory.class));
		install(new GinFactoryModuleBuilder().implement(FeatureService.class, FeatureServiceImpl.class).build(
				FeatureServiceFactory.class));
		install(new GinFactoryModuleBuilder().implement(MapRenderer.class, MapRendererImpl.class).build(
				MapRendererFactory.class));
		install(new GinFactoryModuleBuilder().implement(new TypeLiteral<Layer<ClientVectorLayerInfo>>() {
		}, VectorLayer.class).build(VectorLayerFactory.class));
		install(new GinFactoryModuleBuilder().implement(new TypeLiteral<Layer<ClientRasterLayerInfo>>() {
		}, RasterLayer.class).build(RasterLayerFactory.class));
		install(new GinFactoryModuleBuilder().implement(MapScalesRenderer.class, LayerScalesRenderer.class).build(
				MapScalesRendererFactory.class));

		// Other:
		bind(CommandService.class).to(CommandServiceImpl.class).in(Singleton.class);
		bind(EndPointService.class).to(EndPointServiceImpl.class).in(Singleton.class);
		bind(GfxUtil.class).to(GfxUtilImpl.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}
}