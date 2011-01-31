/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.featureinfo.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.controller.FeatureInfoAllLayersController;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Tool which shows a list of nearby features. By clicking on a feature in the list
 * details of that feature will be shown
 *
 * @author An Buyle
 */
public class NearbyFeaturesModalAction extends ToolbarModalAction  implements ConfigurableAction {

	private MapWidget mapWidget;

	private FeatureInfoAllLayersController controller;

	/** Number of pixels that describes the tolerance allowed when searching nearby features. */
	private int pixelTolerance = 10; /* default value */
	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	
	// Constructor:

	public NearbyFeaturesModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/info.png"/* TODO: icon*/, null);
		setTooltip(messages.nearbyFeaturesModalActionTooltip());
		this.mapWidget = mapWidget;
		controller = new FeatureInfoAllLayersController(mapWidget, pixelTolerance);
	}

	// ToolbarModalAction implementation:

	@Override
	public void onSelect(ClickEvent event) {
		mapWidget.setController(controller);
	}

	@Override
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}

	// Getters and setters:

	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
		controller.setPixelTolerance(pixelTolerance);
	}
	
	// configure  
	public void configure(String key, String value) {
		if ("pixelTolerance".equals(key)) {
			pixelTolerance = Integer.parseInt(value);
			controller.setPixelTolerance(pixelTolerance);
		}
	} 

}
