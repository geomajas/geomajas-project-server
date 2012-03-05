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

import com.google.gwt.layout.client.Layout.Alignment;
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
	// Private methods:
	// ------------------------------------------------------------------------

	private void addAllLayers() {
		for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
			Layer<?> layer = mapPresenter.getLayersModel().getLayer(i);
			layout.addLayer(layer);
		}
	}
}