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

package org.geomajas.puregwt.widget.client.map;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Map gadget that displays a {@link LegendDropDown} in the top right corner or the map. All layers in the map's layers
 * model will be added to the drop down panel.
 * 
 * @author Pieter De Graef
 */
public class LegendDropDownWidget implements IsWidget {

	private MapPresenter mapPresenter;

	private LegendDropDown layout;

	private int closeDelay = 3000;

	private Timer timer;

	public LegendDropDownWidget(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		asWidget().getElement().getStyle().setTop(5, Unit.PX);
		asWidget().getElement().getStyle().setRight(5, Unit.PX);
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public Widget asWidget() {
		if (layout == null) {
			layout = new LegendDropDown(mapPresenter);
			if (mapPresenter.getLayersModel() == null || mapPresenter.getLayersModel().getLayerCount() == 0) {
				mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

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
			layout.addDomHandler(new MouseUpHandler() {

				public void onMouseUp(MouseUpEvent event) {
					event.stopPropagation();
				}
			}, MouseUpEvent.getType());
			layout.addDomHandler(new DoubleClickHandler() {

				public void onDoubleClick(DoubleClickEvent event) {
					event.stopPropagation();
				}
			}, DoubleClickEvent.getType());

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

	/**
	 * Apply a new title on the drop down button.
	 * 
	 * @param safeHtml
	 *            The title in the form of a trusted HTML string.
	 */
	public void setTitle(String safeHtml) {
		if (layout == null) {
			throw new IllegalStateException("Make sure this gadget has been added to the map before calling this"
					+ " method.");
		}
		layout.setTitle(safeHtml);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void addAllLayers() {
		for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
			Layer layer = mapPresenter.getLayersModel().getLayer(i);
			layout.addLayer(layer);
		}
	}
}