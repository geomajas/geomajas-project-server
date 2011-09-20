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

package org.geomajas.gwt.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.FutureApi;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * Service for fetching specific configuration objects from the server.
 * 
 * @author Pieter De Graef
 * @since 1.10.0
 */
@FutureApi
public final class ConfigurationService {

	private static final Map<String, ClientApplicationInfo> CONFIG = new HashMap<String, ClientApplicationInfo>();

	/** Final classes should have a private no-args constructor. */
	private ConfigurationService() {
	}

	/**
	 * Get the configuration for a specific widget. This method will search within an application context at the highest
	 * level. If the requested configuration can't be found there, it will go deeper by investigating all map
	 * configurations as well. This means that it will find maps, tool-bars and layer trees as well.
	 * 
	 * @param application
	 *            The application name wherein to search for the widget configuration.
	 * @param name
	 *            The actual widget configuration bean name.
	 * @param callback
	 *            The call-back that is executed when the requested widget configuration is found. This is called
	 *            asynchronously. If the widget configuration is not found, this is never executed!
	 */
	@SuppressWarnings("rawtypes")
	public static void getApplicationWidgetInfo(final String application, final String name,
			final WidgetConfigurationCallback callback) {
		if (!CONFIG.containsKey(application)) {
			GetConfigurationRequest request = new GetConfigurationRequest();
			request.setApplicationId(application);
			GwtCommand command = new GwtCommand(GetConfigurationRequest.COMMAND);
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetConfigurationResponse>() {

				public void execute(GetConfigurationResponse response) {
					CONFIG.put(application, response.getApplication());
					ConfigurationService.execute(application, name, callback);
				}
			});
		} else {
			execute(application, name, callback);
		}
	}

	/**
	 * Get the configuration for a specific widget. This method will search within the context of a specific map. This
	 * means that it will find maps, tool-bars and layer trees as well as the widget configurations within a map.
	 * 
	 * @param application
	 *            The application name wherein to search for the widget configuration.
	 * @param mapId
	 *            The map wherein to search for the widget configuration.
	 * @param name
	 *            The actual widget configuration bean name (can also be a layer tree or a tool-bar).
	 * @param callback
	 *            The call-back that is executed when the requested widget configuration is found. This is called
	 *            asynchronously. If the widget configuration is not found, this is never executed!
	 */
	@SuppressWarnings("rawtypes")
	public static void getMapWidgetInfo(final String application, final String mapId, final String name,
			final WidgetConfigurationCallback callback) {
		if (!CONFIG.containsKey(application)) {
			GetConfigurationRequest request = new GetConfigurationRequest();
			request.setApplicationId(application);
			GwtCommand command = new GwtCommand(GetConfigurationRequest.COMMAND);
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetConfigurationResponse>() {

				public void execute(GetConfigurationResponse response) {
					CONFIG.put(application, response.getApplication());
					ConfigurationService.execute(application, mapId, name, callback);
				}
			});
		} else {
			execute(application, mapId, name, callback);
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void execute(final String application, final String name, final WidgetConfigurationCallback cb) {
		ClientApplicationInfo applicationInfo = CONFIG.get(application);

		// First search the application level widget configurations:
		ClientWidgetInfo widgetInfo = applicationInfo.getWidgetInfo(name);
		if (widgetInfo != null) {
			cb.execute(widgetInfo);
			return;
		}

		// Next, search within each map configuration:
		for (ClientMapInfo mapInfo : applicationInfo.getMaps()) {
			widgetInfo = search(mapInfo, name);
			if (widgetInfo != null) {
				cb.execute(widgetInfo);
				return;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void execute(final String application, final String mapId, final String name,
			final WidgetConfigurationCallback callback) {
		// Go over all maps, and search therein for the ClientWidgetInfo:
		List<ClientMapInfo> maps = CONFIG.get(application).getMaps();
		for (ClientMapInfo mapInfo : maps) {
			if (mapId.equals(mapInfo.getId())) {
				ClientWidgetInfo widgetInfo = search(mapInfo, name);
				if (widgetInfo != null) {
					callback.execute(widgetInfo);
					return;
				}
			}
		}
	}

	private static ClientWidgetInfo search(ClientMapInfo mapInfo, String name) {
		// Corner case, what if the bean name equals the map ID? Just return the damned map:
		if (mapInfo.getId().equals(name)) {
			return mapInfo;
		}

		// Corner case: check the layer trees:
		ClientLayerTreeInfo layerTree = mapInfo.getLayerTree();
		if (layerTree != null) {
			if (name.equals(layerTree.getId())) {
				return mapInfo.getLayerTree();
			}
		}

		// Corner case: check the tool-bar:
		ClientToolbarInfo toolBar = mapInfo.getToolbar();
		if (toolBar != null) {
			if (name.equals(toolBar.getId())) {
				return mapInfo.getToolbar();
			}
		}

		// Now search all widget info objects within the map:
		Map<String, ClientWidgetInfo> widgets = mapInfo.getWidgetInfo();
		if (widgets != null) {
			if (widgets.containsKey(name)) {
				return widgets.get(name);
			}
		}

		// Bean was not found:
		return null;
	}
}