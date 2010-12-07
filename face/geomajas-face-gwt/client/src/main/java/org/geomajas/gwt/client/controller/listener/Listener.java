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

package org.geomajas.gwt.client.controller.listener;

import org.geomajas.global.Api;

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