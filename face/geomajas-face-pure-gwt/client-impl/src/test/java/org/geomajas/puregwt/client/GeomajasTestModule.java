/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.map.LayersModel;
import org.geomajas.puregwt.client.map.LayersModelImpl;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.MapPresenterImpl;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.ViewPortImpl;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.EventBusImpl;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.GeometryFactoryImpl;
import org.geomajas.puregwt.client.spatial.MathService;
import org.geomajas.puregwt.client.spatial.MathServiceImpl;
import org.geomajas.puregwt.client.widget.MapWidgetTestImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Module for testing.
 * 
 * @author Jan De Moerloose
 */
public class GeomajasTestModule extends AbstractModule {

	@Override
	protected void configure() {
		// Spatial services:
		bind(MathService.class).to(MathServiceImpl.class).in(Singleton.class);
		bind(GeometryFactory.class).to(GeometryFactoryImpl.class).in(Singleton.class);

		// Map related interfaces:
		bind(MapPresenter.class).to(MapPresenterImpl.class);
		bind(LayersModel.class).to(LayersModelImpl.class);
		bind(ViewPort.class).to(ViewPortImpl.class);
		bind(EventBus.class).to(EventBusImpl.class);
		
		bind(MapWidget.class).to(MapWidgetTestImpl.class);
	}
}