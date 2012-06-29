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
package org.geomajas.plugin.jsapi.gwt.client.exporter;

import java.util.HashMap;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.action.toolbar.ToolId;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.DispatchStartedEvent;
import org.geomajas.gwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.gwt.client.controller.FeatureInfoController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.MeasureDistanceController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.controller.SelectionController;
import org.geomajas.gwt.client.controller.SingleSelectionController;
import org.geomajas.gwt.client.controller.editing.ParentEditController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.jsapi.client.GeomajasService;
import org.geomajas.plugin.jsapi.client.event.DispatchStartedHandler;
import org.geomajas.plugin.jsapi.client.event.DispatchStoppedHandler;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.geomajas.plugin.jsapi.client.map.ExportableFunction;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.geomajas.plugin.jsapi.client.spatial.BboxService;
import org.geomajas.plugin.jsapi.client.spatial.GeometryService;
import org.geomajas.plugin.jsapi.gwt.client.exporter.map.MapImpl;
import org.geomajas.plugin.jsapi.gwt.client.exporter.spatial.BboxServiceImpl;
import org.geomajas.plugin.jsapi.gwt.client.exporter.spatial.GeometryServiceImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;

/**
 * MapRegistry provides a registry where {@link org.geomajas.plugin.jsapi.client.map.Map} components can be registered
 * fromGWT
 * to be retrieved from plain JavaScript.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeomajasService")
@ExportPackage("org.geomajas.jsapi")
public final class GeomajasServiceImpl implements Exportable, GeomajasService {

	private static final GeomajasServiceImpl INSTANCE = new GeomajasServiceImpl();

	private HashMap<String, HashMap<String, Map>> maps = new HashMap<String, HashMap<String, Map>>();

	private GeomajasServiceImpl() {
	}

	/**
	 * Get the singleton instance of the GeomajasRegistry.
	 * 
	 * @return the {@link GeomajasServiceImpl}.
	 * @since 1.0.0
	 */
	@Api
	@Export("$wnd.Geomajas")
	public static GeomajasService getInstance() {
		return INSTANCE;
	}

	/**
	 * Register the given {@link Map} with applicationId and mapId.
	 * 
	 * @param applicationId
	 *            the application id.
	 * @param mapId
	 *            the map id.
	 * @param elementId
	 *            the DOM element ID onto which to attach the map.
	 */
	@Export
	public Map createMap(String applicationId, String mapId, String elementId) {
		Map map = getMap(applicationId, mapId);
		if (map == null) {
			MapWidget mapWidget = new MapWidget(mapId, applicationId);
			map = new MapImpl(mapWidget);
			Element element = DOM.getElementById(elementId);
			if (element != null) {
				int width = element.getClientWidth();
				int height = element.getClientHeight();
				map.setSize(width, height);
				map.setHtmlElementId(elementId);
			}
			registerMap(mapId, applicationId, map);
		} else {
			((MapImpl) map).getMapWidget().redraw();
		}
		return map;
	}

	/**
	 * Register the given {@link Map} with applicationId and mapId.
	 * 
	 * @param applicationId
	 *            the application id.
	 * @param mapId
	 *            the map id.
	 * @param map
	 *            the map to register.
	 */
	public void registerMap(String applicationId, String mapId, Map map) {
		HashMap<String, Map> mapMap;
		if (maps.containsKey(applicationId)) {
			mapMap = maps.get(applicationId);
			if (!mapMap.containsKey(mapId)) {
				mapMap.put(mapId, map);
			}
		} else {
			mapMap = new HashMap<String, Map>();
			mapMap.put(mapId, map);
			maps.put(applicationId, mapMap);
		}
	}

	/**
	 * Return the {@link Map} that is registered with the given application and map ID.
	 * 
	 * @param applicationId
	 *            the application id.
	 * @param mapId
	 *            the map id.
	 * @return the map.
	 */
	@Export
	public Map getMap(String applicationId, String mapId) {
		HashMap<String, Map> application = maps.get(applicationId);
		if (null == application) {
			return null;
		}
		return application.get(mapId);
	}

	/**
	 * Add a handler that is called whenever the client starts communicating with the back-end.
	 * 
	 * @param handler
	 *            The actual handler (closure).
	 * @return The registration for the handler. Using this object the handler can be removed again.
	 */
	@Export
	public JsHandlerRegistration addDispatchStartedHandler(final DispatchStartedHandler handler) {
		HandlerRegistration registration = GwtCommandDispatcher.getInstance().addDispatchStartedHandler(
				new org.geomajas.gwt.client.command.event.DispatchStartedHandler() {

					public void onDispatchStarted(DispatchStartedEvent event) {
						handler.onDispatchStarted(new org.geomajas.plugin.jsapi.client.event.DispatchStartedEvent());
					}
				});
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Add a handler that is called whenever the client stops communicating with the back-end.
	 * 
	 * @param handler
	 *            The actual handler (closure).
	 * @return The registration for the handler. Using this object the handler can be removed again.
	 */
	@Export
	public JsHandlerRegistration addDispatchStoppedHandler(final DispatchStoppedHandler handler) {
		HandlerRegistration registration = GwtCommandDispatcher.getInstance().addDispatchStoppedHandler(
				new org.geomajas.gwt.client.command.event.DispatchStoppedHandler() {

					public void onDispatchStopped(DispatchStoppedEvent event) {
						handler.onDispatchStopped(new org.geomajas.plugin.jsapi.client.event.DispatchStoppedEvent());
					}
				});
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Create a known controller for the map. Different implementations may 'know' different controllers, so it's best
	 * to check with the implementing class.
	 * 
	 * @param map
	 *            The onto which the controller should be applied.
	 * @param id
	 *            The unique ID for the map controller (implementation specific).
	 * @return The map controller, or null if it could not be found.
	 */
	@Export
	public MapController createMapController(Map map, String id) {
		MapWidget mapWidget = ((MapImpl) map).getMapWidget();
		if ("PanMode".equalsIgnoreCase(id)) {
			return createMapController(map, new PanController(mapWidget));
		} else if (ToolId.TOOL_MEASURE_DISTANCE_MODE.equalsIgnoreCase(id)) {
			return createMapController(map, new MeasureDistanceController(mapWidget));
		} else if (ToolId.TOOL_FEATURE_INFO.equalsIgnoreCase(id)) {
			return createMapController(map, new FeatureInfoController(mapWidget, 3));
		} else if (ToolId.TOOL_SELECTION_MODE.equalsIgnoreCase(id)) {
			return createMapController(map, new SelectionController(mapWidget, 500, 0.5f, false, 3));
		} else if ("SingleSelectionMode".equalsIgnoreCase(id)) {
			return createMapController(map, new SingleSelectionController(mapWidget, false, 3));
		} else if (ToolId.TOOL_EDIT.equalsIgnoreCase(id)) {
			return createMapController(map, new ParentEditController(mapWidget));
		}
		return null;
	}

	/**
	 * Get a service for geometry manipulation.
	 * 
	 * @return A service for geometry manipulation.
	 */
	@Export
	public GeometryService getGeometryService() {
		return new GeometryServiceImpl();
	}

	/**
	 * Get a service for bounding box manipulation.
	 * 
	 * @return A service for bounding box manipulation.
	 */
	@Export
	public BboxService getBboxService() {
		return new BboxServiceImpl();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private MapController createMapController(Map map, final GraphicsController controller) {
		MapController mapController = new MapController(map, controller);

		mapController.setActivationHandler(new ExportableFunction() {

			public void execute() {
				controller.onActivate();
			}
		});
		mapController.setDeactivationHandler(new ExportableFunction() {

			public void execute() {
				controller.onDeactivate();
			}
		});
		return mapController;
	}
}