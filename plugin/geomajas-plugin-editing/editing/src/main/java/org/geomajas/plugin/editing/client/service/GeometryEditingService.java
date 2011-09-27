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
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertMoveHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditOperationHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditSelectionHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditWorkflowHandler;

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
 * Operations in this service do not only include changes to the actual geometry (insert, move, delete, ...) but also
 * allows for managing states of the sub-geometries, vertices or edges. The supported states for any part of a geometry
 * are the following:
 * <ul>
 * <li><b>Selection</b>: vertices and edges can be selected. This selection process can assist the user in his
 * operations. Specific controllers could then allow for the operations to be performed on this selection only. For
 * example dragging of selected vertices.</li>
 * <li><b>Highlighting</b>: vertices and edges can be highlighted usually when the mouse hovers over them.</li>
 * <li><b>Marked for deletion</b>: vertices and edges can be marked for deletion. Specific controllers could then be
 * implement actions for the user for actually delete the indices that are marked.</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface GeometryEditingService {

	// ------------------------------------------------------------------------
	// Registering handlers for specific editing events:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryEditSelectionHandler} to listen to selection events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditSelectionHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditSelectionHandler(GeometryEditSelectionHandler handler);

	/**
	 * Register a {@link GeometryEditWorkflowHandler} to listen to workflow events in the geometry editing process
	 * (start, stop, ...).
	 * 
	 * @param handler
	 *            The {@link GeometryEditWorkflowHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditWorkflowHandler(GeometryEditWorkflowHandler handler);

	/**
	 * Register a {@link GeometryEditOperationHandler} to listen to operation events (moving, inserting, deleting, ...)
	 * of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditOperationHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditOperationHandler(GeometryEditOperationHandler handler);

	/**
	 * Register a {@link GeometryEditHighlightHandler} to listen to highlighting events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditHighlightHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditHighlightHandler(GeometryEditHighlightHandler handler);

	/**
	 * Register a {@link GeometryEditMarkForDeletionHandler} to listen to mark for deletion events of sub-geometries,
	 * vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryEditMarkForDeletionHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditMarkForDeletionHandler(GeometryEditMarkForDeletionHandler handler);

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
	 * Register a {@link GeometryEditInsertMoveHandler} to listen to mouse move events while inserting. These move
	 * events portray the state of the geometry should a point be inserted at the given location. This event exists
	 * mainly for renderers to display this temporary state.
	 * 
	 * @param handler
	 *            The {@link GeometryEditInsertMoveHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryEditInsertMoveHandler(GeometryEditInsertMoveHandler handler);

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
	 * Stop the geometry editing process.
	 * 
	 * @return Returns the current state of the geometry.
	 */
	Geometry stop();

	// ------------------------------------------------------------------------
	// Edit operations:
	// ------------------------------------------------------------------------

	/**
	 * Move a set of indices to new locations. These indices can point to vertices, edges or sub-geometries. For each
	 * index, a list of new coordinates is provided.
	 * 
	 * @param indices
	 *            The list of indices to move.
	 * @param coordinates
	 *            The coordinates to move the indices to.
	 * @throws GeometryIndexNotFoundException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	void move(List<GeometryIndex> indices, List<List<Coordinate>> coordinates) throws GeometryIndexNotFoundException;

	/**
	 * Insert lists of coordinates at the provided indices. These indices can point to vertices, edges or
	 * sub-geometries. For each index, a list of coordinates is provided to be inserted after that index.
	 * 
	 * @param indices
	 *            The list of indices after which to insert coordinates.
	 * @param coordinates
	 *            The coordinates to be inserted after each index.
	 * @throws GeometryIndexNotFoundException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	void insert(List<GeometryIndex> indices, List<List<Coordinate>> coordinates) throws GeometryIndexNotFoundException;

	/**
	 * Delete vertices, edges or sub-geometries at the given indices.
	 * 
	 * @param indices
	 *            The list of indices that point to the vertices/edges/sub-geometries that should be deleted.
	 * @throws GeometryIndexNotFoundException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	void delete(List<GeometryIndex> indices) throws GeometryIndexNotFoundException;

	// ------------------------------------------------------------------------
	// Methods concerning Vertex/Edge selection:
	// ------------------------------------------------------------------------

	/**
	 * Select a list of vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The list of indices to select.
	 */
	void select(List<GeometryIndex> indices);

	/**
	 * Deselect the given list of vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The list of indices to deselect.
	 */
	void deselect(List<GeometryIndex> indices);

	/** Deselect all vertices/edges/sub-geometries. */
	void deselectAll();

	/**
	 * Check whether or not some index (vertex/edge/sub-geometry) has been selected.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	boolean isSelected(GeometryIndex index);

	/**
	 * Get the current selection. Do not make changes on this list!
	 * 
	 * @return The current selection (vertices/edges/sub-geometries).
	 */
	List<GeometryIndex> getSelection();

	// ------------------------------------------------------------------------
	// Methods concerning editing state:
	// ------------------------------------------------------------------------

	/**
	 * Change the general editing state of this service (idle, dragging, inserting, ...).
	 * 
	 * @param state
	 *            The new editing state.
	 */
	void setEditingState(GeometryEditingState state);

	/**
	 * Get the current editing state of this service (idle, dragging, inserting, ...).
	 * 
	 * @return The current editing state.
	 */
	GeometryEditingState getEditingState();

	// ------------------------------------------------------------------------
	// Methods concerning highlighting:
	// ------------------------------------------------------------------------

	/**
	 * Start highlighting a set vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The set of indices to highlight.
	 */
	void highlightBegin(List<GeometryIndex> indices);

	/**
	 * Stop highlighting a set vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The set of indices to stop highlighting.
	 */
	void highlightEnd(List<GeometryIndex> indices);

	/**
	 * Is a certain index (vertex/edge/sub-geometry) highlighted or not?
	 * 
	 * @param index
	 *            The index to check for highlighting.
	 * @return true or false.
	 */
	boolean isHightlighted(GeometryIndex index);

	// ------------------------------------------------------------------------
	// Methods concerning marking for deletion:
	// ------------------------------------------------------------------------

	/**
	 * Mark a set vertices/edges/sub-geometries for deletion.
	 * 
	 * @param indices
	 *            The set of indices to mark for deletion.
	 */
	void markForDeletionBegin(List<GeometryIndex> indices);

	/**
	 * Unmark a set vertices/edges/sub-geometries for deletion.
	 * 
	 * @param indices
	 *            The set of indices to unmark for deletion.
	 */
	void markForDeletionEnd(List<GeometryIndex> indices);

	/**
	 * Is a certain vertex/edge/sub-geometry marked for deletion or not?
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	boolean isMarkedForDeletion(GeometryIndex index);

	// ------------------------------------------------------------------------
	// Methods regarding the insert move events:
	// ------------------------------------------------------------------------
	
	void setInsertMoveLocation(Coordinate location);
	
	Coordinate getInsertMoveLocation();

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	GeometryIndex getInsertIndex();

	void setInsertIndex(GeometryIndex insertIndex);

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
}