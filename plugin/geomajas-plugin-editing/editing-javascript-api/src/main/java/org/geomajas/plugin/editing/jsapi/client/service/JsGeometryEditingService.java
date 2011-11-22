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

package org.geomajas.plugin.editing.jsapi.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveEvent;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingServiceImpl;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditInsertHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditMoveHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditRemoveHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.jsapi.client.event.GeometryEditTentativeMoveHandler;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterBaseActual.JsArrayObject;
import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * ...
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryEditingService")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryEditingService implements Exportable {

	private GeometryEditingService delegate;

	private JsGeometryIndexStateService stateService;

	public JsGeometryEditingService() {
		delegate = new GeometryEditingServiceImpl();
		stateService = new JsGeometryIndexStateService(delegate.getIndexStateService());
	}

	// Registering event handlers:

	/**
	 * Register a {@link GeometryEditStartHandler} that catches events that signal the editing process has started.
	 * 
	 * @param handler
	 *            The {@link GeometryEditStartHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditStartHandler(final GeometryEditStartHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditStartHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditStartHandler() {

			public void onGeometryEditStart(GeometryEditStartEvent event) {
				handler.onGeometryEditStart(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditStartEvent(
						event.getGeometry()));
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryEditStartHandler(h) });
	}

	/**
	 * Register a {@link GeometryEditStopHandler} that catches events that signal the editing process has ended.
	 * 
	 * @param handler
	 *            The {@link GeometryEditStopHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditStopHandler(final GeometryEditStopHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditStopHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditStopHandler() {

			public void onGeometryEditStop(GeometryEditStopEvent event) {
				handler.onGeometryEditStop(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditStopEvent(
						event.getGeometry()));
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryEditStopHandler(h) });
	}

	/**
	 * Register a {@link GeometryEditChangeStateHandler} to listen to events the mark changes in the general editing
	 * state. This general state can say we're busy idling, dragging vertices/edges/geometries or inserting. The reason
	 * why this exists is for the general editing controllers to know how to behave.
	 * 
	 * @param handler
	 *            The {@link GeometryEditChangeStateHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditChangeStateHandler(final GeometryEditChangeStateHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler() {

			public void onChangeEditingState(GeometryEditChangeStateEvent event) {
				String state = "idle";
				switch (event.getEditingState()) {
					case INSERTING:
						state = "inserting";
						break;
					case DRAGGING:
						state = "dragging";
				}
				org.geomajas.plugin.editing.jsapi.client.event.GeometryEditChangeStateEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditChangeStateEvent(state);
				handler.onChangeEditingState(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryEditChangeStateHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryEditTentativeMoveHandler} to listen to mouse move events that point to tentative
	 * moving. These move events don't have to commit to anything. They may result in operations being executed, they
	 * may not. This is meant mainly for the renderers to respond quickly to user interaction. (user friendliness and
	 * all that jazz).
	 * 
	 * @param handler
	 *            The {@link GeometryEditTentativeMoveHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditTentativeMoveHandler(final GeometryEditTentativeMoveHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveHandler() {

			public void onTentativeMove(GeometryEditTentativeMoveEvent event) {
				handler.onInsertMove(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditTentativeMoveEvent(
						delegate.getTentativeMoveOrigin(), delegate.getTentativeMoveLocation()));
			}
		};
		HandlerRegistration registration = delegate.addGeometryEditTentativeMoveHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryEditInsertHandler} to listen to insert events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditInsertHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditInsertHandler(final GeometryEditInsertHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditInsertHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditInsertHandler() {

			public void onGeometryEditInsert(GeometryEditInsertEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.GeometryEditInsertEvent e;
				List<GeometryIndex> indexes = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditInsertEvent(event.getGeometry(),
						indexes.toArray(new GeometryIndex[indexes.size()]));
				handler.onGeometryEditInsert(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryEditInsertHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryEditMoveHandler} to listen to move(translate) events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditMoveHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditMoveHandler(final GeometryEditMoveHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler() {

			public void onGeometryEditMove(GeometryEditMoveEvent event) {
				List<GeometryIndex> indexes = event.getIndices();
				handler.onGeometryEditMove(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditMoveEvent(
						event.getGeometry(), indexes.toArray(new GeometryIndex[indexes.size()])));
			}
		};
		HandlerRegistration registration = delegate.addGeometryEditMoveHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryEditRemoveHandler} to listen to delete events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditRemoveHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditRemoveHandler(final GeometryEditRemoveHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditRemoveHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditRemoveHandler() {

			public void onGeometryEditRemove(GeometryEditRemoveEvent event) {
				List<GeometryIndex> indexes = event.getIndices();
				handler.onGeometryEditRemove(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditRemoveEvent(
						event.getGeometry(), indexes.toArray(new GeometryIndex[indexes.size()])));
			}
		};
		HandlerRegistration registration = delegate.addGeometryEditRemoveHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryEditShapeChangedHandler} to listen to operation events (moving, inserting, deleting,
	 * ...) of sub-geometries, vertices and edges but also listens to undo/redo events. Anything the changes the
	 * geometry shape basically.
	 * 
	 * @param handler
	 *            The {@link GeometryEditShapeChangedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryEditShapeChangedHandler(final GeometryEditShapeChangedHandler handler) {
		org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler h;
		h = new org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler() {

			public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
				handler.onShapeChanged(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditShapeChangedEvent(
						getGeometry()));
			}
		};
		HandlerRegistration registration = delegate.addGeometryEditShapeChangedHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	// ------------------------------------------------------------------------
	// Methods concerning "UNDO/REDO":
	// ------------------------------------------------------------------------

	/**
	 * Undo the last operation (or operation sequence) that was executed in the editing process, thereby restoring the
	 * previous state.
	 * 
	 * @throws GeometryOperationFailedException
	 *             In case the inverse operation could not be executed.
	 */
	public void undo() throws GeometryOperationFailedException {
		delegate.undo();
	}

	/**
	 * Can an undo actually be executed? You can't execute more calls to undo than there are operations in the undo
	 * queue.
	 * 
	 * @return True or false.
	 */
	public boolean canUndo() {
		return delegate.canUndo();
	}

	/**
	 * Redo an operation again, after it was undone with the undo method.
	 * 
	 * @throws GeometryOperationFailedException
	 *             In case the operation failed.
	 */
	public void redo() throws GeometryOperationFailedException {
		delegate.redo();
	}

	/**
	 * Can a redo operation be executed? This can only be done after some undo.
	 * 
	 * @return True or false.
	 */
	public boolean canRedo() {
		return delegate.canRedo();
	}

	// ------------------------------------------------------------------------
	// Operation sequence manipulation:
	// ------------------------------------------------------------------------

	/**
	 * Starts an operation sequence. All operations called after this method will be regarded as a single unit, which
	 * can be very useful for undo/redo operations. This state is active until <code>stopOperationSequence</code> is
	 * called.
	 * 
	 * @throws GeometryOperationFailedException
	 *             Thrown in case an operation sequence has already been started. Call
	 *             <code>stopOperationSequence</code> first.
	 */
	public void startOperationSequence() throws GeometryOperationFailedException {
		delegate.startOperationSequence();
	}

	/**
	 * Stops the current operation sequence (if there is one active). From this point on, all operations
	 * (move/insert/delete/...) will again be regarded as separate operations.
	 */
	public void stopOperationSequence() {
		delegate.stopOperationSequence();
	}

	/**
	 * Is there currently an operation sequence being build or not?
	 * 
	 * @return Checks if an operation sequence has been started.
	 */
	public boolean isOperationSequenceActive() {
		return delegate.isOperationSequenceActive();
	}

	// ------------------------------------------------------------------------
	// Supported operations:
	// ------------------------------------------------------------------------

	/**
	 * Move a set of indices to new locations. These indices can point to vertices, edges or sub-geometries. For each
	 * index, a list of new coordinates is provided.
	 * 
	 * @param indices
	 *            The list of indices to move.
	 * @param coordinates
	 *            The coordinates to move the indices to. Must be a nested array of coordinates. In other words, for
	 *            each index an array of coordinates must be supplied.
	 * @throws GeometryOperationFailedException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	public void move(GeometryIndex[] indices, JsArray<JsArrayObject> coordinates) {
		List<List<Coordinate>> coords = new ArrayList<List<Coordinate>>(coordinates.length());
		for (int i = 0; i < coordinates.length(); i++) {
			JsArrayObject jsObj = coordinates.get(i);
			coords.add(Arrays.asList(ExporterUtil.toArrObject(jsObj, new Coordinate[jsObj.length()])));
		}
		try {
			delegate.move(Arrays.asList(indices), coords);
		} catch (GeometryOperationFailedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Insert lists of coordinates at the provided indices. These indices can point to vertices, edges or
	 * sub-geometries. For each index, a list of coordinates is provided to be inserted after that index.
	 * 
	 * @param indices
	 *            The list of indices after which to insert coordinates.
	 * @param coordinates
	 *            The coordinates to be inserted after each index. Must be a nested array of coordinates. In other
	 *            words, for each index an array of coordinates must be supplied.
	 * @throws GeometryOperationFailedException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	public void insert(GeometryIndex[] indices, JsArray<JsArrayObject> coordinates) {
		List<List<Coordinate>> coords = new ArrayList<List<Coordinate>>(coordinates.length());
		for (int i = 0; i < coordinates.length(); i++) {
			JsArrayObject jsObj = coordinates.get(i);
			coords.add(Arrays.asList(ExporterUtil.toArrObject(jsObj, new Coordinate[jsObj.length()])));
		}
		try {
			delegate.insert(Arrays.asList(indices), coords);
		} catch (GeometryOperationFailedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Delete vertices, edges or sub-geometries at the given indices.
	 * 
	 * @param indices
	 *            The list of indices that point to the vertices/edges/sub-geometries that should be deleted.
	 * @throws GeometryOperationFailedException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	public void remove(GeometryIndex[] indices) {
		try {
			delegate.remove(Arrays.asList(indices));
		} catch (GeometryOperationFailedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Add an empty child at the lowest sub-geometry level.
	 * 
	 * @return The index that points to the empty child within the geometry.
	 * @throws GeometryOperationFailedException
	 */
	public GeometryIndex addEmptyChild() throws GeometryOperationFailedException {
		return delegate.addEmptyChild();
	}

	// ------------------------------------------------------------------------
	// Methods concerning Workflow:
	// ------------------------------------------------------------------------

	/**
	 * Start the geometry editing process.
	 * 
	 * @param geometry
	 *            The geometry that needs to be edited.
	 */
	public void start(Geometry geometry) {
		delegate.start(geometry);
	}

	/**
	 * Stop the geometry editing process.
	 * 
	 * @return Returns the current state of the geometry.
	 */
	public Geometry stop() {
		return delegate.stop();
	}

	// ------------------------------------------------------------------------
	// Methods concerning editing state:
	// ------------------------------------------------------------------------

	/**
	 * Change the general editing state of this service (idle, dragging, inserting, ...).
	 * 
	 * @param state
	 *            The new editing state.
	 */
	public void setEditingState(String state) {
		if ("idle".equalsIgnoreCase(state)) {
			delegate.setEditingState(GeometryEditingState.IDLE);
		} else if ("dragging".equalsIgnoreCase(state)) {
			delegate.setEditingState(GeometryEditingState.DRAGGING);
		} else if ("inserting".equalsIgnoreCase(state)) {
			delegate.setEditingState(GeometryEditingState.INSERTING);
		}
	}

	/**
	 * Get the current editing state of this service (idle, dragging, inserting, ...).
	 * 
	 * @return The current editing state.
	 */
	public String getEditingState() {
		switch (delegate.getEditingState()) {
			case IDLE:
				return "idle";
			case DRAGGING:
				return "dragging";
			case INSERTING:
				return "inserting";
			default:
				return "unknown";
		}
	}

	// ------------------------------------------------------------------------
	// Methods regarding the tentative move events:
	// ------------------------------------------------------------------------

	public void setTentativeMoveOrigin(Coordinate origin) {
		delegate.setTentativeMoveOrigin(origin);
	}

	public Coordinate getTentativeMoveOrigin() {
		return delegate.getTentativeMoveOrigin();
	}

	public void setTentativeMoveLocation(Coordinate location) {
		delegate.setTentativeMoveLocation(location);
	}

	public Coordinate getTentativeMoveLocation() {
		return delegate.getTentativeMoveLocation();
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	public GeometryIndex getInsertIndex() {
		return delegate.getInsertIndex();
	}

	public void setInsertIndex(GeometryIndex insertIndex) {
		delegate.setInsertIndex(insertIndex);
	}

	public Geometry getGeometry() {
		return delegate.getGeometry();
	}

	public GeometryIndexService getIndexService() {
		return delegate.getIndexService();
	}

	public JsGeometryIndexStateService getIndexStateService() {
		return stateService;
	}
}