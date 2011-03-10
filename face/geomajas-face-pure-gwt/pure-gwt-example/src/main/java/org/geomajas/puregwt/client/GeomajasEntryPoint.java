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
import org.geomajas.puregwt.client.map.ScreenContainer;
import org.geomajas.puregwt.client.map.WorldContainer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.widget.MapWidgetImpl;
import org.vaadin.gwtgraphics.client.shape.Circle;

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
		final MapWidgetImpl mapWidget = injector.getMap();
		final MapPresenter mapPresenter = new MapPresenterImpl("pure-gwt-app", "mapOsm", mapWidget);
		mapPresenter.setSize(640, 480);

		Button initButton = new Button("Init", new ClickHandler() {

			public void onClick(ClickEvent event) {
				mapPresenter.initialize();
			}
		});
		Button opacity05Button = new Button("Opacity=0.5", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer("osmLayer");
				layer.setOpacity(0.5);
			}
		});
		Button opacity1Button = new Button("Opacity=1", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer("osmLayer");
				layer.setOpacity(1);
			}
		});
		Button hideButton = new Button("Hide", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer("osmLayer");
				layer.setMarkedAsVisible(false);
			}
		});
		Button showButton = new Button("Show", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer("osmLayer");
				layer.setMarkedAsVisible(true);
			}
		});
		Button layerOrderButton = new Button("Toggle order", new ClickHandler() {

			public void onClick(ClickEvent event) {
				RasterLayer layer = (RasterLayer) mapPresenter.getMapModel().getLayer(0);
				mapPresenter.getMapModel().moveLayerUp(layer);
			}
		});
		Button drawButton = new Button("Draw", new ClickHandler() {

			private boolean shown;

			public void onClick(ClickEvent event) {
				if (shown) {
					ScreenContainer container = mapPresenter.getScreenContainer("test-container");
					container.clear();
					WorldContainer wc1 = mapPresenter.getWorldContainer("world-container-1");
					wc1.clear();
					WorldContainer wc2 = mapPresenter.getWorldContainer("world-container-2");
					wc2.clear();
				} else {
					WorldContainer wc1 = mapPresenter.getWorldContainer("world-container-1");
					Circle c1 = new Circle(0, 0, 500000);
					c1.setFillColor("red");
					wc1.add(c1);

					WorldContainer wc2 = mapPresenter.getWorldContainer("world-container-2");
					wc2.setResizeChildren(false);
					Circle c2 = new Circle(900000, 900000, 50);
					c2.setFillColor("green");
					wc2.add(c2);

					ScreenContainer sc = mapPresenter.getScreenContainer("screen-container");
					Circle c3 = new Circle(100, 100, 30);
					c3.setFillColor("blue");
					sc.add(c3);
				}
				shown = !shown;
			}
		});

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(initButton);
		hPanel.add(opacity05Button);
		hPanel.add(opacity1Button);
		hPanel.add(hideButton);
		hPanel.add(showButton);
		hPanel.add(layerOrderButton);
		hPanel.add(drawButton);

		RootPanel.get().add(hPanel);

		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(mapWidget);
		RootPanel.get().add(decPanel);
	}
}