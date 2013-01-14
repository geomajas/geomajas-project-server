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

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSnappingBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSnappingEndHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * Service that keeps track of the state of all the individual parts of the geometry being edited. The supported states
 * for any part of a geometry are the following:
 * <ul>
 * <li><b>Selection</b>: vertices and edges can be selected. This selection process can assist the user in his
 * operations. Specific controllers could then allow for the operations to be performed on this selection only. For
 * example dragging of selected vertices.</li>
 * <li><b>Highlighting</b>: vertices and edges can be highlighted usually when the mouse hovers over them.</li>
 * <li><b>Marked for deletion</b>: vertices and edges can be marked for deletion. Specific controllers could then be
 * implement actions for the user for actually delete the indices that are marked.</li>
 * <li><b>Enabled/Disabled</b>: All parts of the geometry can be individually enabled/disabled for further editing.</li>
 * <li><b>Snapping</b>: During dragging or inserting it can be possible that snapping is being used. This state keeps
 * track of whether or not any vertices have snapped to some external geometry.</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface GeometryIndexStateService {

	// ------------------------------------------------------------------------
	// Methods concerning Vertex/Edge selection:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryIndexSelectedHandler} to listen to selection events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexSelectedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexSelectedHandler(GeometryIndexSelectedHandler handler);

	/**
	 * Register a {@link GeometryIndexDeselectedHandler} to listen to deselection events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexDeselectedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexDeselectedHandler(GeometryIndexDeselectedHandler handler);

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
	// Methods concerning disabling of indices:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryIndexDisabledHandler} to listen disable events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexDisabledHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexDisabledHandler(GeometryIndexDisabledHandler handler);

	/**
	 * Register a {@link GeometryIndexEnabledHandler} to listen enable events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexEnabledHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexEnabledHandler(GeometryIndexEnabledHandler handler);

	/**
	 * Enable the given list of vertices/edges/sub-geometries for further editing.
	 * 
	 * @param indices
	 *            The list of indices to enable.
	 */
	void enable(List<GeometryIndex> indices);

	/**
	 * Disable the given list of vertices/edges/sub-geometries for further editing.
	 * 
	 * @param indices
	 *            The list of indices to disable.
	 */
	void disable(List<GeometryIndex> indices);

	/** Enable everything for further editing. Nothing remains disabled. */
	void enableAll();

	/**
	 * Is the given vertex/edge/sub-geometry currently enabled or disabled for further editing?
	 * 
	 * @param index
	 *            The vertex/edge/sub-geometry to check.
	 * @return True or false.
	 */
	boolean isEnabled(GeometryIndex index);

	// ------------------------------------------------------------------------
	// Methods concerning highlighting:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryIndexHighlightBeginHandler} to listen to highlighting begin events of sub-geometries,
	 * vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexHighlightBeginHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexHighlightBeginHandler(GeometryIndexHighlightBeginHandler handler);

	/**
	 * Register a {@link GeometryIndexHighlightEndHandler} to listen to highlighting end events of sub-geometries,
	 * vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexHighlightEndHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexHighlightEndHandler(GeometryIndexHighlightEndHandler handler);

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

	/** End all highlighting for all vertices/edges/sub-geometries. */
	void highlightEndAll();

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
	 * Register a {@link GeometryIndexMarkForDeletionBeginHandler} to listen to mark for deletion begin events of
	 * sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexMarkForDeletionBeginHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexMarkForDeletionBeginHandler(GeometryIndexMarkForDeletionBeginHandler handler);

	/**
	 * Register a {@link GeometryIndexMarkForDeletionEndHandler} to listen to mark for deletion end events of
	 * sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexMarkForDeletionEndHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexMarkForDeletionEndHandler(GeometryIndexMarkForDeletionEndHandler handler);

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

	/** Unmark all vertices/edges/sub-geometries for deletion. */
	void markForDeletionEndAll();

	/**
	 * Is a certain vertex/edge/sub-geometry marked for deletion or not?
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	boolean isMarkedForDeletion(GeometryIndex index);

	// ------------------------------------------------------------------------
	// Methods concerning snapping:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryIndexSnappingBeginHandler} to listen to snapping begin events of sub-geometries,
	 * vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexSnappingBeginHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexSnappingBeginHandler(GeometryIndexSnappingBeginHandler handler);

	/**
	 * Register a {@link GeometryIndexSnappingEndHandler} to listen to snapping end events of sub-geometries, vertices
	 * and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexSnappingEndHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	HandlerRegistration addGeometryIndexSnappingEndHandler(GeometryIndexSnappingEndHandler handler);

	/**
	 * Add the given list of indices to the list of snapped indices.
	 * 
	 * @param indices
	 *            The indices that have snapped to some external geometry.
	 */
	void snappingBegin(List<GeometryIndex> indices);

	/**
	 * Unmark the given indices as being snapped. They return to their normal state and location.
	 * 
	 * @param indices
	 *            The indices that have stopped snapping.
	 */
	void snappingEnd(List<GeometryIndex> indices);

	/** Empty the list of snapped indices. */
	void snappingEndAll();

	/**
	 * Has a certain index snapped to some external geometry or not?
	 * 
	 * @param index
	 *            The index to check.
	 * @return True or false.
	 */
	boolean isSnapped(GeometryIndex index);
}