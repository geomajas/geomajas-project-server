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

package org.geomajas.puregwt.client.feature;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.controller.FeatureSelectionController;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates a feature selection controller.
 * 
 * @author Pieter De Graef
 */
public class FeatureSelectionPanel extends ContentPanel {

	private MapPresenter mapPresenter;

	public String getTitle() {
		return "Feature Selection";
	}

	public String getDescription() {
		return "This example demonstrates the use of the FeatureSelectionController. This controller allows the user "
				+ "to select features on the map.";
	}

	public Widget getContentWidget() {
		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addHandler(MapInitializationEvent.TYPE, new MyMapInitializationHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("pure-gwt-app", "mapCountries");
		return layout;
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			double scale = mapPresenter.getViewPort().getScale() * 4;
			mapPresenter.getViewPort().applyScale(scale);

			mapPresenter.getLayersModel().getLayer(1).setMarkedAsVisible(true);
			mapPresenter.setMapController(new FeatureSelectionController());
		}
	}
}