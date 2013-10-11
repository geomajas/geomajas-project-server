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

package org.geomajas.gwt.client.action.layertree;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolId;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Registry for mapping between tool id's and LayerTree actions.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public final class LayerTreeRegistry {

	private static final Map<String, ToolCreator> REGISTRY;

	static {
		REGISTRY = new HashMap<String, ToolCreator>();
		REGISTRY.put(ToolId.TOOL_LAYER_VISIBLE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new LayerVisibleModalAction();
			}
		});
		REGISTRY.put(ToolId.TOOL_LAYER_SNAPPING, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return null;
			}
		});
		REGISTRY.put(ToolId.TOOL_LAYER_LABELLED, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new LayerLabeledModalAction();
			}
		});
		REGISTRY.put(ToolId.TOOL_LAYER_REFRESH, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new LayerRefreshAction(mapWidget);
			}
		});
	}

	private LayerTreeRegistry() {
		// utility class, hide constructor
	}

	/**
	 * Add another key to the registry. This will overwrite the previous value.
	 * 
	 * @param key
	 *            key for the toolbar actions
	 * @param toolCreator
	 *            toolbar action creator
	 */
	public static void put(String key, ToolCreator toolCreator) {
		if (null != key && null != toolCreator) {
			REGISTRY.put(key, toolCreator);
		}
	}

	/**
	 * Get the toolbar action which matches the given key.
	 * 
	 * @param key
	 *            key for toolbar action
	 * @param mapWidget
	 *            map which will contain this tool
	 * @return toolbar action or null when key not found
	 */
	public static ToolbarBaseAction getToolbarAction(String key, MapWidget mapWidget) {
		ToolCreator toolCreator = REGISTRY.get(key);
		if (null == toolCreator) {
			return null;
		}
		return toolCreator.createTool(mapWidget);
	}
}
