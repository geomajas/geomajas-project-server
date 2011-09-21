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

package org.geomajas.plugin.editing.client.controller;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractGeometryIndexController extends AbstractGeometryEditController {

	protected GeometryIndex index;

	protected AbstractGeometryIndexController(MapWidget mapWidget, GeometryEditingService service, 
			GeometryIndex index) {
		super(mapWidget, service);
		this.index = index;
	}

	public GeometryIndexType getType() {
		return service.getIndexService().getType(index);
	}

	public GeometryIndex getIndex() {
		return index;
	}
}