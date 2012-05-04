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

package org.geomajas.widget.featureinfo.client.action.toolbar;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Show a tooltip with the labels of features under mousepointer.
 * 
 * @author Kristof Heirwegh
 * @author Wout Swartenbroekx
 */
public class TooltipOnMouseoverModalAction extends ToolbarModalAction implements ConfigurableAction {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	
	private final MapWidget mapWidget;
	private final TooltipOnMouseoverListener listener;

	public TooltipOnMouseoverModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/mouse_tooltip.png", null);
		this.mapWidget = mapWidget;
		this.setTitle(messages.tooltipOnMouseoverActionTitle());
		this.setTooltip(messages.tooltipOnMouseoverActionTooltip());
		listener = new TooltipOnMouseoverListener(mapWidget);
	}

	public void onSelect(ClickEvent event) {
		mapWidget.addListener(listener);
	}

	public void onDeselect(ClickEvent event) {
		mapWidget.removeListener(listener);
	}

	public void configure(String key, String value) {
		if ("pixelTolerance".equals(key)) {
			setPixelTolerance(Integer.parseInt(value));
		}
		if ("showEmptyResult".equals(key)) {
			setShowEmptyResult(Boolean.parseBoolean(value));
		}
		if ("minimalMoveDistance".equals(key)) {
			setMinimalMoveDistance(Integer.parseInt(value));
		}
		if ("tooltipUseFeatureDetail".equals(key)) {
			setTooltipUseFeatureDetail(Boolean.parseBoolean(value));
		}
		if ("layersToExclude".equals(key)) {
			String[] layersToExcl = new String [0];
			if (null != value) {
				layersToExcl = value.split(",");
				for (int i = 0; i < layersToExcl.length; i++) {
					layersToExcl[i] = layersToExcl[i].trim();
				}
			}
			setLayersToExclude(layersToExcl);
		}
		if ("tooltipMaxLabelCount".equals(key)) {
			setTooltipMaxLabelCount(Integer.parseInt(value));
		}
	}

	/**
	 * Set the minimal distance the mouse must move before a new mouse over request is triggered.
	 * @param distance the minimal distance.
	 */
	private void setMinimalMoveDistance(int distance) {
		listener.setMinimalMoveDistance(distance);
	}

	/**
	 * Set if empty results should be displayed as "no results found" or be omitted.
	 * @param show true if empty results should be shown.
	 */
	private void setShowEmptyResult(boolean show) {
		listener.setShowEmptyResult(show);
	}

	/**
	 * Set the tolerance in pixels, when clicking on the map, the features in this distance around the mouse click will 
	 * be returned.
	 * @param pixelTolerance distance in pixels
	 */
	public void setPixelTolerance(int pixelTolerance) {
		listener.setPixelTolerance(pixelTolerance);
	}
	
	/**
	 * Set the maximum amount of features for which to draw all attributes
	 * @param tooltipMaxLabelCount
	 */
	private void setTooltipMaxLabelCount(int tooltipMaxLabelCount) {
		listener.setTooltipMaxLabelCount(tooltipMaxLabelCount);
	}

	/**
	 * Set which layers not to be used for composing the tooltip
	 * @param layerIds
	 */
	private void setLayersToExclude(String[] layerIds) {
		listener.setLayersToExclude(layerIds);
		
	}
	/**
	 * Set true to compose the tooltip of feature details instead of label
	 * @param useFeatureDetail
	 */
	private void setTooltipUseFeatureDetail(boolean useFeatureDetail) {
		listener.setTooltipUseFeatureDetail(useFeatureDetail);
	}
}