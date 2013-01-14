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
package org.geomajas.widget.featureinfo.client;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.action.toolbar.CombinedFeatureInfoModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.MultiLayerFeatureInfoModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.ShowCoordinatesModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.TooltipOnMouseoverModalAction;
import org.geomajas.widget.featureinfo.client.widget.builders.UrlFeatureDetailWidgetBuilder;
import org.geomajas.widget.featureinfo.client.widget.factory.WidgetFactory;

import com.google.gwt.core.client.EntryPoint;

/**
 * Initializes the featureInfo plugin.
 * 
 * @author An Buyle
 * @author Oliver May
 * @author Wout Swartenbroekx
 */
public class FeatureInfoEntryPoint implements EntryPoint {

	public static final String SHOW_FEATUREINFOMODE_KEY = "FeatureInfoMode";

	public static final String SHOW_COMBINEDFEATUREINFOMODE_KEY = "CombinedFeatureInfoMode";

	public static final String SHOW_DEFAULT_MULTILAYERFEATUREINFO_KEY = "MultilayerFeatureInfoMode";

	public static final String SHOW_COORDINATES_MODE_KEY = "ShowCoordinatesMode";
	
	public static final String TOOLTIP_ON_MOUSEOVER_MODE_KEY = "TooltipOnMouseOverMode";
	

	public void onModuleLoad() {
		ToolbarRegistry.put(SHOW_DEFAULT_MULTILAYERFEATUREINFO_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new MultiLayerFeatureInfoModalAction(mapWidget);
			}
		});

		ToolbarRegistry.put(SHOW_COORDINATES_MODE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ShowCoordinatesModalAction(mapWidget);
			}
		});

		ToolbarRegistry.put(TOOLTIP_ON_MOUSEOVER_MODE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new TooltipOnMouseoverModalAction(mapWidget);
			}
		});

		ToolbarRegistry.put(SHOW_COMBINEDFEATUREINFOMODE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new CombinedFeatureInfoModalAction(mapWidget);
			}
		});

		// ----------------------------------------------------------

		WidgetFactory.put(UrlFeatureDetailWidgetBuilder.IDENTIFIER, new UrlFeatureDetailWidgetBuilder());

	}
}
