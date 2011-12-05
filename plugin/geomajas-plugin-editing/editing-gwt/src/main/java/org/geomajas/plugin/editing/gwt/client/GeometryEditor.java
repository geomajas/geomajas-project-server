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

package org.geomajas.plugin.editing.gwt.client;

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingServiceImpl;
import org.geomajas.plugin.editing.client.snapping.SnappingService;
import org.geomajas.plugin.editing.gwt.client.controller.EditGeometryBaseController;
import org.geomajas.plugin.editing.gwt.client.gfx.GeometryRenderer;

/**
 * Top level geometry editor for the GWT face.
 * 
 * @author Pieter De Graef
 */
public class GeometryEditor implements GeometryEditStartHandler, GeometryEditStopHandler {

	private final MapWidget mapWidget;

	private final GeometryEditingService service;

	private final SnappingService snappingService;

	private final GeometryRenderer renderer;

	private final EditGeometryBaseController baseController;

	private GraphicsController previousController;

	private boolean isBusyEditing;

	// Options:

	private boolean zoomOnStart;

	// Constructors:

	public GeometryEditor(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		service = new GeometryEditingServiceImpl();
		service.addGeometryEditStartHandler(this);
		service.addGeometryEditStopHandler(this);

		snappingService = new SnappingService();
		baseController = new EditGeometryBaseController(mapWidget, service, snappingService);
		renderer = new GeometryRenderer(mapWidget, service, baseController);
		
		snappingService.addCoordinateSnapHandler(renderer);
		
		service.addGeometryEditStartHandler(renderer);
		service.addGeometryEditStopHandler(renderer);
		service.addGeometryEditShapeChangedHandler(renderer);
		service.addGeometryEditMoveHandler(renderer);
		service.addGeometryEditChangeStateHandler(renderer);
		service.addGeometryEditTentativeMoveHandler(renderer);

		service.getIndexStateService().addGeometryIndexSelectedHandler(renderer);
		service.getIndexStateService().addGeometryIndexDeselectedHandler(renderer);

		service.getIndexStateService().addGeometryIndexEnabledHandler(renderer);
		service.getIndexStateService().addGeometryIndexDisabledHandler(renderer);

		service.getIndexStateService().addGeometryIndexHighlightBeginHandler(renderer);
		service.getIndexStateService().addGeometryIndexHighlightEndHandler(renderer);

		service.getIndexStateService().addGeometryIndexMarkForDeletionBeginHandler(renderer);
		service.getIndexStateService().addGeometryIndexMarkForDeletionEndHandler(renderer);

		mapWidget.getMapModel().getMapView().addMapViewChangedHandler(new MapViewChangedHandler() {

			public void onMapViewChanged(MapViewChangedEvent event) {
				if (isBusyEditing && isSnapOnDrag() && !event.isSameScaleLevel()) {
					org.geomajas.geometry.Bbox bounds = new org.geomajas.geometry.Bbox(event.getBounds().getX(), event
							.getBounds().getY(), event.getBounds().getWidth(), event.getBounds().getHeight());
					snappingService.update(bounds);
				}
			}
		});
	}

	// GeometryEditWorkflowHandler implementation:

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		isBusyEditing = true;

		// Initialize controllers and painters:
		previousController = mapWidget.getController();
		mapWidget.setController(baseController);

		if (zoomOnStart) {
			// TODO Zoom darnit...
		}
		if (isBusyEditing && isSnapOnDrag()) {
			Bbox bounds = mapWidget.getMapModel().getMapView().getBounds();
			snappingService.update(new org.geomajas.geometry.Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(),
					bounds.getHeight()));
		}
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		isBusyEditing = false;

		// Cleanup controllers and painters.
		mapWidget.setController(previousController);
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

	public GeometryRenderer getRenderer() {
		return renderer;
	}

	public SnappingService getSnappingService() {
		return snappingService;
	}

	public boolean isSnapOnDrag() {
		return baseController.getDragController().isSnappingEnabled();
	}

	public void setSnapOnDrag(boolean snapOnDrag) {
		baseController.getDragController().setSnappingEnabled(snapOnDrag);
	}
	
	public boolean isSnapOnInsert() {
		return baseController.getInsertController().isSnappingEnabled();
	}
	
	public void setSnapOnInsert(boolean snapOnInsert) {
		baseController.getInsertController().setSnappingEnabled(snapOnInsert);
	}

	public boolean isBusyEditing() {
		return isBusyEditing;
	}
}