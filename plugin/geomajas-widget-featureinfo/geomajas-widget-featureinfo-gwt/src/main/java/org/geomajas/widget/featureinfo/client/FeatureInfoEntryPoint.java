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
package org.geomajas.widget.featureinfo.client;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.action.toolbar.NearbyFeaturesModalAction;

import com.google.gwt.core.client.EntryPoint;
//import com.smartgwt.client.util.SC;

/**
 * Initializes the featureInfo plugin.
 *
 * @author An Buyle
 *
 */
public class FeatureInfoEntryPoint implements EntryPoint {

	public static final String SHOW_DEFAULT_FEATUREINFOALLLAYERSMODE_KEY = "NearbyFeaturesMode";

	public void onModuleLoad() {
		//SC.showConsole(); // Crash if console is closed
		//SC.say("Registering toolbar tool " + SHOW_DEFAULT_FEATUREINFOALLLAYERSMODE_KEY);
		// ---------------------------------------------------------------------
		// Register extra button
		// ---------------------------------------------------------------------
		ToolbarRegistry.put(SHOW_DEFAULT_FEATUREINFOALLLAYERSMODE_KEY, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new NearbyFeaturesModalAction(mapWidget);
			}
		});

	}

}
