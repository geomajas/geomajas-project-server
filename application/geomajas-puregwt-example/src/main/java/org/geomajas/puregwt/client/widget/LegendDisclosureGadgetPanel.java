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

package org.geomajas.puregwt.client.widget;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.widget.client.map.LegendDropDownWidget;

import com.google.gwt.user.client.ui.Widget;

/**
 * Showcase that show the layer legend view.
 * 
 * @author Pieter De Graef
 */
public class LegendDisclosureGadgetPanel extends ContentPanel {

	/**
	 * Constructor.
	 *
	 * @param mapPresenter map presenter
	 */
	public LegendDisclosureGadgetPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public String getTitle() {
		return "Legend MapGadget";
	}

	@Override
	public String getDescription() {
		return "Showcase that show the layer legend view.";
	}

	@Override
	public Widget getContentWidget() {
		// Create the MapPresenter:
		mapPresenter.initialize("puregwt-app", "mapLegend");
		MapLayoutPanel mapLayoutPanel = new MapLayoutPanel(mapPresenter);

		LegendDropDownWidget legendWidget = new LegendDropDownWidget(mapPresenter);
		mapPresenter.getWidgetPane().add(legendWidget);
		mapLayoutPanel.setSize("600px", "400px");

		return mapLayoutPanel.asWidget();
	}
}