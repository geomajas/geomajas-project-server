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

package org.geomajas.puregwt.client.layer;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TMS test.
 * 
 * @author Pieter De Graef
 */
public class TmsLayerPanel extends ContentPanel {

	public TmsLayerPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "TMS layer test";
	}

	public String getDescription() {
		return "TMS layer test";
	}

	public Widget getContentWidget() {
		VerticalPanel layout = new VerticalPanel();

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(640, 480);

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapTms");
		return layout;
	}
}