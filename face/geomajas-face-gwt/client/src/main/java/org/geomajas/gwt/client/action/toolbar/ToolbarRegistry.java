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

package org.geomajas.gwt.client.action.toolbar;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.annotation.Api;
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
		// Please keep registrations in alphabetical order
		REGISTRY.put(ToolId.BUTTON_GROUP, new ToolCreator() {
			
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ButtonGroup();
			}
		});
		REGISTRY.put(ToolId.DROP_DOWN_BUTTON, new ToolCreator() {
			
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new DropDownButtonAction();
			}
		});
		REGISTRY.put(ToolId.TOOL_EDIT, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new EditingModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_FEATURE_INFO, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FeatureInfoModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_MEASURE_DISTANCE_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new MeasureModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_PAN_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PanModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_PAN_TO_SELECTION, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PanToSelectionAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_SELECTION_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new SelectionModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_DESELECT_ALL, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new DeselectAllAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_IN, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomInAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_IN_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomInModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_NEXT, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomNextAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_OUT, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomOutAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_OUT_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomOutModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_PREVIOUS, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomPreviousAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_TO_RECTANGLE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToRectangleAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_TO_RECTANGLE_MODE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToRectangleModalAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_ZOOM_TO_SELECTION, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ZoomToSelectionAction(mapWidget);
			}
		});
		REGISTRY.put(ToolId.TOOL_SCALE_SELECT, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new ScaleSelectAction(mapWidget);
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
