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

import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.Event;

/**
 * <p>
 * Controller that is used for setting a {@link Listener} onto a map. This class is used internally in the
 * {@link MapWidet}. It is this controller that notifies the listener of mouse events.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ListenerController extends AbstractGraphicsController {

	private Listener listener;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a controller for applying a passive listener onto the map. It is this controller that notifies the
	 * listener of mouse events.
	 * 
	 * @param mapWidget
	 *            The mapWidget onto which to apply this controller.
	 * @param listener
	 *            The actual listener object to activate.
	 */
	public ListenerController(MapWidget mapWidget, Listener listener) {
		super(mapWidget);
		this.listener = listener;
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		listener.onMouseDown(getEvent(Event.ONMOUSEDOWN, event));
	}

	public void onMouseUp(MouseUpEvent event) {
		listener.onMouseUp(getEvent(Event.ONMOUSEUP, event));
	}

	public void onMouseMove(MouseMoveEvent event) {
		listener.onMouseMove(getEvent(Event.ONMOUSEMOVE, event));
	}

	public void onMouseOut(MouseOutEvent event) {
		listener.onMouseOut(getEvent(Event.ONMOUSEOUT, event));
	}

	public void onMouseOver(MouseOverEvent event) {
		listener.onMouseOver(getEvent(Event.ONMOUSEOVER, event));
	}

	public void onMouseWheel(MouseWheelEvent event) {
		listener.onMouseWheel(getEvent(Event.ONMOUSEWHEEL, event));
	}

	// -------------------------------------------------------------------------
	// Getters
	// -------------------------------------------------------------------------

	/**
	 * Return the listener associated with this controller.
	 */
	public Listener getListener() {
		return listener;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private ListenerEvent getEvent(int eventBit, MouseEvent<?> event) {
		return new ListenerEvent(eventBit, getScreenPosition(event), getTarget(event), getTransformer(),
				event.isAltKeyDown(), event.isControlKeyDown(), event.isShiftKeyDown(), event.getNativeButton());
	}
}