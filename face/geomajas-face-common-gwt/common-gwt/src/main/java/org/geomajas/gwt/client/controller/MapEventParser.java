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
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Utility methods for acquiring information out of events that come from the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MapEventParser {

	/**
	 * Get the location of the (mouse/touch/...) event, expressed in the requested {@link RenderSpace}.
	 * 
	 * @param event
	 *            The original event.
	 * @param renderSpace
	 *            The requested render space.
	 * @return Returns the location as a coordinate in the requested render space.
	 */
	Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace);

	/**
	 * Get the target element from a mouse or touch event.
	 * 
	 * @param event
	 *            The event
	 * @return The HTML element that first caught the event.
	 */
	Element getTarget(HumanInputEvent<?> event);
}