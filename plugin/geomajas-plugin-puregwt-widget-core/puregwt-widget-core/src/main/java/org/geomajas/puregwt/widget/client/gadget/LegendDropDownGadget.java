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

package org.geomajas.puregwt.widget.client.gadget;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.widget.client.map.LegendDropDown;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that displays a {@link LegendDropDown} in the top right corner or the map. All layers in the map's layers
 * model will be added to the drop down panel.
 * 
 * @author Pieter De Graef
 */
public class LegendDropDownGadget implements MapGadget {

	private MapPresenter mapPresenter;

	private LegendDropDown layout;

	private int closeDelay = 3000;

	private Timer timer;

	public Widget asWidget() {
		if (layout == null) {
			layout = new LegendDropDown(mapPresenter);
			if (mapPresenter.getLayersModel().getLayerCount() == 0) {
				mapPresenter.getEventBus().addHandler(MapInitializationEvent.TYPE, new MapInitializationHandler() {

					public void onMapInitialized(MapInitializationEvent event) {
						addAllLayers();
					}
				});
			} else {
				addAllLayers();
			}

			// Stop propagation of mouse events to the map:
			layout.addDomHandler(new MouseDownHandler() {

				public void onMouseDown(MouseDownEvent event) {
					event.stopPropagation();
				}
			}, MouseDownEvent.getType());
			layout.addDomHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					event.stopPropagation();
				}
			}, ClickEvent.getType());

			// Install a timer for automatic closing when the mouse leaves us:
			layout.addDomHandler(new MouseOutHandler() {

				public void onMouseOut(MouseOutEvent event) {
					if (layout.isOpen() && closeDelay > 0) {
						timer = new Timer() {

							public void run() {
								layout.setOpen(false);
							}
						};
						timer.schedule(closeDelay);
					}
				}
			}, MouseOutEvent.getType());
			layout.addDomHandler(new MouseOverHandler() {

				public void onMouseOver(MouseOverEvent event) {
					if (timer != null) {
						timer.cancel();
					}
				}
			}, MouseOverEvent.getType());
		}
		return layout;
	}

	public void beforeDraw(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	public Alignment getHorizontalAlignment() {
		return Alignment.END;
	}

	public Alignment getVerticalAlignment() {
		return Alignment.BEGIN;
	}

	public int getHorizontalMargin() {
		return 5;
	}

	public int getVerticalMargin() {
		return 5;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the number of milliseconds this drop down will wait before automatically closing when the mouse moves out of
	 * view. If the mouse comes back within the delay time, the drop down will stay open.
	 * 
	 * @return The number of milliseconds this drop down will wait before automatically closing.
	 */
	public int getCloseDelay() {
		return closeDelay;
	}

	/**
	 * Set the number of milliseconds this drop down will wait before automatically closing when the mouse moves out of
	 * view. If the mouse comes back within the delay time, the drop down will stay open.
	 * 
	 * @param closeDelay
	 *            The automatic closing delay, expressed in milliseconds.
	 */
	public void setCloseDelay(int closeDelay) {
		this.closeDelay = closeDelay;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void addAllLayers() {
		for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
			Layer<?> layer = mapPresenter.getLayersModel().getLayer(i);
			layout.addLayer(layer);
		}
	}
}