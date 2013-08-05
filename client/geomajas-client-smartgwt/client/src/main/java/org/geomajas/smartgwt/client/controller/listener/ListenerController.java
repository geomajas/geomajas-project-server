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

import org.geomajas.smartgwt.client.controller.AbstractGraphicsController;
import org.geomajas.smartgwt.client.widget.MapWidget;

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
		return new ListenerEvent(eventBit, getScreenPosition(event), getClientPosition(event), getTarget(event),
				getTransformer(), event.isAltKeyDown(), event.isControlKeyDown(), event.isShiftKeyDown(),
				event.getNativeButton());
	}
}