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
import org.geomajas.widget.featureinfo.client.action.toolbar.FeatureInfoModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.MultiLayerFeatureInfoModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.MultiLayerFeatureInfoRepresentationType;
import org.geomajas.widget.featureinfo.client.action.toolbar.ShowCoordinatesModalAction;
import org.geomajas.widget.featureinfo.client.action.toolbar.TooltipOnMouseoverModalAction;

import com.google.gwt.core.client.EntryPoint;

/**
 * Initializes the featureInfo plugin.
 * 
 * @author An Buyle
 * @author Oliver May
 * 
 */
public class FeatureInfoEntryPoint implements EntryPoint {

	private static final String SHOW_FEATUREINFOMODE_KEY = "FeatureInfoMode";
	
	private static final String SHOW_DEFAULT_MULTILAYERFEATUREINFO_KEY = "MultilayerFeatureInfoMode";

	private static final String SHOW_DEFAULT_MULTILAYERFEATUREINFOTREE_KEY = "MultilayerFeatureInfoTreeMode";

	private static final String SHOW_DEFAULT_MULTILAYERFEATUREINFOFULLTREE_KEY = "MultilayerFeatureInfoFullTreeMode";

	private static final String SHOW_DEFAULT_MULTILAYERFEATUREINFO_INLINE_KEY = "MultilayerFeatureInfoInlineMode";

	private static final String SHOW_DEFAULT_MULTILAYERFEATUREINFOTREE_INLINE_KEY = 
		"MultilayerFeatureInfoTreeInlineMode";

	public static final String SHOW_COORDINATES_MODE_KEY = "ShowCoordinatesMode";
	
	public static final String TOOLTIP_ON_MOUSEOVER_MODE_KEY = "TooltipOnMouseOverMode";
	

	public void onModuleLoad() {
		// SC.showConsole(); // Crash if console is closed
		// SC.say("Registering toolbar tool " + SHOW_DEFAULT_FEATUREINFOALLLAYERSMODE_KEY);
		// ---------------------------------------------------------------------
		// Register extra button
		// ---------------------------------------------------------------------
		
		ToolbarRegistry.put(SHOW_DEFAULT_MULTILAYERFEATUREINFO_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new MultiLayerFeatureInfoModalAction(mapWidget);
			}
		});
		ToolbarRegistry.put(SHOW_DEFAULT_MULTILAYERFEATUREINFOTREE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				MultiLayerFeatureInfoModalAction action = new MultiLayerFeatureInfoModalAction(mapWidget);
				action.setRepresentationType(MultiLayerFeatureInfoRepresentationType.TREE);
				return action;
			}
		});
		ToolbarRegistry.put(SHOW_DEFAULT_MULTILAYERFEATUREINFOFULLTREE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				MultiLayerFeatureInfoModalAction action = new MultiLayerFeatureInfoModalAction(mapWidget);
				action.setRepresentationType(MultiLayerFeatureInfoRepresentationType.TREE_FULL);
				return action;
			}
		});
		ToolbarRegistry.put(SHOW_DEFAULT_MULTILAYERFEATUREINFO_INLINE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				MultiLayerFeatureInfoModalAction action = new MultiLayerFeatureInfoModalAction(mapWidget);
				action.setShowDetailWindowInline(true);
				return action;
			}
		});
		ToolbarRegistry.put(SHOW_DEFAULT_MULTILAYERFEATUREINFOTREE_INLINE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				MultiLayerFeatureInfoModalAction action = new MultiLayerFeatureInfoModalAction(mapWidget);
				action.setRepresentationType(MultiLayerFeatureInfoRepresentationType.TREE);
				action.setShowDetailWindowInline(true);
				return action;
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

		ToolbarRegistry.put(SHOW_FEATUREINFOMODE_KEY, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FeatureInfoModalAction(mapWidget);
			}
		});
	}
}
