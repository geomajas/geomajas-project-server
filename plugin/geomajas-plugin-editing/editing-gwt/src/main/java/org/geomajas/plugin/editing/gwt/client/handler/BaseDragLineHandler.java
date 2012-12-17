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
package org.geomajas.plugin.editing.gwt.client.handler;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Abstract editing handler that exposes the drag line information (up to 3 coordinates) to its subclasses.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class BaseDragLineHandler implements GeometryEditMoveHandler, GeometryEditShapeChangedHandler,
		GeometryEditChangeStateHandler, GeometryEditTentativeMoveHandler {

	protected GeometryEditService editService;

	protected List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	public BaseDragLineHandler(GeometryEditService editService) {
		this.editService = editService;
	}

	/**
	 * Register handlers. Activates this handler.
	 */
	public void register() {
		registrations.add(editService.addGeometryEditMoveHandler(this));
		registrations.add(editService.addGeometryEditShapeChangedHandler(this));
		registrations.add(editService.addGeometryEditTentativeMoveHandler(this));
		registrations.add(editService.addGeometryEditChangeStateHandler(this));
	}

	/**
	 * Unregister handlers.
	 */
	public void unregister() {
		for (HandlerRegistration registration : registrations) {
			registration.removeHandler();
		}
		registrations.clear();
	}
	
	/**
	 * Is this handler registered ?
	 * 
	 * @return true if registered
	 */
	public boolean isRegistered() {
		return registrations.size() > 0;
	}

	@Override
	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		onDragStopped();
	}

	@Override
	public void onChangeEditingState(GeometryEditChangeStateEvent event) {
		if (event.getEditingState() == GeometryEditState.IDLE) {
			onDragStopped();
		}
	}

	@Override
	public void onTentativeMove(GeometryEditTentativeMoveEvent event) {
		try {
			Coordinate[] vertices = editService.getIndexService().getSiblingVertices(editService.getGeometry(),
					editService.getInsertIndex());
			String geometryType = editService.getIndexService().getGeometryType(editService.getGeometry(),
					editService.getInsertIndex());
			if (vertices != null && Geometry.LINE_STRING.equals(geometryType)) {
				// line string has single drag line
				Coordinate dragPoint = event.getCurrentPosition();
				Coordinate startA = event.getOrigin();
				onDrag(dragPoint, startA, null);
			} else if (vertices != null && Geometry.LINEAR_RING.equals(geometryType)) {
				// linear ring has one or two drag lines
				Coordinate dragPoint = event.getCurrentPosition();
				Coordinate startA = event.getOrigin();
				if (vertices.length > 2) {
					Coordinate startB = vertices[vertices.length - 1];
					onDrag(dragPoint, startA, startB);
				} else {
					onDrag(dragPoint, startA, null);
				}
			}
		} catch (GeometryIndexNotFoundException e) {
		}
	}

	@Override
	public void onGeometryEditMove(GeometryEditMoveEvent event) {
		if (event.getIndices().size() == 1) {
			GeometryIndex index = event.getIndices().get(0);
			if (GeometryIndexType.TYPE_VERTEX == editService.getIndexService().getType(index)) {
				Geometry geometry = editService.getGeometry();
				try {
					Coordinate dragPoint = editService.getIndexService().getVertex(geometry, index);
					Coordinate startA = null;
					Coordinate startB = null;
					List<GeometryIndex> vertices = editService.getIndexService().getAdjacentVertices(geometry, index);
					if (vertices.size() == 1) {
						startA = editService.getIndexService().getVertex(geometry, vertices.get(0));
					}
					if (vertices.size() == 2) {
						startA = editService.getIndexService().getVertex(geometry, vertices.get(0));
						startB = editService.getIndexService().getVertex(geometry, vertices.get(1));
					}
					onDrag(dragPoint, startA, startB);
				} catch (GeometryIndexNotFoundException e) {
				}
			}
		}
	}

	/**
	 * Called when one or more drag lines are shown.
	 * 
	 * @param dragPoint point that is being dragged or about to be created
	 * @param startA start point of first drag line (could be labeled A)
	 * @param startB start point of second drag line (could be labeled B)
	 */
	protected abstract void onDrag(Coordinate dragPoint, Coordinate startA, Coordinate startB);

	/**
	 * Called when drag lines have disappeared.
	 */
	protected abstract void onDragStopped();

}
