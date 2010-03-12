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

package org.geomajas.gwt.client.action.toolbar;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Registry for mapping between tool id's and toolbar actions.
 * 
 * @author Joachim Van der Auwera
 */
public final class ToolbarRegistry {

	private static final Map<String, ToolCreator> REGISTRY;

	static {
		REGISTRY = new HashMap<String, ToolCreator>();
		REGISTRY.put("EditMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new EditingModalAction(mapWidget);
			}
		});
		REGISTRY.put("MeasureDistanceMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new MeasureModalAction(mapWidget);
			}
		});
		REGISTRY.put("SelectionMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new SelectionModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomIn", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomInAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomOut", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomOutAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomInMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomInModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomOutMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomOutModalAction(mapWidget);
			}
		});
		REGISTRY.put("PanMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PanModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomToRectangleMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToRectangleModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomNext", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomNextAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomPrevious", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomPreviousAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomToSelection", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToSelectionAction(mapWidget);
			}
		});
		REGISTRY.put("PanToSelection", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PanToSelectionAction(mapWidget);
			}
		});
		REGISTRY.put("FeatureInfoMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FeatureInfoModalAction(mapWidget);
			}
		});
	}

	private ToolbarRegistry() {
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
