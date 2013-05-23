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

import java.util.HashMap;
import java.util.Map;

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
 * @author Wout Swartenbroekx
 */
public class MultiLayerFeatureInfoModalAction extends ToolbarModalAction implements ConfigurableAction {

	private static final FeatureInfoMessages MESSAGES = GWT.create(FeatureInfoMessages.class);

	private final MapWidget mapWidget;

	private final MultiLayerFeatureInfoController controller;

	/**
	 * Number of pixels that describes the tolerance allowed when searching nearby features.
	 */
	private int pixelTolerance = 10; /* default value */
	private String[] layersToExclude = new String[0];
	private Map<String, String> featuresListLabels = new HashMap<String, String>();

	/**
	 * Constructor.
	 * @param mapWidget the mapwidget where this action should work on
	 */
	public MultiLayerFeatureInfoModalAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/info.png", null);
		setTitle(MESSAGES.nearbyFeaturesModalActionTitle());
		setTooltip(MESSAGES.nearbyFeaturesModalActionTooltip());
		this.mapWidget = mapWidget;
		controller = new MultiLayerFeatureInfoController(mapWidget, pixelTolerance);
		if (null != layersToExclude) {
			controller.setLayersToExclude(layersToExclude);
		}
		if (null != featuresListLabels) {
			controller.setFeaturesListLabels(featuresListLabels);
		}
	}

	@Override
	public void configure(String key, String value) {
		if ("pixelTolerance".equals(key)) {
			setPixelTolerance(Integer.parseInt(value));
		} else if ("layersToExclude".equals(key) && null != value) {
			String[] layersToExcl = value.split(",");
			for (int i = 0; i < layersToExcl.length; i++) {
				layersToExcl[i] = layersToExcl[i].trim();
			}
			setLayersToExclude(layersToExcl);
		} else if ("featuresListLabels".equalsIgnoreCase(key) && null != value) {
			String [] features = value.split(",");
			for (String feature : features) {
				if (feature.indexOf((char) 61) > -1) { //String.indexOf(char) faster than String.indexOf(String)
					featuresListLabels.put(feature.split("=")[0], feature.split("=")[1]);
				}
			}
		}
	}

	@Override
	public void onSelect(ClickEvent event) {
		mapWidget.setController(controller);
	}

	@Override
	public void onDeselect(ClickEvent event) {
		mapWidget.setController(null);
	}

	/**
	 * Set the tolerance in pixels, when clicking on the map, the features in this distance around the mouse click will 
	 * be returned.
	 * @param pixelTolerance distance in pixels
	 */
	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
		controller.setPixelTolerance(pixelTolerance);
	}

	/**
	 * Get pixel tolerance.
	 *
	 * @return the current pixel tolerance
	 */
	public int getPixelTolerance() {
		return pixelTolerance;
	}
	
	/**
	 * Set the layers that should be excluded from the query.
	 *
	 * @param layerIds list of layerIds
	 */
	public void setLayersToExclude(String[] layerIds) {
		this.layersToExclude = layerIds;
		controller.setLayersToExclude(layerIds);
	}

	/**
	 * Set labels for feature lists.
	 *
	 * @param featuresListLabels the featuresListLabels to set
	 */
	public void setFeaturesListLabels(Map<String, String> featuresListLabels) {
		this.featuresListLabels = featuresListLabels;
		controller.setFeaturesListLabels(featuresListLabels);
	}
}
