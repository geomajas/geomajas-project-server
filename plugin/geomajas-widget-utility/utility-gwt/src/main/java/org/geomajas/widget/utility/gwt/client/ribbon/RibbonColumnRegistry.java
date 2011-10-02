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

package org.geomajas.widget.utility.gwt.client.ribbon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn.TitleAlignment;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarRadioAction;

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

	private static int buttonIconSize = 24;

	private static int listIconSize = 16;

	static {
		REGISTRY = new HashMap<String, RibbonColumnCreator>();

		REGISTRY.put("ToolbarActionButton", new RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				if (tools != null && tools.size() > 0) {
					ButtonAction action = getAction(tools.get(0), mapWidget);
					if (action != null) {
						return new RibbonButton(action, buttonIconSize, TitleAlignment.BOTTOM);
					}
				}
				return null;
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
						return new ActionListRibbonColumn(actions, listIconSize);
					}
				}
				return null;
			}
		});
	}

	private RibbonColumnRegistry() {
		// utility class, hide constructor
	}

	/**
	 * Change the default icon size for the big buttons in a ribbon.
	 * 
	 * @param buttonIconSize
	 *            The new icon size. Default is 24 pixels.
	 */
	public static void setButtonIconSize(int buttonIconSize) {
		RibbonColumnRegistry.buttonIconSize = buttonIconSize;
	}

	/**
	 * Change the default icon size for the small vertical action lists in a ribbon.
	 * 
	 * @param listIconSize
	 *            The new icon size. Default is 16.
	 */
	public static void setListIconSize(int listIconSize) {
		RibbonColumnRegistry.listIconSize = listIconSize;
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

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private static ButtonAction getAction(ClientToolInfo tool, MapWidget mapWidget) {
		ToolbarBaseAction toolbarAction = ToolbarRegistry.getToolbarAction(tool.getId(), mapWidget);

		if (toolbarAction != null) {
			ButtonAction action = null;
			if (toolbarAction instanceof ToolbarAction) {
				action = new ToolbarButtonAction(toolbarAction);
			} else if (toolbarAction instanceof ToolbarModalAction) {
				action = new ToolbarRadioAction((ToolbarModalAction) toolbarAction, "map-controller-group");
			}
			if (action != null) {
				for (Parameter parameter : tool.getParameters()) {
					action.configure(parameter.getName(), parameter.getValue());
				}
				return action;
			}
		}
		return null;
	}
}