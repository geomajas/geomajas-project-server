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

package org.geomajas.gwt.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import org.geomajas.geometry.Coordinate;

/**
 * Utility class regarding GWT mouse events. These functions can be very handy in the {@link GraphicsController}
 * implementations.
 *
 * @author Pieter De Graef
 */
public final class GwtEventUtil {

	private GwtEventUtil() {
	}

	/**
	 * Get the position of a mouse event.
	 *
	 * @param event
	 *            The mouse event itself.
	 * @return Returns a coordinate holding the event's X and Y ordinate, where the origin is the upper left corner of
	 *         the DOM element catching the event. If used in a {@link GraphicsController}, these are screen
	 *         coordinates.
	 */
	public static Coordinate getPosition(MouseEvent<?> event) {
		return new Coordinate(event.getX(), event.getY());
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
