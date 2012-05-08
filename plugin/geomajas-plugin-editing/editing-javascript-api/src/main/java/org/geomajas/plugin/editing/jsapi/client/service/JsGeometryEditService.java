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

package org.geomajas.plugin.editing.jsapi.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveEvent;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditServiceImpl;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
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
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * Central service for all operations concerning the geometry editing process. This process should work together with a
 * set of controllers on the map that execute methods from this service after which events are fired for a renderer to
 * act upon. Note that this service uses the {@link GeometryIndexService} to identify sub-geometries, vertices and
 * edges, and that all operations work on a set of such indices. This allows for great flexibility in the operations
 * that can be performed on geometries.
 * </p>
 * <p>
 * This service also extends the {@link org.geomajas.plugin.editing.client.service.GeometryIndexOperationService}
 * service, which defines possible operations on geometries during the editing process. Operations can be stand alone or
 * can be part of an operation sequence. Using an operations sequence wherein multiple operations are executed will be
 * regarded as a single operation unit for the undo and redo methods.
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
 * <p>
 * Know also that operations onto the geometry really do apply on the same geometry that was passed with the
 * <code>start</code> method. In other words, this service does change the original geometry. If you want to support
 * some roll-back functionality within your code, make sure to create a clone of the geometry before starting this edit
 * service.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryEditService")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryEditService implements Exportable {

	private GeometryEditService delegate;

	private JsGeometryIndexStateService stateService;

	/**
	 * Default constructor.
	 */
	public JsGeometryEditService() {
		delegate = new GeometryEditServiceImpl();
		stateService = new JsGeometryIndexStateService(delegate.getIndexStateService());
	}

	/**
	 * Constructor with a {@link GeometryEditService} delegate.
	 * @param delegate delegate
	 */
	@NoExport
	public JsGeometryEditService(GeometryEditService delegate) {
		this.delegate = delegate;
		stateService = new JsGeometryIndexStateService(delegate.getIndexStateService());
	}

	/**
	 * Get the delegating {@link GeometryEditService}.
	 * @return delegate delegate
	 */
	@NoExport
	public GeometryEditService getDelegate() {
		return delegate;
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
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditInsertEvent(event.getGeometry(),
						indices.toArray(new GeometryIndex[indices.size()]));
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
				List<GeometryIndex> indices = event.getIndices();
				handler.onGeometryEditMove(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditMoveEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()])));
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
				List<GeometryIndex> indices = event.getIndices();
				handler.onGeometryEditRemove(new org.geomajas.plugin.editing.jsapi.client.event.GeometryEditRemoveEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()])));
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
			delegate.setEditingState(GeometryEditState.IDLE);
		} else if ("dragging".equalsIgnoreCase(state)) {
			delegate.setEditingState(GeometryEditState.DRAGGING);
		} else if ("inserting".equalsIgnoreCase(state)) {
			delegate.setEditingState(GeometryEditState.INSERTING);
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

	/**
	 * Set the origin for a line to a possible next vertex position. This is regarded as tentative, because no
	 * commitment is taken yet.
	 * 
	 * @param origin
	 *            The origin for the tentative move event.
	 */
	public void setTentativeMoveOrigin(Coordinate origin) {
		delegate.setTentativeMoveOrigin(origin);
	}

	/**
	 * Set the end-point for a possible next vertex position. This is regarded as tentative, because no commitment is
	 * taken yet. An event is thrown though, containing both the tentative origin and end-point.
	 * 
	 * @param location
	 *            The end-point for the tentative move event.
	 */
	public void setTentativeMoveLocation(Coordinate location) {
		delegate.setTentativeMoveLocation(location);
	}

	/**
	 * Get the origin for the tentative move event.
	 * 
	 * @return The origin for the tentative move event.
	 */
	public Coordinate getTentativeMoveOrigin() {
		return delegate.getTentativeMoveOrigin();
	}

	/**
	 * Get the end-point for the tentative move event.
	 * 
	 * @return The end-point for the tentative move event.
	 */
	public Coordinate getTentativeMoveLocation() {
		return delegate.getTentativeMoveLocation();
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	/**
	 * Get the index where the next insert operation should take place.
	 * 
	 * @return The index where the next insert operation should take place.
	 */
	public GeometryIndex getInsertIndex() {
		return delegate.getInsertIndex();
	}

	/**
	 * Set the index where the insert operation should take place. This is mainly used as a helper method. The insert
	 * operation can actually insert wherever it wants, but this is often used to keep track of the latest inserts and
	 * extrapolate where to insert next.
	 * 
	 * @param insertIndex
	 *            The vertex/edge/sub-geometry where to insert on the next insert statement.
	 */
	public void setInsertIndex(GeometryIndex insertIndex) {
		delegate.setInsertIndex(insertIndex);
	}

	/**
	 * Get the current geometry. This geometry may change shape during the editing process.
	 * 
	 * @return The current geometry that is being edited.
	 */
	public Geometry getGeometry() {
		return delegate.getGeometry();
	}

	/**
	 * Return the indexing service the is being used to identify vertices/edges/sub-geometries.
	 * 
	 * @return The geometry indexing service.
	 */
	public GeometryIndexService getIndexService() {
		return delegate.getIndexService();
	}

	/**
	 * Return the service that keeps track of the changes in state of the individual vertices/edges/sub-geometries
	 * during editing.
	 * 
	 * @return The geometry-index-state-change service.
	 */
	public JsGeometryIndexStateService getIndexStateService() {
		return stateService;
	}
}