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

package org.geomajas.plugin.editing.client.service;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditRemoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditTentativeMoveHandler;

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
public interface GeometryEditService extends GeometryIndexOperationService {

	// ------------------------------------------------------------------------
	// Registering handlers for specific editing events:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryEditStartHandler} to listen to events that signal the editing process has started.
	 * 
	 * @param handler
	 *            The {@link GeometryEditStartHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditStartHandler(GeometryEditStartHandler handler);

	/**
	 * Register a {@link GeometryEditStopHandler} to listen to events that signal the editing process has ended.
	 * 
	 * @param handler
	 *            The {@link GeometryEditStopHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditStopHandler(GeometryEditStopHandler handler);

	/**
	 * Register a {@link GeometryEditShapeChangedHandler} to listen to operation events (moving, inserting, deleting,
	 * ...) of sub-geometries, vertices and edges but also listens to undo/redo events. Anything the changes the
	 * geometry shape basically.
	 * 
	 * @param handler
	 *            The {@link GeometryEditShapeChangedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditShapeChangedHandler(GeometryEditShapeChangedHandler handler);

	/**
	 * Register a {@link GeometryEditInsertHandler} to listen to insert events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditInsertHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditInsertHandler(GeometryEditInsertHandler handler);

	/**
	 * Register a {@link GeometryEditMoveHandler} to listen to move(translate) events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditMoveHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditMoveHandler(GeometryEditMoveHandler handler);

	/**
	 * Register a {@link GeometryEditRemoveHandler} to listen to delete events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditRemoveHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditRemoveHandler(GeometryEditRemoveHandler handler);

	/**
	 * Register a {@link GeometryEditChangeStateHandler} to listen to events the mark changes in the general editing
	 * state. This general state can say we're busy idling, dragging vertices/edges/geometries or inserting. The reason
	 * why this exists is for the general editing controllers to know how to behave.
	 * 
	 * @param handler
	 *            The {@link GeometryEditChangeStateHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditChangeStateHandler(GeometryEditChangeStateHandler handler);

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
	HandlerRegistration addGeometryEditTentativeMoveHandler(GeometryEditTentativeMoveHandler handler);

	// ------------------------------------------------------------------------
	// Methods concerning Workflow:
	// ------------------------------------------------------------------------

	/**
	 * Start the geometry editing process.
	 * 
	 * @param geometry
	 *            The geometry that needs to be edited.
	 */
	void start(Geometry geometry);
	
	/**
	 * Get the started state of the editing process.
	 * 
	 * @return started state
	 */
	boolean isStarted();

	/**
	 * Stop the geometry editing process.
	 * 
	 * @return Returns the current state of the geometry.
	 */
	Geometry stop();

	// ------------------------------------------------------------------------
	// Methods concerning editing state:
	// ------------------------------------------------------------------------

	/**
	 * Change the general editing state of this service (idle, dragging, inserting, ...).
	 * 
	 * @param state
	 *            The new editing state.
	 */
	void setEditingState(GeometryEditState state);

	/**
	 * Get the current editing state of this service (idle, dragging, inserting, ...).
	 * 
	 * @return The current editing state.
	 */
	GeometryEditState getEditingState();

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
	void setTentativeMoveOrigin(Coordinate origin);

	/**
	 * Set the end-point for a possible next vertex position. This is regarded as tentative, because no commitment is
	 * taken yet. An event is thrown though, containing both the tentative origin and end-point.
	 * 
	 * @param location
	 *            The end-point for the tentative move event.
	 */
	void setTentativeMoveLocation(Coordinate location);

	/**
	 * Get the origin for the tentative move event.
	 * 
	 * @return The origin for the tentative move event.
	 */
	Coordinate getTentativeMoveOrigin();

	/**
	 * Get the end-point for the tentative move event.
	 * 
	 * @return The end-point for the tentative move event.
	 */
	Coordinate getTentativeMoveLocation();
	
	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	/**
	 * Set the index where the insert operation should take place. This is mainly used as a helper method. The insert
	 * operation can actually insert wherever it wants, but this is often used to keep track of the latest inserts and
	 * extrapolate where to insert next.
	 * 
	 * @param insertIndex
	 *            The vertex/edge/sub-geometry where to insert on the next insert statement.
	 */
	void setInsertIndex(GeometryIndex insertIndex);

	/**
	 * Get the index where the next insert operation should take place.
	 * 
	 * @return The index where the next insert operation should take place.
	 */
	GeometryIndex getInsertIndex();

	/**
	 * Get the current geometry. This geometry may change shape during the editing process.
	 * 
	 * @return The current geometry that is being edited.
	 */
	Geometry getGeometry();

	/**
	 * Return the indexing service the is being used to identify vertices/edges/sub-geometries.
	 * 
	 * @return The geometry indexing service.
	 */
	GeometryIndexService getIndexService();

	/**
	 * Return the service that keeps track of the changes in state of the individual vertices/edges/sub-geometries
	 * during editing.
	 * 
	 * @return The geometry-index-state-change service.
	 */
	GeometryIndexStateService getIndexStateService();
	
	/**
	 * Set boolean that determines if a user can stop editing by clicking outside the geometry that is being edited.
	 * 
	 * @param isClickToStop true to stop, false otherwise.
	 */
	void setClickToStop(boolean isClickToStop);
	
	/**
	 * Get boolean that determines if a user can stop editing by clicking outside the geometry that is being edited.
	 * 
	 * @return isClickToStop true to stop, false otherwise.
	 */
	boolean isClickToStop();
}