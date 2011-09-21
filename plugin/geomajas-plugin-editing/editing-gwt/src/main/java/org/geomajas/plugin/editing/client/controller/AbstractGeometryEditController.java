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

import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractGeometryEditController extends AbstractGraphicsController {

	protected GeometryEditingService service;

	protected AbstractGeometryEditController(MapWidget mapWidget, GeometryEditingService service) {
		super(mapWidget);
		this.service = service;
	}

	public GeometryEditingService getService() {
		return service;
	}

	public void setService(GeometryEditingService service) {
		this.service = service;
	}
}