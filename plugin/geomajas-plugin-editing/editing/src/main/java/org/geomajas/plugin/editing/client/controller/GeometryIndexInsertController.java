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

import java.util.Collections;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.snapping.SnappingService;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.user.client.Window;

/**
 * Controller that inserts vertices by clicking/tapping on the map.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexInsertController extends AbstractController {

	private final GeometryEditingService service;

	private final SnappingService snappingService;

	private boolean snappingEnabled;

	public GeometryIndexInsertController(GeometryEditingService service, SnappingService snappingService,
			MapEventParser mapEventParser) {
		super(mapEventParser, service.getEditingState() == GeometryEditingState.DRAGGING);
		this.service = service;
		this.snappingService = snappingService;
	}

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING && isRightMouseButton(event)) {
			service.setEditingState(GeometryEditingState.IDLE);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		// Only insert when service is in the correct state:
		if (service.getEditingState() == GeometryEditingState.INSERTING) {
			try {
				// Insert the location at the given index:
				GeometryIndex insertIndex = service.getInsertIndex();
				Coordinate location = getLocation(event, RenderSpace.WORLD);
				if (snappingEnabled) {
					location = snappingService.snap(location);
				}
				service.insert(Collections.singletonList(insertIndex),
						Collections.singletonList(Collections.singletonList(location)));
				service.setTentativeMoveOrigin(location);

				// Update the insert index (if allowed):
				if (!service.getGeometry().getGeometryType().equals(Geometry.POINT)
						&& !service.getGeometry().getGeometryType().equals(Geometry.MULTI_POINT)) {
					service.setInsertIndex(service.getIndexService().getNextVertex(insertIndex));
				} else {
					// If the case of a point, no more inserting:
					service.setEditingState(GeometryEditingState.IDLE);
				}
			} catch (GeometryOperationFailedException e) {
				Window.alert("Exception during editing: " + e.getMessage());
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING) {
			Coordinate location = getLocation(event, RenderSpace.WORLD);
			if (snappingEnabled) {
				Coordinate result = snappingService.snap(location);
				if (snappingService.hasSnapped()) {
					service.setTentativeMoveLocation(result);
					//service.getIndexStateService().snappingBegin(Collections.singletonList(index));
				} else {
					service.setTentativeMoveLocation(location);
					service.getIndexStateService().snappingEndAll();
				}
			} else {
				service.setTentativeMoveLocation(location);
				service.getIndexStateService().snappingEndAll();
			}
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING) {
			service.setEditingState(GeometryEditingState.IDLE);
		}
	}

	public boolean isSnappingEnabled() {
		return snappingEnabled;
	}

	public void setSnappingEnabled(boolean snappingEnabled) {
		this.snappingEnabled = snappingEnabled;
	}
}