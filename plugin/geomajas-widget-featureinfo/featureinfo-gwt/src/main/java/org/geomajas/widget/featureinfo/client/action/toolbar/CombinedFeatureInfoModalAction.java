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
package org.geomajas.widget.featureinfo.client.action.toolbar;

import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.controller.CombinedFeatureInfoController;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Combination of Pan, MultiLayerFeatureInfoModalAction and TooltipOnMouseover actions.
 *
 * @author Kristof Heirwegh
 */
public class CombinedFeatureInfoModalAction extends ToolbarModalAction {

	private final MapWidget mapWidget;

	private final CombinedFeatureInfoController controller;

	private static final FeatureInfoMessages MESSAGES = GWT.create(FeatureInfoMessages.class);

	// Constructor:

	public CombinedFeatureInfoModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/pan_info.png", null);
		setTitle(MESSAGES.combinedFeatureInfoActionTitle());
		setTooltip(MESSAGES.combinedFeatureInfoActionTooltip());
		this.mapWidget = mapWidget;
		controller = new CombinedFeatureInfoController(mapWidget);

		// if it is available it is the default:
		// TODO: what is the impact of setting this default controller? 
		mapWidget.setFallbackController(controller);
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
}
