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

package org.geomajas.smartgwt.client.util;

import org.geomajas.geometry.Coordinate;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseEvent;

/**
 * <p>
 * Utility class regarding GWT mouse events. These functions can be very handy in the
 * {@link org.geomajas.smartgwt.client.controller.GraphicsController} implementations.
 * </p>
 * <p>
 * <b>Warning</b>: When events are triggered above a symbol using the Chrome browser, this class is unable to find the
 * target element.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class GwtEventUtil {

	private GwtEventUtil() {
		// do not allow instantiation.
	}

	/**
	 * Get the position of a mouse event.
	 * 
	 * @param event
	 *            The mouse event itself.
	 * @return Returns a coordinate holding the event's X and Y ordinate, where the origin is the upper left corner of
	 *         the DOM element catching the event. If used in a
	 *         {@link org.geomajas.smartgwt.client.controller.GraphicsController}, these are screen coordinates.
	 */
	public static Coordinate getPosition(MouseEvent<?> event) {
		return new Coordinate(event.getX(), event.getY());
	}

	/**
	 * Get the position of a mouse event.
	 * 
	 * @param event
	 *            The mouse event itself.
	 * @param offsetX
	 *            An extra value to be added to the X axis.
	 * @param offsetY
	 *            An extra value to be added to the Y axis.
	 * @return Returns a coordinate holding the event's X and Y ordinate, where the origin is the upper left corner of
	 *         the DOM element catching the event. If used in a
	 *         {@link org.geomajas.smartgwt.client.controller.GraphicsController}, these are screen coordinates.
	 */
	public static Coordinate getPosition(MouseEvent<?> event, int offsetX, int offsetY) {
		return new Coordinate(event.getX() + offsetX, event.getY() + offsetY);
	}

	/**
	 * Get the target DOM element of the mouse event.
	 * 
	 * @param event
	 *            The mouse event itself.
	 * @return Returns the DOM element or null if something went wrong.
	 */
	public static Element getTarget(DomEvent<?> event) {
		EventTarget target = event.getNativeEvent().getEventTarget();
		if (Element.is(target)) {
			return Element.as(target);
		}
		return null;
	}

	/**
	 * Return the ID of the DOM element of the mouse event.
	 * 
	 * @param event
	 *            The mouse event itself.
	 * @return Returns the ID of the DOM element or null if something went wrong.
	 */
	public static String getTargetId(DomEvent<?> event) {
		Element element = getTarget(event);
		if (element != null) {
			return element.getId();
		}
		return null;
	}
}
