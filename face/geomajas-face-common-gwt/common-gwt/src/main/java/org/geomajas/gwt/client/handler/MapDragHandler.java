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

import org.geomajas.geometry.Coordinate;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Handler for dragging on a map.<br>
 * TODO Is this really needed??
 * 
 * @author Pieter De Graef
 */
public interface MapDragHandler extends MapHandler {

	void onDragStart(HumanInputEvent<?> event, Coordinate position);

	void onDragMove(HumanInputEvent<?> event, Coordinate position);

	void onDragEnd(HumanInputEvent<?> event, Coordinate position);
}