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

package org.geomajas.plugin.editing.client.handler;

import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapUpHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * <p>
 * When a single selected vertex is dragged over this vertex, than it will snap to it. At this point, this handler will
 * stop any further propagation of the events, to make sure the base controllers can't mess up. As a results it is
 * recommended to install this snapping handler as the latest vertex handler.
 * </p>
 * <p>
 * At this moment only vertex snapping is supported, but later on, this may be applied to edges as well.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexSnapToDeleteHandler extends AbstractGeometryIndexMapHandler implements MouseOverHandler,
		MouseOutHandler, MapDragHandler, MapUpHandler, MouseMoveHandler {

	public void onMouseOver(MouseOverEvent event) {
		checkHover(event);
	}

	public void onMouseOut(MouseOutEvent event) {
		service.getIndexStateService().markForDeletionEnd(Collections.singletonList(index));
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (service.getIndexStateService().isMarkedForDeletion(index)) {
			event.stopPropagation();
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		checkHover(event);
	}

	public void onUp(HumanInputEvent<?> event) {
		if (service.getIndexStateService().isMarkedForDeletion(index)) {
			// If marked for deletion, remove on mouse up:
			try {
				List<GeometryIndex> toDelete = Collections.singletonList(index);
				service.getIndexStateService().markForDeletionEnd(toDelete);
				service.getIndexStateService().deselectAll();
				service.remove(toDelete);
			} catch (GeometryOperationFailedException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	/**
	 * Supports only vertices for now.
	 */
	private void checkHover(HumanInputEvent<?> event) {
		// Check: editing state, selection (there must be 1 index selected, but not this one):
		if (service.getEditingState() == GeometryEditState.DRAGGING
				&& !service.getIndexStateService().isSelected(index)
				&& service.getIndexStateService().getSelection().size() == 1) {
			GeometryIndex selected = service.getIndexStateService().getSelection().get(0);

			// Check: is the selected index of the same type, and is it a neighbor?
			if (service.getIndexService().getType(index) == service.getIndexService().getType(selected)
					&& service.getIndexService().isAdjacent(service.getGeometry(), index, selected)) {

				// Neighbor detected. Now see if there are enough vertices left to delete one:
				int siblingCount = service.getIndexService().getSiblingCount(service.getGeometry(), index);
				try {
					String geometryType = service.getIndexService().getGeometryType(service.getGeometry(), index);
					if (geometryType.equals(Geometry.LINE_STRING)) {
						if (siblingCount < 3) {
							return; // 2 vertices is the minimum for a LineString.
						}
					} else if (geometryType.equals(Geometry.LINEAR_RING)) {
						if (siblingCount < 5) {
							return; // 4 vertices is the minimum for a LinearRing.
						}
					} else {
						throw new IllegalStateException("Illegal type of geometry found.");
					}
				} catch (GeometryIndexNotFoundException e) {
					throw new IllegalStateException(e);
				}

				// Mark for deletion:
				if (!service.getIndexStateService().isMarkedForDeletion(index)) {
					service.getIndexStateService().markForDeletionBegin(Collections.singletonList(index));
				}

				// Than snap the selected vertex/edge to this one:
				if (service.getIndexService().getType(index) == GeometryIndexType.TYPE_VERTEX) {
					try {
						Coordinate location = service.getIndexService().getVertex(service.getGeometry(), index);
						service.move(Collections.singletonList(selected),
								Collections.singletonList(Collections.singletonList(location)));
						event.stopPropagation();
					} catch (GeometryIndexNotFoundException e) {
						throw new IllegalStateException(e);
					} catch (GeometryOperationFailedException e) {
						throw new IllegalStateException(e);
					}
				}
			}
		}
	}
}