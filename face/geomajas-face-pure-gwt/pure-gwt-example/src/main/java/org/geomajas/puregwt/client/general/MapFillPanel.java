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

package org.geomajas.puregwt.client.general;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates how to let the map fill up an area on the page.
 * 
 * @author Pieter De Graef
 */
public class MapFillPanel extends ContentPanel {

	private MapPresenter mapPresenter;

	public String getTitle() {
		return "Full screen map";
	}

	public String getDescription() {
		return "This example shows how to let the map fill up an area on the page.";
	}

	public Widget getContentWidget() {
		// Define the left layout:
		final DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
		layout.setSize("100%", "100%");

		// Create the MapPresenter and add to the layout:
		mapPresenter = getInjector().getMapPresenter().get();
		mapPresenter.getEventBus().addHandler(MapInitializationEvent.TYPE, new MyMapInitializationHandler());
		layout.add(mapPresenter.asWidget());

		// Add an automatic resize handler to set the correct size when the window resizes:
		Window.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				mapPresenter.setSize(layout.getOffsetWidth(), layout.getOffsetHeight());
			}
		});

		// Calculate the correct size on load:
		layout.addAttachHandler(new AttachEvent.Handler() {

			public void onAttachOrDetach(AttachEvent event) {
				Timer timer = new Timer() {

					@Override
					public void run() {
						int width = layout.getOffsetWidth();
						int height = layout.getOffsetHeight();
						if (width != 0 && height != 0) {
							mapPresenter.setSize(width, height);
						} else {
							schedule(50);
						}
					}
				};
				timer.run();
			}
		});

		// Initialize the map, and return the layout:
		mapPresenter.initialize("pure-gwt-app", "mapOsm");
		return layout;
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