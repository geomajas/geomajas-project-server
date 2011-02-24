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
import org.geomajas.widget.featureinfo.client.controller.MultiLayerFeatureInfoController;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Tool which shows a list of nearby features. By clicking on a feature in the list details of that feature will be
 * shown.
 * 
 * @author An Buyle
 * @author Oliver May
 */
public class MultiLayerFeatureInfoModalAction extends ToolbarModalAction implements ConfigurableAction {

	private MapWidget mapWidget;

	private MultiLayerFeatureInfoController controller;

	/**
	 * Number of pixels that describes the tolerance allowed when searching nearby features.
	 */
	private int pixelTolerance = 10; /* default value */

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);

	/**
	 * The way the feature list is displayed.
	 */
	private MultiLayerFeatureInfoRepresentationType representationType = MultiLayerFeatureInfoRepresentationType.FLAT;

	/**
	 * Whether to show the per feature details inline. The default is to show in a separate window.
	 */
	private boolean showDetailWindowInline;

	// Constructor:

	public MultiLayerFeatureInfoModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/info.png"/* TODO: icon */, null);
		setTooltip(messages.nearbyFeaturesModalActionTooltip());
		this.mapWidget = mapWidget;
		controller = new MultiLayerFeatureInfoController(mapWidget, pixelTolerance, showDetailWindowInline,
				representationType);
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

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
		controller.setPixelTolerance(pixelTolerance);
		setTooltip(getTooltip() + " pixeltolerance: " + pixelTolerance);
	}

	public void setRepresentationType(MultiLayerFeatureInfoRepresentationType representationType) {
		this.representationType = representationType;
		controller.setRepresentationType(representationType);
		setTooltip(getTooltip() + " representationType: " + representationType);
	}

	public void setShowDetailWindowInline(boolean showDetailWindowInline) {
		this.showDetailWindowInline = showDetailWindowInline;
		controller.setShowDetailWindowInline(showDetailWindowInline);
		setTooltip(getTooltip() + " detailwindowinline: " + showDetailWindowInline);
	}

	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public MultiLayerFeatureInfoRepresentationType getRepresentationType() {
		return representationType;
	}

	public boolean isShowDetailWindowInline() {
		return showDetailWindowInline;
	}

	// configure
	public void configure(String key, String value) {
		if ("pixelTolerance".equals(key)) {
			setPixelTolerance(Integer.parseInt(value));
		} else if ("representationType".equals(key)) {
			setRepresentationType(MultiLayerFeatureInfoRepresentationType.valueOf(value));
		} else if ("showDetailWindowInline".equals(key)) {
			setShowDetailWindowInline(Boolean.parseBoolean(value));
		}
	}

}
