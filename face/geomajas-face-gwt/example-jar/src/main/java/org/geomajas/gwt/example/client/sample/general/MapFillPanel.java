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

package org.geomajas.gwt.example.client.sample.general;

import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.widget.MapLayoutPanel;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.client.ExampleJar;

import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates how to let the map fill up an area on the page.
 * 
 * @author Pieter De Graef
 */
public class MapFillPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	public Widget asWidget() {
		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");

		// Create the MapPresenter and add to the layout:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		layout.setPresenter(mapPresenter);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("gwt-app", "mapOsm");
		return resizeLayoutPanel;
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			double scale = mapPresenter.getViewPort().getScale() * 8;
			mapPresenter.getViewPort().applyScale(scale);
		}
	}
}