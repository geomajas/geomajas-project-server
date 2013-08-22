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
package org.geomajas.plugin.geocoder.puregwt.example.client.sample;

import org.geomajas.gwt.client.GeomajasGinjector;
import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.widget.MapLayoutPanel;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.plugin.geocoder.client.GeocoderWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class description.
 *
 * @author Dosi Bingov
 */
public class GeoCoderExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	private MapPresenter mapPresenter;

	private static final GeomajasGinjector GEOMAJASINJECTOR = GWT.create(GeomajasGinjector.class);

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@Override
	public Widget asWidget() {
		// return root layout element
		return rootElement;
	}

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 */
	interface FeatureSelectedExampleUiBinder extends
			UiBinder<DockLayoutPanel, GeoCoderExample> {

	}

	private static final FeatureSelectedExampleUiBinder UIBINDER = GWT.create(FeatureSelectedExampleUiBinder.class);

	public GeoCoderExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		// Create the MapPresenter
		mapPresenter = GEOMAJASINJECTOR.getMapPresenter();

		// Initialize the map
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		mapPresenter.initialize("geocoder-app", "mapOsm");
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");
		layout.setPresenter(mapPresenter);

		// create geocoder widget
		GeocoderWidget geocoder = new GeocoderWidget(mapPresenter);
		geocoder.getElement().getStyle().setTop(7, Style.Unit.PX);
		geocoder.getElement().getStyle().setLeft(100, Style.Unit.PX);

		// add geocoder widget to the map
		mapPresenter.getWidgetPane().add(geocoder);

		mapPanel.setWidget(resizeLayoutPanel);
	}

	/**
	 * private MapInitializationHandler to zoom in the map.
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			double scale = mapPresenter.getViewPort().getScale() * 6;
			mapPresenter.getViewPort().applyScale(scale);
		}
	}
}