/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.puregwt.client.map;

import java.util.Collection;

import org.geomajas.puregwt.client.controller.Controller;
import org.geomajas.puregwt.client.controller.Listener;

/**
 * Definition of a presenter for the map model.....
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
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
	VectorContainer getWorldContainer(String id);

	/**
	 * Returns a container in screen space wherein one can render new vector objects. Note that all objects drawn into
	 * such a container should be expressed in pixel coordinates. No matter how much the map moves or zooms, these
	 * objects will always remain on the same fixed position.
	 * 
	 * @param id
	 *            The identifier of the container. If no such container exists, a new one will be created.
	 * @return Returns the vector container.
	 */
	VectorContainer getScreenContainer(String id);

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
	 * An optional fall-back {@link Controller} to return to, when no controller is explicitly set (controller=null). If
	 * no current controller is active when this setter is called, it is applied immediately. The default fall-back
	 * controller when a map is initialized, is a controller that allows you to navigate.
	 * 
	 * @param fallbackController
	 *            The new fall-back controller to use.
	 */
	void setFallbackController(Controller fallbackController);

	/**
	 * Apply a new {@link Controller} on the map. This controller will handle all mouse-events that are global for the
	 * map. Only one controller can be set at any given time. When a controller is active on the map, using this method,
	 * any fall-back controller is automatically disabled.
	 * 
	 * @param controller
	 *            The new {@link Controller} object. If null is passed, then the active controller is again disabled. At
	 *            that time the fall-back controller is again activated.
	 */
	void setController(Controller controller);

	/**
	 * Return the currently active controller on the map.
	 * 
	 * @return The currently active controller.
	 */
	Controller getController();

	/**
	 * Add a new listener to the map. These listeners passively listen to mouse events on the map, without actually
	 * interfering with these events. The difference with a {@link Controller} is that controllers can do whatever they
	 * want, while a listener is not allowed to interfere with the mouse events in any way.
	 * 
	 * @param listener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean addListener(Listener listener);

	/**
	 * Remove one of the currently active listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. The difference with a {@link Controller} is that controllers
	 * can do whatever they want, while a listener is not allowed to interfere with the mouse events in any way.
	 * 
	 * @param listener
	 *            The listener to try and remove again.
	 * @return Returns true of removal was successful, false otherwise (i.e. if the listener could not be found).
	 */
	boolean removeListener(Listener listener);

	/**
	 * Get the currently active set of listeners on the map. These listeners passively listen to mouse events on the
	 * map, without actually interfering with these events. The difference with a {@link Controller} is that controllers
	 * can do whatever they want, while a listener is not allowed to interfere with the mouse events in any way.
	 * 
	 * @return Returns the full collection of currently active listeners.
	 */
	Collection<Listener> getListeners();
}