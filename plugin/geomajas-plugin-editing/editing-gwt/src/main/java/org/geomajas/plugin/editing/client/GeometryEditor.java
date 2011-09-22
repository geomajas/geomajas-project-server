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

package org.geomajas.plugin.editing.client;

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.controller.EditGeometryBaseController;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditWorkflowHandler;
import org.geomajas.plugin.editing.client.gfx.GeometryRenderer;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.impl.GeometryEditingServiceImpl;

/**
 * Top level geometry editor for the GWT face.
 * 
 * @author Pieter De Graef
 */
public class GeometryEditor implements GeometryEditWorkflowHandler {

	private MapWidget mapWidget;

	private GeometryEditingService service;

	private GraphicsController previousController;

	private EditGeometryBaseController baseController;

	private GeometryRenderer renderer;

	// Options:

	private boolean zoomOnStart;

	// Constructors:

	public GeometryEditor(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		service = new GeometryEditingServiceImpl();
		service.addGeometryEditWorkflowHandler(this);
		baseController = new EditGeometryBaseController(mapWidget, service); // use factories?
		renderer = new GeometryRenderer(mapWidget, service, baseController);
	}

	// GeometryEditWorkflowHandler implementation:

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		// Initialize controllers and painters:
		previousController = mapWidget.getController();
		mapWidget.setController(baseController);
		
		if (zoomOnStart) {
			// TODO Zoom damnit...
		}
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		// Cleanup controllers and painters.
		mapWidget.setController(previousController);
	}

	// Editing manipulation methods:

	public void highLight(GeometryIndex index) {

	}

	// Getters and setters:

	public MapWidget getMapWidget() {
		return mapWidget;
	}

	public GeometryEditingService getService() {
		return service;
	}

	public boolean isZoomOnStart() {
		return zoomOnStart;
	}

	public void setZoomOnStart(boolean zoomOnStart) {
		this.zoomOnStart = zoomOnStart;
	}
}