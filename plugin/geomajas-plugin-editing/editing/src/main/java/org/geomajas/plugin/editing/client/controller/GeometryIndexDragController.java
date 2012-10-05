/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.snap.SnapService;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * <p>
 * Default controller to be used when in dragging mode during editing. It will allow the user to drag the selected
 * indices about.
 * </p>
 * <p>
 * This controller has support for snapping. If snapping is enabled, it will be applied on the first vertex that is both
 * selected and hightlighted. If this index exists and snaps, all other selected indices will translate as well, using
 * the same deltaX and deltaY.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexDragController extends AbstractGeometryIndexController {

	protected final Map<GeometryIndex, Coordinate> origins;

	protected GeometryIndex lastSelection;

	protected Coordinate origin;

	public GeometryIndexDragController(GeometryEditService service, SnapService snappingService,
			MapEventParser mapEventParser) {
		super(service, snappingService, mapEventParser);
		origins = new HashMap<GeometryIndex, Coordinate>();

		service.getIndexStateService().addGeometryIndexSelectedHandler(new GeometryIndexSelectedHandler() {

			public void onGeometryIndexSelected(GeometryIndexSelectedEvent event) {
				if (event.getIndices() != null && event.getIndices().size() >= 0) {
					lastSelection = event.getIndices().get(0);
				}
			}
		});
	}

	public void onDown(HumanInputEvent<?> event) {
		origin = getOrigin(event);
		origins.clear();
		try {
			for (GeometryIndex selected : service.getIndexStateService().getSelection()) {
				Coordinate vertex;
				vertex = service.getIndexService().getVertex(service.getGeometry(), selected);
				origins.put(selected, vertex);
			}
		} catch (GeometryIndexNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		// Get the position in world space:
		Coordinate worldPos = getLocationWithinMaxBounds(event);

		// In case snapping is enabled, this might influence the target position:
		if (snappingEnabled) {

			// Snapping is calculated on a selected and highlighted vertex:
			if (lastSelection != null) {
				worldPos = snappingService.snap(worldPos);

				// Set index snapping state:
				if (snappingService.hasSnapped()) {
					service.getIndexStateService().snappingBegin(Collections.singletonList(lastSelection));
				} else {
					service.getIndexStateService().snappingEndAll();
				}
			}
		}

		// Calculate the delta relative to the drag origin:
		double deltaX = worldPos.getX() - origin.getX();
		double deltaY = worldPos.getY() - origin.getY();

		// Go over all selected indices and find their new positions:
		List<List<Coordinate>> coordinateList = new ArrayList<List<Coordinate>>();
		for (GeometryIndex selected : service.getIndexStateService().getSelection()) {
			switch (service.getIndexService().getType(selected)) {
				case TYPE_VERTEX:
					// Get the original position for the selected vertex:
					Coordinate selOr = origins.get(selected);
					Coordinate currentLocation = new Coordinate(selOr.getX() + deltaX, selOr.getY() + deltaY);
					coordinateList.add(Collections.singletonList(currentLocation));
					break;
				case TYPE_EDGE:
				default:
					throw new RuntimeException("Not really a RTE. Just not implemented ;-)");
			}
		}

		// Execute the move operation:
		try {
			service.move(service.getIndexStateService().getSelection(), coordinateList);
		} catch (GeometryOperationFailedException e) {
			throw new IllegalStateException(e);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		service.stopOperationSequence();
		service.setEditingState(GeometryEditState.IDLE);
		service.getIndexStateService().snappingEndAll();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private Coordinate getOrigin(HumanInputEvent<?> event) {
		// Get the original location for the lastly selected index:
		if (lastSelection != null) {
			try {
				return service.getIndexService().getVertex(service.getGeometry(), lastSelection);
			} catch (GeometryIndexNotFoundException e) {
			}
		}

		// Backup plan: mouse location.
		return getLocation(event, RenderSpace.SCREEN);
	}
}