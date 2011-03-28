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

package org.geomajas.widget.featureinfo.client.widget;

import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.feature.Feature;

import com.smartgwt.client.widgets.Window;

/**
 * 
 * @author Oliver May
 */
public class MultiLayerFeatureInfoTreeWindow extends Window {

	public MultiLayerFeatureInfoTreeWindow(MapWidget mapWidget, Map<String, List<Feature>> featureMap,
			boolean showDetailWindowInline) {
		// TODO Auto-generated constructor stub
		
		// make use of FeatureDetailWidgetFactory.createFeatureDetailCanvas/window(feature, false)
		// to create panes, so custom panels are used
	}

}