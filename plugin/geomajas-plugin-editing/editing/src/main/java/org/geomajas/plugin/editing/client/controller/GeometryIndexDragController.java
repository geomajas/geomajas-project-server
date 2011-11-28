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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.client.snapping.SnappingService;

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
public class GeometryIndexDragController extends AbstractController {

	private final GeometryEditingService service;

	private final SnappingService snappingService;

	private Coordinate previous;

	private boolean snappingEnabled;

	public GeometryIndexDragController(GeometryEditingService service, SnappingService snappingService,
			MapEventParser mapEventParser) {
		super(mapEventParser, service.getEditingState() == GeometryEditingState.DRAGGING);
		this.snappingService = snappingService;
		this.service = service;
	}

	public void onDown(HumanInputEvent<?> event) {
		previous = getLocation(event, RenderSpace.WORLD);
	}

	public void onDrag(HumanInputEvent<?> event) {
		// First calculate moving delta:
		Coordinate worldPos = getLocation(event, RenderSpace.WORLD);
		double deltaX = worldPos.getX() - previous.getX();
		double deltaY = worldPos.getY() - previous.getY();

		try {
			// If snapping is enabled, it might influence deltaX and deltY:
			if (snappingEnabled) {
				// Snapping is calculated on a selected and highlighted vertex:
				GeometryIndex target = getSelectedAndHighlightedVertex();
				if (target != null) {
					// Check snapping on this target to influence deltaX and deltaY:
					Coordinate to = snappingService.snap(new Coordinate(worldPos.getX() + deltaX, worldPos.getY()
							+ deltaY));
					deltaX = to.getX() - worldPos.getX();
					deltaY = to.getY() - worldPos.getY();

					if (snappingService.hasSnapped()) {
						service.getIndexStateService().snappingBegin(Collections.singletonList(target));
					} else {
						service.getIndexStateService().snappingEndAll();
					}
				} else {
					service.getIndexStateService().snappingEndAll();
				}
			}

			// Go over all selected indices and find their new positions:
			List<List<Coordinate>> coordinateList = new ArrayList<List<Coordinate>>();
			for (GeometryIndex selected : service.getIndexStateService().getSelection()) {
				switch (service.getIndexService().getType(selected)) {
					case TYPE_VERTEX:
						Coordinate to = new Coordinate(worldPos.getX() + deltaX, worldPos.getY() + deltaY);
						coordinateList.add(Collections.singletonList(to));
						break;
					case TYPE_EDGE:
					default:
						throw new RuntimeException("Not really a RTE. Just not implemented ;-)");
				}
			}

			// Execute the move operation:
			service.move(service.getIndexStateService().getSelection(), coordinateList);
		} catch (GeometryOperationFailedException e) {
			// Something went wrong;
		}

		previous = worldPos;
	}

	public void onUp(HumanInputEvent<?> event) {
		previous = null;
		service.stopOperationSequence();
		service.setEditingState(GeometryEditingState.IDLE);
	}

	public boolean isSnappingEnabled() {
		return snappingEnabled;
	}

	public void setSnappingEnabled(boolean snappingEnabled) {
		this.snappingEnabled = snappingEnabled;
	}

	private GeometryIndex getSelectedAndHighlightedVertex() {
		for (GeometryIndex selected : service.getIndexStateService().getSelection()) {
			if (service.getIndexService().getType(selected) == GeometryIndexType.TYPE_VERTEX
					&& service.getIndexStateService().isHightlighted(selected)) {
				return selected;
			}
		}
		return service.getIndexStateService().getSelection().get(0);
	}
}