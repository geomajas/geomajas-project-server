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

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Controller that inserts vertices by clicking/tapping on the map.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexInsertController extends AbstractController {

	private GeometryEditingService service;

	public GeometryIndexInsertController(GeometryEditingService service, MapEventParser mapEventParser) {
		super(mapEventParser, service.getEditingState() == GeometryEditingState.DRAGGING);
		this.service = service;
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
				service.insert(Collections.singletonList(insertIndex),
						Collections.singletonList(Collections.singletonList(getLocation(event, RenderSpace.WORLD))));

				// Update the insert index (if allowed):
				if (!service.getGeometry().getGeometryType().equals(Geometry.POINT)
						&& !service.getGeometry().getGeometryType().equals(Geometry.MULTI_POINT)) {
					service.setInsertIndex(service.getIndexService().getNextVertex(insertIndex));
				} else {
					// If the case of a point, no more inserting:
					service.setEditingState(GeometryEditingState.IDLE);
				}
			} catch (GeometryIndexNotFoundException e) {
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING) {
			service.setInsertMoveLocation(getLocation(event, RenderSpace.SCREEN));
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING) {
			service.setEditingState(GeometryEditingState.IDLE);
		}
	}
}