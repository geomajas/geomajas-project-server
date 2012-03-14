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

package org.geomajas.puregwt.client.widget;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.widget.client.map.LayerLegendPanel;
import org.geomajas.puregwt.widget.client.map.ResizableMapLayout;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Showcase that show the layer legend view.
 * 
 * @author Pieter De Graef
 */
public class LayerLegendViewPanel extends ContentPanel {

	private VerticalPanel layerPanel;

	public LayerLegendViewPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "Legend";
	}

	public String getDescription() {
		return "Showcase that show the layer legend view.";
	}

	public Widget getContentWidget() {
		DockLayoutPanel layout = new DockLayoutPanel(Unit.PX);
		layout.setSize("100%", "100%");

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		layerPanel = new VerticalPanel();
		scrollPanel.add(layerPanel);
		layout.addWest(scrollPanel, 300);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			public void onMapInitialized(MapInitializationEvent event) {
				for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
					Layer<?> layer = mapPresenter.getLayersModel().getLayer(i);
					layerPanel.add(new LayerLegendPanel(mapPresenter.getEventBus(), layer));
				}
			}
		});

		// Define the whole layout:
		ResizableMapLayout mapDecorator = new ResizableMapLayout(mapPresenter);
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("pure-gwt-app", "mapLegend");
		return layout;
	}
}