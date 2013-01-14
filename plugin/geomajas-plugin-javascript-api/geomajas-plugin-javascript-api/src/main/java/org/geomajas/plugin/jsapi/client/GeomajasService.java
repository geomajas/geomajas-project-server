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
package org.geomajas.plugin.jsapi.client;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.jsapi.client.event.DispatchStartedHandler;
import org.geomajas.plugin.jsapi.client.event.DispatchStoppedHandler;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.geomajas.plugin.jsapi.client.map.Map;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.geomajas.plugin.jsapi.client.spatial.BboxService;
import org.geomajas.plugin.jsapi.client.spatial.GeometryService;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

/**
 * MapRegistry provides a registry where {@link org.geomajas.plugin.jsapi.map.Map} components can be registered from GWT
 * to be retrieved from plain JavaScript. This will most probably be implemented as a singleton.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
@Export
public interface GeomajasService extends Exportable {

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
	Map createMap(String applicationId, String mapId, String elementId);

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
	void registerMap(String applicationId, String mapId, Map map);

	/**
	 * Return the {@link Map} that is registered with the given application and map ID.
	 * 
	 * @param applicationId
	 *            the application id.
	 * @param mapId
	 *            the map id.
	 * @return the map.
	 */
	Map getMap(String applicationId, String mapId);

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
	MapController createMapController(Map map, String id);

	/**
	 * Get a service for geometry manipulation.
	 * 
	 * @return A service for geometry manipulation.
	 */
	GeometryService getGeometryService();

	/**
	 * Get a service for bounding box manipulation.
	 * 
	 * @return A service for bounding box manipulation.
	 */
	BboxService getBboxService();

	/**
	 * Add a handler that is called whenever the client starts communicating with the back-end.
	 * 
	 * @param handler
	 *            The actual handler (closure).
	 * @return The registration for the handler. Using this object the handler can be removed again.
	 */
	JsHandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler);

	/**
	 * Add a handler that is called whenever the client stops communicating with the back-end.
	 * 
	 * @param handler
	 *            The actual handler (closure).
	 * @return The registration for the handler. Using this object the handler can be removed again.
	 */
	JsHandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler);
}