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

package org.geomajas.widget.utility.gwt.client.ribbon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarCanvas;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.action.toolbar.DropDownButtonAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonCanvas;
import org.geomajas.widget.utility.gwt.client.action.ToolbarRadioAction;
import org.geomajas.widget.utility.gwt.client.ribbon.dropdown.DropDownRibbonButton;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;
/**
 * Registry for all {@link RibbonColumn} types. By default only big buttons and vertical columns are known, but users
 * can add their own types. This registry also offers a few configuration settings, such as icon sizes for the default
 * ribbon columns.
 * 
 * @author Pieter De Graef
 */
public final class RibbonColumnRegistry {

	/**
	 * Special interface that creates a certain kind of {@link RibbonColumn}.
	 * 
	 * @author Pieter De Graef
	 */
	public interface RibbonColumnCreator {

		/**
		 * Create a {@link RibbonColumn} instance.
		 * 
		 * @param tools
		 *            A list of tools to be used in the ribbon column. It is up to the implementation to interpret these
		 *            correctly.This is optional, and can be null.
		 * @param mapWidget
		 *            The map on which the action applies.
		 * @return Returns the requested {@link RibbonColumn} instance.
		 */
		RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget);
	}

	private static final Map<String, RibbonColumnCreator> REGISTRY;

	static {
		REGISTRY = new HashMap<String, RibbonColumnCreator>();

		REGISTRY.put("ToolbarActionButton", new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				RibbonColumn column = null;
				if (tools != null && tools.size() > 0) {
					ClientToolInfo tool = tools.get(0);
					ButtonAction action = getAction(tool, mapWidget);
					if (action != null) {
						if (action instanceof ToolbarButtonCanvas) {
							column = new RibbonColumnCanvas((ToolbarButtonCanvas) action);
						} else if (action instanceof DropDownButtonAction) {
							column = new DropDownRibbonButton(
									(DropDownButtonAction) action, tool.getTools(), mapWidget);
						} else {
							column = new RibbonButton(action);
						}
					}
				}
				return column;
			}
		});

		REGISTRY.put("ToolbarActionList", new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				if (tools != null && tools.size() > 0) {
					List<ButtonAction> actions = new ArrayList<ButtonAction>();
					for (ClientToolInfo tool : tools) {
						ButtonAction action = getAction(tool, mapWidget);
						if (action != null) {
							actions.add(action);
						}
					}
					if (actions.size() > 0) {
						return new ActionListRibbonColumn(actions, GuwLayout.ribbonColumnListIconSize, mapWidget);
					}
				}
				return null;
			}
		});
		
		REGISTRY.put("ToolbarDropDownButton", new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new DropDownRibbonButton(new DropDownButtonAction(), tools, mapWidget);
			}
		});
	}

	private RibbonColumnRegistry() {
		// utility class, hide constructor
	}

	/**
	 * Add another key to the registry. This will overwrite the previous value.
	 * 
	 * @param key
	 *            Unique key for the action within this registry.
	 * @param ribbonColumnCreator
	 *            Implementation of the {@link RibbonColumnCreator} interface to link the correct type of ribbon column
	 *            widget to the key.
	 */
	public static void put(String key, RibbonColumnCreator ribbonColumnCreator) {
		if (null != key && null != ribbonColumnCreator) {
			REGISTRY.put(key, ribbonColumnCreator);
		}
	}

	/**
	 * Get the ribbon column which matches the given key.
	 * 
	 * @param key
	 *            Unique key for the action within this registry.
	 * @param tools
	 *            A list of tools to be used in the ribbon column. It is up to the implementation to interpret these
	 *            correctly.This is optional, and can be null.
	 * @param parameters
	 *            A list of specific configuration parameters to be used in the column. This is optional, and can be
	 *            null.
	 * @param mapWidget
	 *            The map to which this action is linked.
	 * @return {@link RibbonColumn} or null when key not found.
	 */
	public static RibbonColumn getRibbonColumn(String key, List<ClientToolInfo> tools, List<Parameter> parameters,
			MapWidget mapWidget) {
		RibbonColumnCreator ribbonColumnCreator = REGISTRY.get(key);
		if (ribbonColumnCreator == null) {
			Log.logWarn("Could not find RibbonColumn with ID: " + key);
			return null;
		} else {
			RibbonColumn column = ribbonColumnCreator.create(tools, mapWidget);
			if (parameters != null) {
				for (Parameter parameter : parameters) {
					column.configure(parameter.getName(), parameter.getValue());
				}
			}
			return column;
		}
	}

	public static ButtonAction getAction(ClientToolInfo tool, MapWidget mapWidget) {
		ToolbarBaseAction toolbarAction = ToolbarRegistry.getToolbarAction(tool.getToolId(), mapWidget);

		if (toolbarAction != null) {
			ButtonAction action = null;
			if (toolbarAction instanceof DropDownButtonAction) {
				((DropDownButtonAction) toolbarAction).setTools(tool.getTools());
				action = new ToolbarButtonAction(toolbarAction);
			} else if (toolbarAction instanceof ToolbarAction) {
				action = new ToolbarButtonAction(toolbarAction);
			} else if (toolbarAction instanceof ToolbarModalAction) {
				action = new ToolbarRadioAction((ToolbarModalAction) toolbarAction, "map-controller-group");
			} else if (toolbarAction instanceof ToolbarCanvas) {
				action = new ToolbarButtonCanvas(toolbarAction);
			}
			if (action != null) {
				for (Parameter parameter : tool.getParameters()) {
					action.configure(parameter.getName(), parameter.getValue());
				}
				String iconUrl = tool.getIcon();
				if (null != iconUrl && !"".equals(iconUrl)) {
					action.setIcon(iconUrl);
				}
				String title = tool.getTitle();
				if (null != title && !"".equals(title)) {
					action.setTitle(title);
				}
				// if description is set in ClientToolInfo, it overrides the tooltip parameter.
				String description = tool.getDescription();
				if (null != description && !"".equals(description)) {
					action.setTooltip(description);
				}
				return action;
			}
		}
		return null;
	}
}