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

package org.geomajas.puregwt.client.controller;

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
 * Controller that is used for setting a {@link MapListener} onto a map. This class is used internally in the map. It is
 * this controller that notifies the listener of mouse events.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class ListenerController extends AbstractMapController {

	private MapListener mapListener;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a controller for applying a passive listener onto the map. It is this controller that notifies the
	 * listener of mouse events.
	 * 
	 * @param mapListener
	 *            The actual map listener object to activate.
	 */
	public ListenerController(MapListener mapListener) {
		super();
		this.mapListener = mapListener;
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		mapListener.onMouseDown(getEvent(Event.ONMOUSEDOWN, event));
	}

	public void onMouseUp(MouseUpEvent event) {
		mapListener.onMouseUp(getEvent(Event.ONMOUSEUP, event));
	}

	public void onMouseMove(MouseMoveEvent event) {
		mapListener.onMouseMove(getEvent(Event.ONMOUSEMOVE, event));
	}

	public void onMouseOut(MouseOutEvent event) {
		mapListener.onMouseOut(getEvent(Event.ONMOUSEOUT, event));
	}

	public void onMouseOver(MouseOverEvent event) {
		mapListener.onMouseOver(getEvent(Event.ONMOUSEOVER, event));
	}

	public void onMouseWheel(MouseWheelEvent event) {
		mapListener.onMouseWheel(getEvent(Event.ONMOUSEWHEEL, event));
	}

	// -------------------------------------------------------------------------
	// Getters
	// -------------------------------------------------------------------------

	/** Return the listener associated with this controller. */
	public MapListener getMapListener() {
		return mapListener;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private MapListenerEvent getEvent(int eventBit, MouseEvent<?> event) {
		return new MapListenerEvent(eventBit, getScreenPosition(event), getTarget(event), mapPresenter.getViewPort(),
				event.isAltKeyDown(), event.isControlKeyDown(), event.isShiftKeyDown(), event.getNativeButton());
	}
}