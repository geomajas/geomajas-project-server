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

import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.MapPresenterImpl;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.widget.MapWidgetImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point and main class for pure GWT example application.
 * 
 * @author Pieter De Graef
 */
public class GeomajasEntryPoint implements EntryPoint {

	private final PureGwtExampleGinjector injector = GWT.create(PureGwtExampleGinjector.class);

	public void onModuleLoad() {
		MapWidgetImpl mapWidget = injector.getMap();
		final MapPresenter mapPresenter = new MapPresenterImpl("pure-gwt-app", "mapOsm", mapWidget);
		mapPresenter.setSize(640, 480);

		Button initButton = new Button("Init", new ClickHandler() {

			public void onClick(ClickEvent event) {
				mapPresenter.initialize();
			}
		});
		Button opacity05Button = new Button("Opacity=0.5", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer(0);
				layer.setOpacity(0.5);
			}
		});
		Button opacity1Button = new Button("Opacity=1", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer(0);
				layer.setOpacity(1);
			}
		});
		Button hideButton = new Button("Hide", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer(0);
				layer.setMarkedAsVisible(false);
			}
		});
		Button showButton = new Button("Show", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer(0);
				layer.setMarkedAsVisible(true);
			}
		});

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(initButton);
		hPanel.add(opacity05Button);
		hPanel.add(opacity1Button);
		hPanel.add(hideButton);
		hPanel.add(showButton);

		RootPanel.get().add(hPanel);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(mapWidget);
		RootPanel.get().add(decPanel);
	}
}