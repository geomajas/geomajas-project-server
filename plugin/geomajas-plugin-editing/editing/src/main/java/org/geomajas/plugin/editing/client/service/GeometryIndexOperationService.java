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
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;

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
public interface GeometryIndexOperationService {

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
	void undo() throws GeometryOperationFailedException;

	/**
	 * Can an undo actually be executed? You can't execute more calls to undo than there are operations in the undo
	 * queue.
	 * 
	 * @return True or false.
	 */
	boolean canUndo();

	/**
	 * Redo an operation again, after it was undone with the undo method.
	 * 
	 * @throws GeometryOperationFailedException
	 *             In case the operation failed.
	 */
	void redo() throws GeometryOperationFailedException;

	/**
	 * Can a redo operation be executed? This can only be done after some undo.
	 * 
	 * @return True or false.
	 */
	boolean canRedo();

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
	void startOperationSequence() throws GeometryOperationFailedException;

	/**
	 * Stops the current operation sequence (if there is one active). From this point on, all operations
	 * (move/insert/delete/...) will again be regarded as separate operations.
	 */
	void stopOperationSequence();

	/**
	 * Is there currently an operation sequence being build or not?
	 * 
	 * @return Checks if an operation sequence has been started.
	 */
	boolean isOperationSequenceActive();

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
	 *            The coordinates to move the indices to.
	 * @throws GeometryOperationFailedException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	void move(List<GeometryIndex> indices, List<List<Coordinate>> coordinates) throws GeometryOperationFailedException;

	/**
	 * Insert lists of coordinates at the provided indices. These indices can point to vertices, edges or
	 * sub-geometries. For each index, a list of coordinates is provided to be inserted after that index.
	 * 
	 * @param indices
	 *            The list of indices after which to insert coordinates.
	 * @param coordinates
	 *            The coordinates to be inserted after each index.
	 * @throws GeometryOperationFailedException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	void insert(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryOperationFailedException;

	/**
	 * Delete vertices, edges or sub-geometries at the given indices.
	 * 
	 * @param indices
	 *            The list of indices that point to the vertices/edges/sub-geometries that should be deleted.
	 * @throws GeometryOperationFailedException
	 *             In case one of the indices could not be found. No changes will have been performed.
	 */
	void remove(List<GeometryIndex> indices) throws GeometryOperationFailedException;

	/**
	 * Add an empty child at the lowest sub-geometry level.
	 * 
	 * @return The index that points to the empty child within the geometry.
	 * @throws GeometryOperationFailedException
	 */
	//TODO Remove this method in favor of the addEmptyChild(GeometryIndex index)?
	GeometryIndex addEmptyChild() throws GeometryOperationFailedException;
	/**
	 * Add an empty child at the lowest sub-geometry level.
	 * 
	 * @param index TODO
	 * @return The index that points to the empty child within the geometry.
	 * @throws GeometryOperationFailedException
	 */
	GeometryIndex addEmptyChild(GeometryIndex index) throws GeometryOperationFailedException;
}