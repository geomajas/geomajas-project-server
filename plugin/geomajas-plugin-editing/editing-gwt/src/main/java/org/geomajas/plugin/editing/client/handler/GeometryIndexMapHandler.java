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

package org.geomajas.plugin.editing.client.handler;

import org.geomajas.gwt.client.handler.MapEventParser;
import org.geomajas.gwt.client.handler.MapHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public abstract class GeometryIndexMapHandler implements MapHandler {

	protected GeometryEditingService service;

	protected GeometryIndex index;

	protected MapWidget mapWidget;

	protected MapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public MapEventParser getEventParser() {
		return eventParser;
	}

	public void setEventParser(MapEventParser eventParser) {
		this.eventParser = eventParser;
	}

	public void setMapWidget(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	}

	public GeometryIndexType getGeometryIndexType() {
		return service.getIndexService().getType(index);
	}

	public void setService(GeometryEditingService service) {
		this.service = service;
	}

	public void setIndex(GeometryIndex index) {
		this.index = index;
	}

	public GeometryEditingService getService() {
		return service;
	}

	public GeometryIndex getIndex() {
		return index;
	}
}