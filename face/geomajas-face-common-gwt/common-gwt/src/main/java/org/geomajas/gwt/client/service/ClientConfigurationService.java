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

package org.geomajas.gwt.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * Service for fetching specific configuration objects from the server.
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public final class ClientConfigurationService {

	// map is not synchronized as this class runs in JavaScript which only has one execution thread
	private static final Map<String, ClientApplicationInfo> CONFIG = new HashMap<String, ClientApplicationInfo>();
	// map is not synchronized as this class runs in JavaScript which only has one execution thread
	private static final Map<String, List<DelayedCallback>> BACKLOG = new HashMap<String, List<DelayedCallback>>();
	private static final ClientConfigurationLoader DEFAULT_LOADER;
	private static final ClientConfigurationSetter SETTER = new ClientConfigurationSetterImpl();
	private static ClientConfigurationLoader configurationLoader;

	static {
		DEFAULT_LOADER = new ConfigurationLoaderImpl();
		configurationLoader = DEFAULT_LOADER;
	}

	private ClientConfigurationService() {
		// final class, hide constructor
	}

	/**
	 * Set the {@link ClientConfigurationLoader} which is responsible for getting the application configuration from the
	 * server.
	 *
	 * @param configurationLoader configuration loader
	 */
	public static void setConfigurationLoader(ClientConfigurationLoader configurationLoader) {
		ClientConfigurationService.configurationLoader = configurationLoader;
	}

	/**
	 * Clear the cached data, forcing data to be reloaded on the next request.
	 */
	public static void clear() {
		CONFIG.clear();
	}

	/**
	 * Get the configuration for a specific widget. This method will search within an application context at the highest
	 * level. If the requested configuration can't be found there, it will go deeper by investigating all map
	 * configurations as well. This means that it will find maps, tool-bars and layer trees as well.
	 * 
	 * @param applicationId
	 *            The application name wherein to search for the widget configuration.
	 * @param name
	 *            The actual widget configuration bean name.
	 * @param callback
	 *            The call-back that is executed when the requested widget configuration is found. This is called
	 *            asynchronously. If the widget configuration is not found, null is passed as value!
	 */
	public static void getApplicationWidgetInfo(final String applicationId, final String name,
			final WidgetConfigurationCallback callback) {
		if (!CONFIG.containsKey(applicationId)) {
			if (addCallback(applicationId, new DelayedCallback(name, callback))) {
				configurationLoader.loadClientApplicationInfo(applicationId, SETTER);
			}
		} else {
			execute(applicationId, name, callback);
		}
	}

	/**
	 * Add a delayed callback for the given application id. Returns whether this is the first request for the
	 * application id.
	 *
	 * @param applicationId application id
	 * @param callback callback
	 * @return true when first request for that application id
	 */
	private static boolean addCallback(String applicationId, DelayedCallback callback) {
		boolean isFirst = false;
		List<DelayedCallback> list = BACKLOG.get(applicationId);
		if (null == list) {
			list = new ArrayList<DelayedCallback>();
			BACKLOG.put(applicationId, list);
			isFirst = true;
		}
		list.add(callback);
		return isFirst;
	}


	/**
	 * Get the configuration for a specific widget. This method will search within the context of a specific map. This
	 * means that it will find maps, tool-bars and layer trees as well as the widget configurations within a map.
	 * 
	 * @param applicationId
	 *            The application name wherein to search for the widget configuration.
	 * @param mapId
	 *            The map wherein to search for the widget configuration.
	 * @param name
	 *            The actual widget configuration bean name (can also be a layer tree or a tool-bar).
	 * @param callback
	 *            The call-back that is executed when the requested widget configuration is found. This is called
	 *            asynchronously. If the widget configuration is not found, null is passed as value!
	 */
	public static void getMapWidgetInfo(final String applicationId, final String mapId, final String name,
			final WidgetConfigurationCallback callback) {
		if (!CONFIG.containsKey(applicationId)) {
			if (addCallback(applicationId, new DelayedCallback(mapId, name, callback))) {
				configurationLoader.loadClientApplicationInfo(applicationId, SETTER);
			}
		} else {
			execute(applicationId, mapId, name, callback);
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void execute(final String application, final String name,
			final WidgetConfigurationCallback callback) {
		ClientApplicationInfo applicationInfo = CONFIG.get(application);

		// First search the application level widget configurations:
		ClientWidgetInfo widgetInfo = applicationInfo.getWidgetInfo(name);
		if (widgetInfo != null) {
			callback.execute(widgetInfo);
			return;
		}

		// Next, search within each map configuration:
		for (ClientMapInfo mapInfo : applicationInfo.getMaps()) {
			widgetInfo = search(mapInfo, name);
			if (widgetInfo != null) {
				callback.execute(widgetInfo);
				return;
			}
		}

		callback.execute(null); // found nothing, still invoke the callback!
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
		callback.execute(null); // found nothing, still invoke the callback!
	}

	private static ClientWidgetInfo search(ClientMapInfo mapInfo, String name) {
		// Corner case, what if the bean name equals the map ID? Just return the damned map:
		if (mapInfo.getId().equals(name)) {
			return mapInfo;
		}

		// Corner case: check the layer trees:
		ClientLayerTreeInfo layerTree = mapInfo.getLayerTree();
		if (null != layerTree && name.equals(layerTree.getId())) {
			return mapInfo.getLayerTree();
		}

		// Corner case: check the tool-bar:
		ClientToolbarInfo toolBar = mapInfo.getToolbar();
		if (null != toolBar && name.equals(toolBar.getId())) {
			return mapInfo.getToolbar();
		}

		// Now search all widget info objects within the map:
		Map<String, ClientWidgetInfo> widgets = mapInfo.getWidgetInfo();
		if (null != widgets && widgets.containsKey(name)) {
			return widgets.get(name);
		}

		// Bean was not found:
		return null;
	}

	/**
	 * Encapsulation of the callback with the information to retrieve the requested object from the application
	 * configuration.
	 *
	 * @author Joachim Van der Auwera
	 */
	private static final class DelayedCallback {

		private final String mapId;
		private final String widgetKey;
		private final WidgetConfigurationCallback callback;

		private DelayedCallback(String widgetKey, WidgetConfigurationCallback callback) {
			this(null, widgetKey, callback);
		}

		private DelayedCallback(String mapId, String widgetKey, WidgetConfigurationCallback callback) {
			this.mapId = mapId;
			this.widgetKey = widgetKey;
			this.callback = callback;
		}

		public void run(String applicationId) {
			if (null == mapId) {
				execute(applicationId, widgetKey, callback);
			} else {
				execute(applicationId, mapId, widgetKey, callback);
			}
		}
	}

	/**
	 * Callback for the configuration loader which allows storing the loaded application configuration.
	 *
	 * @author Joachim Van der Auwera
	 */
	private static class ClientConfigurationSetterImpl implements ClientConfigurationSetter {

		public void set(String applicationId, ClientApplicationInfo applicationInfo) {
			CONFIG.put(applicationId, applicationInfo);
			List<DelayedCallback> callbacks = BACKLOG.get(applicationId);
			BACKLOG.remove(applicationId);
			if (null != callbacks) {
				for (DelayedCallback callback : callbacks) {
					callback.run(applicationId);
				}
			}
		}
	}

	/**
	 * Default {@link ClientConfigurationLoader} implementation.
	 *
	 * @author Joachim Van der Auwera
	 */
	private static class ConfigurationLoaderImpl implements ClientConfigurationLoader {

		public void loadClientApplicationInfo(final String applicationId, final ClientConfigurationSetter setter) {
			GetConfigurationRequest request = new GetConfigurationRequest();
			request.setApplicationId(applicationId);
			GwtCommand command = new GwtCommand(GetConfigurationRequest.COMMAND);
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command,
					new AbstractCommandCallback<GetConfigurationResponse>() {

				public void execute(GetConfigurationResponse response) {
					setter.set(applicationId, response.getApplication());
				}
			});
		}
	}
}