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

package org.geomajas.widget.featureinfo.client.controller;

import java.util.HashMap;

import org.geomajas.gwt.client.controller.listener.ListenerController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.action.toolbar.MultiLayerFeatureInfoListener;

/**
 * Shows information of the features (per visible vector layer) near the
 * position where the user clicked on the map. First a list of all the
 * appropriate features is shown in a floating window. Clicking on a feature of
 * the list shows its attributes in a feature attribute window under the list
 * window. As a starting point for this class the
 * org.geomajas.gwt.client.controller.FeatureInfoController was used.
 * 
 * @author An Buyle
 * @author Oliver May
 * @author Kristof Heirwegh
 * @author Wout Swartenbroekx
 */
public class MultiLayerFeatureInfoController extends ListenerController {

	public MultiLayerFeatureInfoController(MapWidget mapWidget) {
		super(mapWidget, new MultiLayerFeatureInfoListener(mapWidget));
	}
	
	public MultiLayerFeatureInfoController(MapWidget mapWidget, int pixelTolerance) {
		this(mapWidget);
		setPixelTolerance(pixelTolerance);
	}

	public void setPixelTolerance(int pixelTolerance) {
		((MultiLayerFeatureInfoListener) getListener()).setPixelTolerance(pixelTolerance);
	}
	
	public void setLayersToExclude(String[] layerIds) {
		((MultiLayerFeatureInfoListener) getListener()).setLayersToExclude(layerIds);
	}

	public void setFeaturesListLabels(HashMap<String, String> featuresListLabels) {
		((MultiLayerFeatureInfoListener) getListener()).setFeaturesListLabels(featuresListLabels);
	}
}
