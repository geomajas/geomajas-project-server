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

package org.geomajas.gwt.client.handler;

import org.geomajas.gwt.client.controller.MapEventParser;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Handler for either MouseUpEvents or single TouchEndEvents.
 * 
 * @author Pieter De Graef
 */
public interface MapUpHandler extends MapEventParser {

	void onUp(HumanInputEvent<?> event);
}