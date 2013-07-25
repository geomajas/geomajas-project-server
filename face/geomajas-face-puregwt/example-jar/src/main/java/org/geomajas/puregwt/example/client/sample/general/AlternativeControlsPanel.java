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

package org.geomajas.puregwt.example.client.sample.general;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.widget.MapLayoutPanel;
import org.geomajas.puregwt.client.widget.control.pan.PanControl;
import org.geomajas.puregwt.client.widget.control.zoom.ZoomControl;
import org.geomajas.puregwt.client.widget.control.zoom.ZoomStepControl;
import org.geomajas.puregwt.example.base.client.ExampleBase;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;

import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates alternative controls on the map.
 * 
 * @author Pieter De Graef
 */
public class AlternativeControlsPanel implements SamplePanel {

	private MapPresenter mapPresenter;

	public Widget asWidget() {
		// Define the layout:
		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		final MapLayoutPanel layout = new MapLayoutPanel();
		resizeLayoutPanel.setWidget(layout);
		resizeLayoutPanel.setSize("100%", "100%");

		// Create the MapPresenter and add to the layout:
		mapPresenter = ExampleBase.getInjector().getMapPresenter();
		layout.setPresenter(mapPresenter);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapOsm");

		// Install alternative controls on the map - when the map has been initialized:
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		return resizeLayoutPanel;
	}

	/**
	 * Initialization handler that deletes the default zoom control and adds 2 new controls.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		@Override
		public void onMapInitialized(MapInitializationEvent event) {
			// Search for the ZoomControl widget and remove it:
			for (int i = 0; i < mapPresenter.getWidgetPane().getWidgetCount(); i++) {
				Widget widget = mapPresenter.getWidgetPane().getWidget(i);
				if (widget instanceof ZoomControl) {
					mapPresenter.getWidgetPane().remove(i);
				}
			}

			// Now add the alternative controls:
			mapPresenter.getWidgetPane().add(new PanControl(mapPresenter), 5, 5);
			mapPresenter.getWidgetPane().add(new ZoomStepControl(mapPresenter), 17, 60);
		}
	}
}