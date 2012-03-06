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

import org.geomajas.puregwt.client.controller.MapEventParserFactory;
import org.geomajas.puregwt.client.controller.TestMapEventParserFactory;
import org.geomajas.puregwt.client.gfx.GfxUtil;
import org.geomajas.puregwt.client.gfx.GfxUtilImpl;
import org.geomajas.puregwt.client.map.LayersModel;
import org.geomajas.puregwt.client.map.LayersModelImpl;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.MapPresenterImpl;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.ViewPortImpl;
import org.geomajas.puregwt.client.map.feature.FeatureFactory;
import org.geomajas.puregwt.client.map.feature.FeatureServiceFactory;
import org.geomajas.puregwt.client.map.feature.TestFeatureFactory;
import org.geomajas.puregwt.client.map.feature.TestFeatureServiceFactory;
import org.geomajas.puregwt.client.map.render.MapRendererFactory;
import org.geomajas.puregwt.client.map.render.MapScalesRendererFactory;
import org.geomajas.puregwt.client.map.render.TestMapRendererFactory;
import org.geomajas.puregwt.client.map.render.TestMapScalesRendererFactory;
import org.geomajas.puregwt.client.widget.MapWidgetImpl;
import org.geomajas.puregwt.client.widget.MapWidgetTestImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
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
		bind(ViewPort.class).to(ViewPortImpl.class);
		bind(MapWidget.class).to(MapWidgetTestImpl.class);
		bind(FeatureFactory.class).to(TestFeatureFactory.class);
		bind(MapEventParserFactory.class).to(TestMapEventParserFactory.class);
		bind(FeatureServiceFactory.class).to(TestFeatureServiceFactory.class);
		bind(MapRendererFactory.class).to(TestMapRendererFactory.class);
		bind(MapScalesRendererFactory.class).to(TestMapScalesRendererFactory.class);

		// Other:
		bind(GfxUtil.class).to(GfxUtilImpl.class).in(Singleton.class);
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}


}