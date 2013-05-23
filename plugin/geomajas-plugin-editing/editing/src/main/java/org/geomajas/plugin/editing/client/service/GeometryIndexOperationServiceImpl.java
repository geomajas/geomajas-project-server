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

package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.operation.DeleteGeometryOperation;
import org.geomajas.plugin.editing.client.operation.DeleteVertexOperation;
import org.geomajas.plugin.editing.client.operation.GeometryIndexOperation;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.operation.InsertGeometryOperation;
import org.geomajas.plugin.editing.client.operation.InsertVertexOperation;
import org.geomajas.plugin.editing.client.operation.MoveVertexOperation;

import com.google.gwt.event.shared.EventBus;

/**
 * <p>
 * Service definition that defines possible operations on geometries during the editing process. Operations can be stand
 * alone or can be part of an operation sequence. Using an operations sequence wherein multiple operations are executed
 * will be regarded as a single operation unit for the undo and redo methods.
 * </p>
 * <p>
 * Take for example the moving of a vertex on the map. The user might drag a vertex over a lot of pixels, but every
 * intermediary change is executed as an operation (otherwise no events would be thrown and the renderer on the map
 * wouldn't know there was a change). When the user finally releases the vertex, dozens of move operations might already
 * have been executed. If the user would now have to click an undo button dozens of times to get the vertex back to it's
 * original position, that would not be very user-friendly.<br/>
 * On such occasions, an operation sequence would be used so that those dozens move operations are regarded as a single
 * unit, undone with a single call to undo.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexOperationServiceImpl implements GeometryIndexOperationService, GeometryEditStartHandler,
		GeometryEditStopHandler {

	private final Stack<OperationSequence> undoQueue;

	private final Stack<OperationSequence> redoQueue;

	private final GeometryEditServiceImpl service;

	private final GeometryIndexService indexService;

	private final EventBus eventBus;

	private OperationSequence current;

	// ------------------------------------------------------------------------
	// Protected constructor:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this service implementation with the required references.
	 * 
	 * @param service
	 *            The editing service that will delegate to this service. We still need it's other functionalities.
	 * @param eventBus
	 *            We wish to use the same event bus as the service that delegates to us.
	 */
	protected GeometryIndexOperationServiceImpl(GeometryEditServiceImpl service, EventBus eventBus) {
		this.service = service;
		this.eventBus = eventBus;
		indexService = service.getIndexService();
		undoQueue = new Stack<OperationSequence>();
		redoQueue = new Stack<OperationSequence>();

		service.addGeometryEditStartHandler(this);
		service.addGeometryEditStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	@Override
	public void onGeometryEditStart(GeometryEditStartEvent event) {
		undoQueue.clear();
		redoQueue.clear();
	}

	@Override
	public void onGeometryEditStop(GeometryEditStopEvent event) {
		undoQueue.clear();
		redoQueue.clear();
	}

	// ------------------------------------------------------------------------
	// Methods concerning "UNDO/REDO":
	// ------------------------------------------------------------------------

	@Override
	public boolean canUndo() {
		return undoQueue.size() > 0;
	}

	@Override
	public boolean canRedo() {
		return redoQueue.size() > 0;
	}

	@Override
	public void undo() throws GeometryOperationFailedException {
		if (!canUndo()) {
			throw new GeometryOperationFailedException("Cannot perform UNDO. No operation sequence could be found.");
		}
		stopOperationSequence();
		OperationSequence sequence = undoQueue.pop();
		redoQueue.add(sequence);
		sequence.undo(service.getGeometry());
		eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
	}

	@Override
	public void redo() throws GeometryOperationFailedException {
		if (!canRedo()) {
			throw new GeometryOperationFailedException("Cannot perform REDO. No operation sequence could be found.");
		}
		stopOperationSequence();
		OperationSequence sequence = redoQueue.pop();
		undoQueue.add(sequence);
		sequence.redo(service.getGeometry());
		eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
	}

	// ------------------------------------------------------------------------
	// Operation sequence manipulation:
	// ------------------------------------------------------------------------

	@Override
	public void startOperationSequence() throws GeometryOperationFailedException {
		if (current != null) {
			throw new GeometryOperationFailedException("An operation sequence has already been started.");
		}
		current = new OperationSequence();
		redoQueue.clear();
	}

	@Override
	public void stopOperationSequence() {
		if (isOperationSequenceActive() && !current.isEmpty()) {
			undoQueue.add(current);
			eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
		}
		current = null;
	}

	@Override
	public boolean isOperationSequenceActive() {
		return current != null;
	}

	// ------------------------------------------------------------------------
	// Supported operations:
	// ------------------------------------------------------------------------

	@Override
	public void move(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException {
		if (indices == null || coordinates == null || indices.size() == 0 || coordinates.size() == 0) {
			throw new GeometryOperationFailedException("Illegal arguments passed; nothing to move.");
		}

		Geometry geometry = service.getGeometry();
		OperationSequence seq = null;
		if (isOperationSequenceActive()) {
			seq = current;
		} else {
			seq = new OperationSequence();
			redoQueue.clear();
		}
		for (int i = 0; i < indices.size(); i++) {
			if (indexService.getType(indices.get(i)) == GeometryIndexType.TYPE_VERTEX) {
				GeometryIndexOperation op = new MoveVertexOperation(indexService, coordinates.get(i).get(0));
				op.execute(geometry, indices.get(i));
				seq.addOperation(op);
			} else {
				throw new GeometryOperationFailedException("Can only move vertices. Other types not suported.");
			}
		}
		if (!isOperationSequenceActive()) {
			undoQueue.add(seq);
		}
		eventBus.fireEvent(new GeometryEditMoveEvent(geometry, indices));
		if (!isOperationSequenceActive()) {
			eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
		}
	}

	@Override
	public void insert(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException {
		if (indices == null || indices.size() == 0) {
			throw new GeometryOperationFailedException("Illegal arguments passed; nothing to insert.");
		}

		Geometry geometry = service.getGeometry();
		OperationSequence seq = null;
		if (isOperationSequenceActive()) {
			seq = current;
		} else {
			seq = new OperationSequence();
			redoQueue.clear();
		}
		for (int i = 0; i < indices.size(); i++) {
			switch (indexService.getType(indices.get(i))) {
				case TYPE_GEOMETRY:
					if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
						Geometry child;
						if (indices.get(i).hasChild()) {
							child = new Geometry(Geometry.LINEAR_RING, 0, 0);
						} else {
							child = new Geometry(Geometry.POLYGON, 0, 0);
						}
						GeometryIndexOperation op = new InsertGeometryOperation(indexService, child);
						op.execute(geometry, indices.get(i));
						seq.addOperation(op);
					} else {
						throw new GeometryOperationFailedException("Cannot insert new geometries (yet).");
					}
					break;
				default:
					if (coordinates == null || coordinates.size() < indices.size()) {
						throw new GeometryOperationFailedException("No coordinates passed to insert.");
					}
					GeometryIndexOperation op2 = new InsertVertexOperation(indexService, coordinates.get(i).get(0));
					op2.execute(geometry, indices.get(i));
					seq.addOperation(op2);
			}
		}
		if (!isOperationSequenceActive()) {
			undoQueue.add(seq);
		}
		eventBus.fireEvent(new GeometryEditInsertEvent(geometry, indices));
		if (!isOperationSequenceActive()) {
			eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
		}
	}

	@Override
	public void remove(List<GeometryIndex> indices) throws GeometryOperationFailedException {
		if (indices == null || indices.size() == 0) {
			throw new GeometryOperationFailedException("Illegal arguments passed; nothing to delete.");
		}

		Geometry geometry = service.getGeometry();
		OperationSequence seq = null;
		if (isOperationSequenceActive()) {
			seq = current;
		} else {
			seq = new OperationSequence();
			redoQueue.clear();
		}
		for (int i = 0; i < indices.size(); i++) {
			GeometryIndexOperation op;
			switch (indexService.getType(indices.get(i))) {
				case TYPE_GEOMETRY:
					op = new DeleteGeometryOperation(indexService);
					break;
				default:
					op = new DeleteVertexOperation(indexService);
			}
			op.execute(geometry, indices.get(i));
			seq.addOperation(op);
		}
		if (!isOperationSequenceActive()) {
			undoQueue.add(seq);
		}
		eventBus.fireEvent(new GeometryEditRemoveEvent(geometry, indices));
		if (!isOperationSequenceActive()) {
			eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
		}
	}

	@Override
	public GeometryIndex addEmptyChild() throws GeometryOperationFailedException {
		return addEmptyChild(null);
	}
	
	public GeometryIndex addEmptyChild(GeometryIndex index) throws GeometryOperationFailedException {
		Geometry geometry = service.getGeometry();
		OperationSequence seq = null;
		if (isOperationSequenceActive()) {
			seq = current;
		} else {
			seq = new OperationSequence();
			redoQueue.clear();
		}

		GeometryIndexOperation operation = null;
		if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			operation = new InsertGeometryOperation(indexService, new Geometry(Geometry.LINEAR_RING,
					geometry.getSrid(), geometry.getPrecision()));
		} else if (Geometry.MULTI_POINT.equals(geometry.getGeometryType())) {
			operation = new InsertGeometryOperation(indexService, new Geometry(Geometry.POINT, geometry.getSrid(),
					geometry.getPrecision()));
		} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
			operation = new InsertGeometryOperation(indexService, new Geometry(Geometry.LINE_STRING,
					geometry.getSrid(), geometry.getPrecision()));
		} else if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			operation = new InsertGeometryOperation(indexService, new Geometry(Geometry.POLYGON, geometry.getSrid(),
					geometry.getPrecision()));
		}
		if (operation != null) {
			// Execute the operation:
			if (index == null) {
				if (geometry.getGeometries() == null) {
					index = indexService.create(GeometryIndexType.TYPE_GEOMETRY, 0);
				} else {
					index = indexService.create(GeometryIndexType.TYPE_GEOMETRY, geometry.getGeometries().length);
				}
			}
			operation.execute(geometry, index);

			// Add the operation to the queue (if not part of a sequence):
			seq.addOperation(operation);
			if (!isOperationSequenceActive()) {
				undoQueue.add(seq);
			}
			if (!isOperationSequenceActive()) {
				eventBus.fireEvent(new GeometryEditShapeChangedEvent(service.getGeometry()));
			}
			return index;
		}
		throw new GeometryOperationFailedException("Can't add a new geometry to the given geometry.");
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Private definition of a sequence of operations. All operations added to this sequence are regarded as a single
	 * entity.
	 * 
	 * @author Pieter De Graef
	 */
	private static class OperationSequence {

		private final List<GeometryIndexOperation> operations = new ArrayList<GeometryIndexOperation>();

		public void addOperation(GeometryIndexOperation operation) {
			operations.add(operation);
		}

		public Geometry undo(Geometry geometry) throws GeometryOperationFailedException {
			// TODO loop over all operations for undo/redo is not very performing.
			for (int i = operations.size() - 1; i >= 0; i--) {
				GeometryIndexOperation op = operations.get(i);
				geometry = op.getInverseOperation().execute(geometry, op.getGeometryIndex());
			}
			return geometry;
		}

		public Geometry redo(Geometry geometry) throws GeometryOperationFailedException {
			for (GeometryIndexOperation op : operations) {
				geometry = op.execute(geometry, op.getGeometryIndex());
			}
			return geometry;
		}

		public boolean isEmpty() {
			return operations.isEmpty();
		}
	}
}