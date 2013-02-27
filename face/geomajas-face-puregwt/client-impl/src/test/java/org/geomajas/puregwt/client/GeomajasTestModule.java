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

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.controller.MapEventParserFactory;
import org.geomajas.puregwt.client.controller.MockMapEventParserFactory;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.GfxUtilImpl;
import org.geomajas.puregwt.client.map.DefaultMapGadgetFactory;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.MapPresenterImpl;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.ViewPortImpl;
import org.geomajas.puregwt.client.map.feature.FeatureFactory;
import org.geomajas.puregwt.client.map.feature.FeatureServiceFactory;
import org.geomajas.puregwt.client.map.feature.MockFeatureFactory;
import org.geomajas.puregwt.client.map.feature.MockFeatureServiceFactory;
import org.geomajas.puregwt.client.map.gadget.MockDefaultMapGadgetFactory;
import org.geomajas.puregwt.client.map.layer.LayerFactory;
import org.geomajas.puregwt.client.map.layer.LayersModel;
import org.geomajas.puregwt.client.map.layer.LayersModelImpl;
import org.geomajas.puregwt.client.map.layer.RasterServerLayer;
import org.geomajas.puregwt.client.map.layer.RasterServerLayerImpl;
import org.geomajas.puregwt.client.map.layer.VectorServerLayer;
import org.geomajas.puregwt.client.map.layer.VectorServerLayerImpl;
import org.geomajas.puregwt.client.map.render.MapRendererFactory;
import org.geomajas.puregwt.client.map.render.MapScalesRendererFactory;
import org.geomajas.puregwt.client.map.render.MockMapRendererFactory;
import org.geomajas.puregwt.client.map.render.MockMapScalesRendererFactory;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.service.EndPointService;
import org.geomajas.puregwt.client.service.EndPointServiceImpl;
import org.geomajas.puregwt.client.service.MockCommandService;
import org.geomajas.puregwt.client.widget.MapWidgetTestImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Module for testing.
 * 
 * @author Jan De Moerloose
 */
public class GeomajasTestModule extends AbstractModule {

	@Override
	protected void configure() {
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
		bind(MapScalesRendererFactory.class).to(MockMapScalesRendererFactory.class);
		bind(DefaultMapGadgetFactory.class).to(MockDefaultMapGadgetFactory.class);

		// Other:
		bind(GfxUtil.class).to(GfxUtilImpl.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(CommandService.class).to(MockCommandService.class).in(Singleton.class);
		bind(EndPointService.class).to(EndPointServiceImpl.class).in(Singleton.class);
	}
}