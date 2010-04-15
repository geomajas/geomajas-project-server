/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.action.layertree;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Registry for mapping between tool id's and LayerTree actions.
 * 
 * @author Pieter De Graef
 */
@Api(allMethods = true)
public final class LayerTreeRegistry {

	private static final Map<String, ToolCreator> REGISTRY;

	static {
		REGISTRY = new HashMap<String, ToolCreator>();
		REGISTRY.put("LayerVisibleTool", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new LayerVisibleModalAction();
			}
		});
		REGISTRY.put("LayerSnappingTool", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return null;
			}
		});
		REGISTRY.put("LayerLabeledTool", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new LayerLabeledModalAction();
			}
		});
		REGISTRY.put("ShowTableAction", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return null;
			}
		});
		REGISTRY.put("LayerRefreshAction", new ToolCreator() {

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
