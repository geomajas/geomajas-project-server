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
package org.geomajas.plugin.jsapi.client.map;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.jsapi.client.event.JsEventBus;
import org.geomajas.plugin.jsapi.client.map.controller.MapController;
import org.geomajas.plugin.jsapi.client.map.feature.FeatureSearchService;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable facade of the Map presentation. See the specific implementation for details how to initialize.
 * 
 * The implementation should make sure the newly created Map is registered in a
 * {@link org.geomajas.plugin.jsapi.client.GeomajasService}! This way created maps are guaranteed available trough
 * JavaScript.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
public interface Map extends Exportable {

	/**
	 * Returns the layers model for this presenter. This model is the central layer handler for the map, with methods
	 * for getting layers, moving them up and down, adding or removing layers, ..
	 * 
	 * @return The layers model.
	 */
	LayersModel getLayersModel();

	/**
	 * Returns the {@link ViewPort} associated with this map. The view port regulates zooming and panning around the
	 * map, but also presents transformation methods for transforming vector objects between the different render
	 * spaces.
	 * 
	 * @return Returns the view port.
	 */
	ViewPort getViewPort();

	/**
	 * Apply a new {@link MapController} on the map. This controller will handle all mouse-events that are global for
	 * the map. Only one controller can be set at any given time. When a controller is active on the map, using this
	 * method, any fall-back controller is automatically disabled.
	 * 
	 * @param controller
	 *            The new {@link MapController} object. If null is passed, then the active controller is again disabled.
	 *            At that time the fall-back controller is again activated.
	 */
	void setMapController(MapController controller);

	/**
	 * Return the currently active controller on the map.
	 * 
	 * @return The currently active controller.
	 */
	MapController getMapController();

	/**
	 * Couples this map to an existing HTML element (div or span).
	 * 
	 * @param id
	 *            id of the element
	 */
	void setHtmlElementId(String id);

	/**
	 * Get the id of the HTML element this map is coupled with.
	 *
	 * @return element id
	 */
	String getHtmlElementId();

	/**
	 * Apply a new width and height on the map. Both parameters are expressed in pixels.
	 * 
	 * @param width
	 *            The new pixel width for the map.
	 * @param height
	 *            The new pixel height for the map.
	 */
	void setSize(int width, int height);

	/**
	 * Apply a new mouse cursor when hovering above the map.
	 * 
	 * @param cursor
	 *            The new cursor to apply.
	 */
	void setCursor(String cursor);

	/**
	 * Get the event bus that handles all event handlers and event firing for this map.
	 * 
	 * @return The event bus that manages all event related to this map.
	 */
	JsEventBus getEventBus();

	/**
	 * Return a service that can search for features.
	 * 
	 * @return A service that can search for features.
	 */
	FeatureSearchService getFeatureSearchService();
}