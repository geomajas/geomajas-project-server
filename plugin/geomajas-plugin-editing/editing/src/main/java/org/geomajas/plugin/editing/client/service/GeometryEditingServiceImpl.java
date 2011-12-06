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
 * Implementation of the {@link GeometryEditingService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryEditingServiceImpl implements GeometryEditingService {

	protected EventBus eventBus;

	protected Geometry geometry;

	protected GeometryIndexService indexService;

	protected GeometryIndexStateService indexStateService;

	protected GeometryIndexOperationService operationService;

	private GeometryEditingState state = GeometryEditingState.IDLE;

	private GeometryIndex insertIndex;

	private Coordinate tentativeMoveOrigin;

	private Coordinate tentativeMoveLocation;

	private boolean started;

	// ------------------------------------------------------------------------
	// Public constructors:
	// ------------------------------------------------------------------------

	public GeometryEditingServiceImpl() {
		eventBus = new SimpleEventBus();
		indexService = new GeometryIndexService();
		indexStateService = new GeometryIndexStateServiceImpl(this);
		operationService = new GeometryIndexOperationServiceImpl(this, eventBus);
	}

	// ------------------------------------------------------------------------
	// Handler registration:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditStartHandler(GeometryEditStartHandler handler) {
		return eventBus.addHandler(GeometryEditStartHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditStopHandler(GeometryEditStopHandler handler) {
		return eventBus.addHandler(GeometryEditStopHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditShapeChangedHandler(GeometryEditShapeChangedHandler handler) {
		return eventBus.addHandler(GeometryEditShapeChangedHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditInsertHandler(GeometryEditInsertHandler handler) {
		return eventBus.addHandler(GeometryEditInsertHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditMoveHandler(GeometryEditMoveHandler handler) {
		return eventBus.addHandler(GeometryEditMoveHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditRemoveHandler(GeometryEditRemoveHandler handler) {
		return eventBus.addHandler(GeometryEditRemoveHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditChangeStateHandler(GeometryEditChangeStateHandler handler) {
		return eventBus.addHandler(GeometryEditChangeStateHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryEditTentativeMoveHandler(GeometryEditTentativeMoveHandler handler) {
		return eventBus.addHandler(GeometryEditTentativeMoveHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Methods concerning Workflow:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void start(Geometry geometry) {
		if (started) {
			return;
		}
		this.geometry = geometry;
		if (geometry == null) {
			throw new NullPointerException("Null geometry not allowed.");
		}
		started = true;
		eventBus.fireEvent(new GeometryEditStartEvent(geometry));
	}

	/** {@inheritDoc} */
	public Geometry stop() {
		started = false;
		setEditingState(GeometryEditingState.IDLE);

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

	/** {@inheritDoc} */
	public GeometryEditingState getEditingState() {
		return state;
	}

	/** {@inheritDoc} */
	public void setEditingState(GeometryEditingState state) {
		this.state = state;
		eventBus.fireEvent(new GeometryEditChangeStateEvent(state));
	}

	// ------------------------------------------------------------------------
	// Methods regarding the insert move events:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void setTentativeMoveOrigin(Coordinate tentativeMoveOrigin) {
		this.tentativeMoveOrigin = tentativeMoveOrigin;
	}

	/** {@inheritDoc} */
	public void setTentativeMoveLocation(Coordinate tentativeMoveLocation) {
		this.tentativeMoveLocation = tentativeMoveLocation;
		eventBus.fireEvent(new GeometryEditTentativeMoveEvent(tentativeMoveOrigin, tentativeMoveLocation));
	}

	/** {@inheritDoc} */
	public Coordinate getTentativeMoveLocation() {
		return tentativeMoveLocation;
	}

	/** {@inheritDoc} */
	public Coordinate getTentativeMoveOrigin() {
		return tentativeMoveOrigin;
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Geometry getGeometry() {
		return geometry;
	}

	/** {@inheritDoc} */
	public GeometryIndexService getIndexService() {
		return indexService;
	}

	/** {@inheritDoc} */
	public GeometryIndexStateService getIndexStateService() {
		return indexStateService;
	}

	/** {@inheritDoc} */
	public GeometryIndex getInsertIndex() {
		return insertIndex;
	}

	/** {@inheritDoc} */
	public void setInsertIndex(GeometryIndex insertIndex) {
		this.insertIndex = insertIndex;
	}

	/** {@inheritDoc} */
	protected EventBus getEventBus() {
		return eventBus;
	}

	// ------------------------------------------------------------------------
	// GeometryIndexOperation Service implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void undo() throws GeometryOperationFailedException {
		operationService.undo();
	}

	/** {@inheritDoc} */
	public boolean canUndo() {
		return operationService.canUndo();
	}

	/** {@inheritDoc} */
	public void redo() throws GeometryOperationFailedException {
		operationService.redo();
	}

	/** {@inheritDoc} */
	public boolean canRedo() {
		return operationService.canRedo();
	}

	/** {@inheritDoc} */
	public void startOperationSequence() throws GeometryOperationFailedException {
		operationService.startOperationSequence();
	}

	/** {@inheritDoc} */
	public void stopOperationSequence() {
		operationService.stopOperationSequence();
	}

	/** {@inheritDoc} */
	public boolean isOperationSequenceActive() {
		return operationService.isOperationSequenceActive();
	}

	/** {@inheritDoc} */
	public void move(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException {
		operationService.move(indices, coordinates);
	}

	/** {@inheritDoc} */
	public void insert(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException {
		operationService.insert(indices, coordinates);
	}

	/** {@inheritDoc} */
	public void remove(List<GeometryIndex> indices) throws GeometryOperationFailedException {
		operationService.remove(indices);
	}

	/** {@inheritDoc} */
	public GeometryIndex addEmptyChild() throws GeometryOperationFailedException {
		return operationService.addEmptyChild();
	}
}