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

package org.geomajas.puregwt.client.map.controller;

import org.geomajas.annotation.FutureApi;
import org.geomajas.global.UserImplemented;

/**
 * <p>
 * Interface for passive listeners on a map. These listeners receive notifications of mouse events on the map, but
 * cannot interfere. That is why they receive a replacement event ({@link MapListenerEvent}) instead of the real mouse
 * events.
 * </p>
 * <p>
 * The difference with a {@link MapController} is that controllers can do whatever they want, while a listener is not
 * allowed to interfere with the mouse events in any way. As a result, only one {@link MapController} can be active on a
 * map at any given time, while there is no limit to the amount of listeners active on a map.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@UserImplemented
@FutureApi(allMethods = true)
public interface MapListener {

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEDOWN</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseDown(MapListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEUP</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseUp(MapListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEMOVE</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseMove(MapListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEOUT</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseOut(MapListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEOVER</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseOver(MapListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEWHEEL</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseWheel(MapListenerEvent event);
}