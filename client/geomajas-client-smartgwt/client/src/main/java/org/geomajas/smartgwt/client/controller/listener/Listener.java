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

package org.geomajas.smartgwt.client.controller.listener;

import org.geomajas.annotation.Api;

/**
 * <p>
 * Interface for passive listeners on a map. These listeners receive notifications of mouse events on the map, but
 * cannot interfere. That is why they receive a replacement event ({@link ListenerEvent}) instead of the real mouse
 * events.
 * </p>
 * <p>
 * The difference with a <code>GraphicsController</code> is that controllers can do whatever they want, while a listener
 * is not allowed to interfere with the mouse events in any way. As a result, only one <code>GraphicsController</code>
 * can be active on a map at any given time, while there is no limit to the amount of listeners active on a map.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface Listener {

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEDOWN</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseDown(ListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEUP</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseUp(ListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEMOVE</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseMove(ListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEOUT</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseOut(ListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEOVER</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseOver(ListenerEvent event);

	/**
	 * React upon receiving a <code>com.google.gwt.user.client.Event.ONMOUSEWHEEL</code> event from the map.
	 * 
	 * @param event
	 *            A specific event for listeners. It contains the type of mouse event, positions (screen + world) and
	 *            the target element and more.
	 */
	void onMouseWheel(ListenerEvent event);
}