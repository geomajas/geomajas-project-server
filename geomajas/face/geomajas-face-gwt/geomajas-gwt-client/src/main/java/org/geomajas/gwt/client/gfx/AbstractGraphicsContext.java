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

package org.geomajas.gwt.client.gfx;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.util.DOM;

/**
 * Common methods for {@link org.geomajas.gwt.client.gfx.GraphicsContext}.
 *
 * @see GraphicsContext
 *
 * @author Kristof Heirwegh
 */
public abstract class AbstractGraphicsContext implements GraphicsContext {

	/**
	 * Set the controller of an element of this <code>GraphicsContext</code> so it can react to events.
	 *
	 * @param id The id of the element of which the controller should be set.
	 * @param controller The new <code>GraphicsController</code>
	 */
	public void setController(String id, GraphicsController controller) {
		// set them all
		setController(id, controller, Event.MOUSEEVENTS | Event.ONDBLCLICK | Event.ONMOUSEWHEEL);
	}

	/**
	 * Set the controller of an element of this <code>GraphicsContext</code> so it can react to events.
	 *
	 * @param id The id of the element of which the controller should be set.
	 * @param controller The new <code>GraphicsController</code>
	 * @param eventMask a bitmask to specify exactly which events you wish to listen for @see {@link Event} 
	 */
	public void setController(String id, GraphicsController controller, int eventMask) {
		Element element = DOM.getElementById(id);
		if (element != null) {
			DOM.setEventListener(element, new EventListenerHelper(element, controller, eventMask));
			DOM.sinkEvents(element, eventMask);
		}
	}

	// ----------------------------------------------------------

	/**
	 * Internal class to pass DOM-events to a GraphicsController
	 * @author Kristof Heirwegh
	 */
	private class EventListenerHelper implements EventListener {

		private final Element e;
		private final HandlerManager hm;

		public EventListenerHelper(Element e, GraphicsController gc, int eventMask) {
			this.e = e;
			this.hm = new HandlerManager(e);
			if ((Event.ONMOUSEDOWN & eventMask) > 0) {
				hm.addHandler(MouseDownEvent.getType(), gc);
			}
			if ((Event.ONMOUSEUP & eventMask) > 0) {
				hm.addHandler(MouseUpEvent.getType(), gc);
			}
			if ((Event.ONMOUSEOUT & eventMask) > 0) {
				hm.addHandler(MouseOutEvent.getType(), gc);
			}
			if ((Event.ONMOUSEOVER & eventMask) > 0) {
				hm.addHandler(MouseOverEvent.getType(), gc);
			}
			if ((Event.ONMOUSEMOVE & eventMask) > 0) {
				hm.addHandler(MouseMoveEvent.getType(), gc);
			}
			if ((Event.ONMOUSEWHEEL & eventMask) > 0) {
				hm.addHandler(MouseWheelEvent.getType(), gc);
			}
			if ((Event.ONDBLCLICK & eventMask) > 0) {
				hm.addHandler(DoubleClickEvent.getType(), gc);
			}
		}

		@SuppressWarnings("deprecation")
		public void onBrowserEvent(Event event) {
			// copied from Widget class to mimic behaviour of other widgets
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEOVER:
					// Only fire the mouse over event if it's coming from outside this
					// widget.
				case Event.ONMOUSEOUT:
					// Only fire the mouse out event if it's leaving this
					// widget.
					com.google.gwt.dom.client.Element related = event.getRelatedTarget();
					if (related != null && e.isOrHasChild(related)) {
						return;
					}
					break;
			}

			DomEvent.fireNativeEvent(event, new HasHandlers() {
				public void fireEvent(GwtEvent<?> event) {
					hm.fireEvent(event);
				}
			}, e);
		}
	}
}
