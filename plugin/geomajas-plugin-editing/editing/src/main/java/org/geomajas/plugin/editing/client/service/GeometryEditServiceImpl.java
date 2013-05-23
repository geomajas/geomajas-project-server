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

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * <p>
 * Central service for all operations concerning the geometry editing process. This process should work together with a
 * set of controllers on the map that execute methods from this service after which events are fired for a renderer to
 * act upon. Note that this service uses the {@link GeometryIndexService} to identify sub-geometries, vertices and
 * edges, and that all operations work on a set of such indices. This allows for great flexibility in the operations
 * that can be performed on geometries.
 * </p>
 * <p>
 * This service also extends the {@link GeometryIndexOperationService} service, which defines possible operations on
 * geometries during the editing process. Operations can be stand alone or can be part of an operation sequence. Using
 * an operations sequence wherein multiple operations are executed will be regarded as a single operation unit for the
 * undo and redo methods.
 * </p>
 * <p>
 * Know also that operations onto the geometry really do apply on the same geometry that was passed with the
 * <code>start</code> method. In other words, this service does change the original geometry. If you want to support
 * some roll-back functionality within your code, make sure to create a clone of the geometry before starting this edit
 * service.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryEditServiceImpl implements GeometryEditService {

	private final EventBus eventBus;

	private final GeometryIndexService indexService;

	private final GeometryIndexStateService indexStateService;

	private GeometryIndexOperationService operationService;

	private Geometry geometry;

	private GeometryEditState state = GeometryEditState.IDLE;

	private GeometryIndex insertIndex;

	private Coordinate tentativeMoveOrigin;

	private Coordinate tentativeMoveLocation;

	private boolean started;

	// ------------------------------------------------------------------------
	// Public constructors:
	// ------------------------------------------------------------------------

	/** Create a brand new and shining service for geometry editing. */
	public GeometryEditServiceImpl() {
		eventBus = new SimpleEventBus();
		indexService = new GeometryIndexService();
		indexStateService = new GeometryIndexStateServiceImpl(this);
		operationService = new GeometryIndexOperationServiceImpl(this, eventBus);
	}

	// ------------------------------------------------------------------------
	// Handler registration:
	// ------------------------------------------------------------------------

	@Override
	public HandlerRegistration addGeometryEditStartHandler(GeometryEditStartHandler handler) {
		return eventBus.addHandler(GeometryEditStartHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditStopHandler(GeometryEditStopHandler handler) {
		return eventBus.addHandler(GeometryEditStopHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditShapeChangedHandler(GeometryEditShapeChangedHandler handler) {
		return eventBus.addHandler(GeometryEditShapeChangedHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditInsertHandler(GeometryEditInsertHandler handler) {
		return eventBus.addHandler(GeometryEditInsertHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditMoveHandler(GeometryEditMoveHandler handler) {
		return eventBus.addHandler(GeometryEditMoveHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditRemoveHandler(GeometryEditRemoveHandler handler) {
		return eventBus.addHandler(GeometryEditRemoveHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditChangeStateHandler(GeometryEditChangeStateHandler handler) {
		return eventBus.addHandler(GeometryEditChangeStateHandler.TYPE, handler);
	}

	@Override
	public HandlerRegistration addGeometryEditTentativeMoveHandler(GeometryEditTentativeMoveHandler handler) {
		return eventBus.addHandler(GeometryEditTentativeMoveHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Methods concerning Workflow:
	// ------------------------------------------------------------------------

	@Override
	public void start(Geometry geometry) {
		if (started) {
			return;
		}
		this.geometry = geometry;
		if (geometry == null) {
			throw new IllegalArgumentException("Null geometry not allowed.");
		}
		started = true;
		eventBus.fireEvent(new GeometryEditStartEvent(geometry));
	}
	
	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public Geometry stop() {
		started = false;
		setEditingState(GeometryEditState.IDLE);

		indexStateService.deselectAll();
		indexStateService.enableAll();
		indexStateService.highlightEndAll();
		indexStateService.markForDeletionEndAll();

		eventBus.fireEvent(new GeometryEditStopEvent(geometry));
		return geometry;
	}

	// ------------------------------------------------------------------------
	// Methods concerning editing state:
	// ------------------------------------------------------------------------

	@Override
	public GeometryEditState getEditingState() {
		return state;
	}

	@Override
	public void setEditingState(GeometryEditState state) {
		this.state = state;
		eventBus.fireEvent(new GeometryEditChangeStateEvent(state));
	}

	// ------------------------------------------------------------------------
	// Methods regarding the insert move events:
	// ------------------------------------------------------------------------

	@Override
	public void setTentativeMoveOrigin(Coordinate tentativeMoveOrigin) {
		this.tentativeMoveOrigin = tentativeMoveOrigin;
	}

	@Override
	public void setTentativeMoveLocation(Coordinate tentativeMoveLocation) {
		this.tentativeMoveLocation = tentativeMoveLocation;
		eventBus.fireEvent(new GeometryEditTentativeMoveEvent(tentativeMoveOrigin, tentativeMoveLocation));
	}

	@Override
	public Coordinate getTentativeMoveLocation() {
		return tentativeMoveLocation;
	}

	@Override
	public Coordinate getTentativeMoveOrigin() {
		return tentativeMoveOrigin;
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	@Override
	public Geometry getGeometry() {
		return geometry;
	}

	@Override
	public GeometryIndexService getIndexService() {
		return indexService;
	}

	@Override
	public GeometryIndexStateService getIndexStateService() {
		return indexStateService;
	}

	@Override
	public GeometryIndex getInsertIndex() {
		return insertIndex;
	}

	@Override
	public void setInsertIndex(GeometryIndex insertIndex) {
		this.insertIndex = insertIndex;
	}

	/**
	 * Get access to the event bus.
	 *
	 * @return event bus
	 */
	protected EventBus getEventBus() {
		return eventBus;
	}

	// ------------------------------------------------------------------------
	// GeometryIndexOperation Service implementation:
	// ------------------------------------------------------------------------

	@Override
	public void undo() throws GeometryOperationFailedException {
		operationService.undo();
	}

	@Override
	public boolean canUndo() {
		return operationService.canUndo();
	}

	@Override
	public void redo() throws GeometryOperationFailedException {
		operationService.redo();
	}

	@Override
	public boolean canRedo() {
		return operationService.canRedo();
	}

	@Override
	public void startOperationSequence() throws GeometryOperationFailedException {
		operationService.startOperationSequence();
	}

	@Override
	public void stopOperationSequence() {
		operationService.stopOperationSequence();
	}

	@Override
	public boolean isOperationSequenceActive() {
		return operationService.isOperationSequenceActive();
	}

	@Override
	public void move(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException {
		operationService.move(indices, coordinates);
	}

	@Override
	public void insert(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException {
		operationService.insert(indices, coordinates);
	}

	@Override
	public void remove(List<GeometryIndex> indices) throws GeometryOperationFailedException {
		operationService.remove(indices);
	}

	@Override
	public GeometryIndex addEmptyChild() throws GeometryOperationFailedException {
		return operationService.addEmptyChild();
	}

	@Override
	public GeometryIndex addEmptyChild(GeometryIndex index) throws GeometryOperationFailedException {
		return operationService.addEmptyChild(index);
	}
}