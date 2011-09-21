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
package org.geomajas.gwt.client.handler;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
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
	 * Get the target DOM element of the (mouse/touch/...) event.
	 * 
	 * @param event
	 *            The mouse event itself.
	 * @return Returns the DOM element or null if something went wrong.
	 */
	Element getTarget(DomEvent<?> event);

	/**
	 * Return the ID of the DOM element of the (mouse/touch/...) event.
	 * 
	 * @param event
	 *            The mouse event itself.
	 * @return Returns the ID of the DOM element or null if something went wrong.
	 */
	String getTargetId(DomEvent<?> event);
}