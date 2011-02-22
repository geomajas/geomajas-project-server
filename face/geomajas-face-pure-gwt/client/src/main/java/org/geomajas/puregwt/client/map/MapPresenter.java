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

package org.geomajas.puregwt.client.map;

import java.util.Collection;

import org.geomajas.global.Api;
import org.geomajas.puregwt.client.map.controller.MapController;
import org.geomajas.puregwt.client.map.controller.MapListener;

/**
 * Definition of a presenter for the map model.....
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public interface MapPresenter {

	/**
	 * Returns a container in world space wherein one can render new vector objects. Note that all objects drawn into
	 * such a container should be expressed in world coordinates (the CRS of the map). These objects will also be
	 * automatically redrawn when the view port on the map changes.
	 * 
	 * @param id
	 *            The identifier of the container. If no such container exists, a new one will be created.
	 * @return Returns the vector container.
	 */
	WorldContainer getWorldContainer(String id);

	/**
	 * Returns a container in screen space wherein one can render new vector objects. Note that all objects drawn into
	 * such a container should be expressed in pixel coordinates. No matter how much the map moves or zooms, these
	 * objects will always remain on the same fixed position.
	 * 
	 * @param id
	 *            The identifier of the container. If no such container exists, a new one will be created.
	 * @return Returns the vector container.
	 */
	ScreenContainer getScreenContainer(String id);

	/**
	 * Returns the map model for this presenter. This model is the central layer handler for the map.
	 * 
	 * @return The map model.
	 */
	MapModel getMapModel();

	/**
	 * Add a new gadget to the map. These gadgets are autonomous entities that can draw themselves in screen space, and
	 * react to certain events from the view port.
	 * 
	 * @param mapGadget
	 *            The new gadget to add to the map.
	 */
	void addMapGadget(MapGadget mapGadget);

	/**
	 * Remove a gadget from the map. These gadgets are autonomous entities that can draw themselves in screen space, and
	 * react to certain events from the view port.
	 * 
	 * @param mapGadget
	 *            The gadget to remove from the map.
	 */
	void removeMapGadget(MapGadget mapGadget);

	/**
	 * An optional fall-back {@link MapController} to return to, when no controller is explicitly set (controller=null).
	 * If no current controller is active when this setter is called, it is applied immediately. The default fall-back
	 * controller when a map is initialized, is a controller that allows you to navigate.
	 * 
	 * @param fallbackController
	 *            The new fall-back controller to use.
	 */
	void setFallbackController(MapController fallbackController);

	/**
	 * Apply a new {@link MapController} on the map. This controller will handle all mouse-events that are global for
	 * the map. Only one controller can be set at any given time. When a controller is active on the map, using this
	 * method, any fall-back controller is automatically disabled.
	 * 
	 * @param controller
	 *            The new {@link MapController} object. If null is passed, then the active controller is again disabled.
	 *            At that time the fall-back controller is again activated.
	 */
	void setController(MapController controller);

	/**
	 * Return the currently active controller on the map.
	 * 
	 * @return The currently active controller.
	 */
	MapController getController();

	/**
	 * Add a new listener to the map. These listeners passively listen to mouse events on the map, without actually
	 * interfering with these events. The difference with a {@link MapController} is that controllers can do whatever
	 * they want, while a listener is not allowed to interfere with the mouse events in any way.
	 * 
	 * @param listener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean addListener(MapListener listener);

	/**
	 * Remove one of the currently active listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. The difference with a {@link MapController} is that
	 * controllers can do whatever they want, while a listener is not allowed to interfere with the mouse events in any
	 * way.
	 * 
	 * @param listener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean removeListener(MapListener listener);

	/**
	 * Get the currently active set of listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. The difference with a {@link MapController} is that
	 * controllers can do whatever they want, while a listener is not allowed to interfere with the mouse events in any
	 * way.
	 * 
	 * @return Returns the full collection of currently active listeners.
	 */
	Collection<MapListener> getListeners();
}