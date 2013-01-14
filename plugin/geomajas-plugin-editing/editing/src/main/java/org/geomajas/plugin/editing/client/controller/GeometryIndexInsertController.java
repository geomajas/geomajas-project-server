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

package org.geomajas.plugin.editing.client.controller;

import java.util.Collections;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.snap.SnapService;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.user.client.Window;

/**
 * Controller that inserts vertices by clicking/tapping on the map.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexInsertController extends AbstractGeometryIndexController {

	public GeometryIndexInsertController(GeometryEditService service, SnapService snappingService,
			MapEventParser mapEventParser) {
		super(service, snappingService, mapEventParser);
	}

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.INSERTING && isRightMouseButton(event)) {
			service.setEditingState(GeometryEditState.IDLE);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		// Only insert when service is in the correct state:
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			try {
				// Insert the location at the given index:
				GeometryIndex insertIndex = service.getInsertIndex();
				Coordinate location = getSnappedLocationWithinMaxBounds(event);
				service.insert(Collections.singletonList(insertIndex),
						Collections.singletonList(Collections.singletonList(location)));
				service.setTentativeMoveOrigin(location);

				// Update the insert index (if allowed):
				if (!service.getGeometry().getGeometryType().equals(Geometry.POINT)
						&& !service.getGeometry().getGeometryType().equals(Geometry.MULTI_POINT)) {
					service.setInsertIndex(service.getIndexService().getNextVertex(insertIndex));
				} else {
					// If the case of a point, no more inserting:
					service.setEditingState(GeometryEditState.IDLE);
				}
			} catch (GeometryOperationFailedException e) {
				Window.alert("Exception during editing: " + e.getMessage());
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			Coordinate location = getLocation(event, RenderSpace.WORLD);
			if (snappingEnabled) {
				Coordinate result = snappingService.snap(location);
				if (snappingService.hasSnapped()) {
					service.setTentativeMoveLocation(result);
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
		if (service.getEditingState() == GeometryEditState.INSERTING) {
			service.setEditingState(GeometryEditState.IDLE);
		}
	}
}