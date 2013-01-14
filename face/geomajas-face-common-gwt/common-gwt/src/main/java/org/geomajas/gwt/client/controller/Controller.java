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

package org.geomajas.gwt.client.controller;

import org.geomajas.annotation.Api;

import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;

/**
 * <p>
 * General interface for a controller set on a map. It should implement all of the available mouse and touch handling
 * events, although each face provides abstract implementations which are perfectly suitable to start working from.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Controller extends MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler,
		MouseOverHandler, MouseWheelHandler, DoubleClickHandler, TouchStartHandler, TouchEndHandler, TouchMoveHandler,
		TouchCancelHandler, MapEventParser {

	/**
	 * Is the given event the result of a mouse event coming from the right mouse button?
	 * 
	 * @param event
	 *            The given event. May not even be a mouse event.
	 * @return true or false.
	 */
	boolean isRightMouseButton(HumanInputEvent<?> event);
}