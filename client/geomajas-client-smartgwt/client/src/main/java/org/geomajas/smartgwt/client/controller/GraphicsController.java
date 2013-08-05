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

package org.geomajas.smartgwt.client.controller;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.controller.Controller;

// @extract-start GraphicsController, GraphicsController
/**
 * <p>
 * General interface for a controller set on a {@link org.geomajas.smartgwt.client.widget.MapWidget}.
 * It should implement all of the available mouse handling events.
 * </p>
 * <p>
 * These controllers can do anything they want with these mouse events, and as a result only one controller can be
 * active on a map at any given time. This is also the difference between controllers and listeners. A
 * <code>Listener</code> passively listens to mouse events without ever interfering with them. Therefore there is no
 * maximum of listeners that can be active on a map at any given time.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface GraphicsController extends /*MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler,
		MouseOverHandler, MouseWheelHandler, DoubleClickHandler*/Controller {

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
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
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
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
	void setOffsetX(int offsetX);

	/**
	 * An offset along the Y-axis expressed in pixels for event coordinates. Used when controllers are placed on
	 * specific elements that have such an offset as compared to the origin of the map. Event from such elements have
	 * X,Y coordinates relative from their own position, but need this extra offset so that we can still calculate the
	 * correct screen and world position.
	 * 
	 * @since 1.8.0
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
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
	 * @deprecated Since 1.10, due to http://jira.geomajas.org/browse/GWT-354. No longer used.
	 */
	@Deprecated
	void setOffsetY(int offsetY);
}
// @extract-end
