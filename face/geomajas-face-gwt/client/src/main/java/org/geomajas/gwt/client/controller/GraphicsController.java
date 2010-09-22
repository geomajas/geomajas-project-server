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

package org.geomajas.gwt.client.controller;

import org.geomajas.global.Api;

import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;

// @extract-start GraphicsController, GraphicsController
/**
 * General interface for a controller set on a {@link org.geomajas.gwt.client.widget.MapWidget}. It should implement all
 * of the available mouse handling events.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface GraphicsController extends MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler,
		MouseOverHandler, MouseWheelHandler, DoubleClickHandler {

	/**
	 * Function executed when the controller instance is applied on the map.
	 */
	void onActivate();

	/**
	 * Function executed when the controller instance is removed from the map.
	 */
	void onDeactivate();

	/**
	 * An offset along the X-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @since 1.8.0
	 */
	int getOffsetX();

	/**
	 * An offset along the X-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @param offsetX
	 *            Set the actual offset value in pixels.
	 * 
	 * @since 1.8.0
	 */
	void setOffsetX(int offsetX);

	/**
	 * An offset along the Y-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @since 1.8.0
	 */
	int getOffsetY();

	/**
	 * An offset along the Y-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @param offsetY
	 *            Set the actual offset value in pixels.
	 * @since 1.8.0
	 */
	void setOffsetY(int offsetY);
}
// @extract-end
