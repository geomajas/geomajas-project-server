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

package org.geomajas.plugin.editing.client.handler;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.shared.EventHandler;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractGeometryIndexMapHandler implements MapEventParser, EventHandler {

	protected GeometryEditService service;

	protected GeometryIndex index;

	protected MapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	@Override
	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		return eventParser.getLocation(event, renderSpace);
	}

	@Override
	public Element getTarget(HumanInputEvent<?> event) {
		return eventParser.getTarget(event);
	}

	public void setEventParser(MapEventParser eventParser) {
		this.eventParser = eventParser;
	}

	public GeometryIndexType getGeometryIndexType() {
		return service.getIndexService().getType(index);
	}

	public void setService(GeometryEditService service) {
		this.service = service;
	}

	public void setIndex(GeometryIndex index) {
		this.index = index;
	}

	public GeometryEditService getService() {
		return service;
	}

	public GeometryIndex getIndex() {
		return index;
	}
}