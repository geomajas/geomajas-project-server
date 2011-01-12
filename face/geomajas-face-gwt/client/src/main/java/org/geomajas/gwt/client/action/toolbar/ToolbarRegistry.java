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

package org.geomajas.gwt.client.action.toolbar;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Registry for mapping between tool id's and toolbar actions.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public final class ToolbarRegistry {

	private static final Map<String, ToolCreator> REGISTRY;

	static {
		REGISTRY = new HashMap<String, ToolCreator>();
		// Please keep registrations in aphabetical order
		REGISTRY.put("EditMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new EditingModalAction(mapWidget);
			}
		});
		REGISTRY.put("FeatureInfoMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FeatureInfoModalAction(mapWidget);
			}
		});
		REGISTRY.put("MeasureDistanceMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new MeasureModalAction(mapWidget);
			}
		});
		REGISTRY.put("PanMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PanModalAction(mapWidget);
			}
		});
		REGISTRY.put("PanToSelection", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PanToSelectionAction(mapWidget);
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
		REGISTRY.put("ZoomInMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomInModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomNext", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomNextAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomOut", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomOutAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomOutMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomOutModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomPrevious", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomPreviousAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomToRectangleMode", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToRectangleModalAction(mapWidget);
			}
		});
		REGISTRY.put("ZoomToSelection", new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToSelectionAction(mapWidget);
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
