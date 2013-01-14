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

package org.geomajas.plugin.editing.jsapi.client.service;

import java.util.Arrays;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedEvent;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexStateService;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexDeselectedHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexDisabledHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexEnabledHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexHighlightBeginHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexHighlightEndHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexMarkForDeletionBeginHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexMarkForDeletionEndHandler;
import org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexSelectedHandler;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

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
@Export("GeometryIndexStateService")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryIndexStateService implements Exportable {

	private GeometryIndexStateService delegate;

	/** No argument constructor. Do not use. */
	public JsGeometryIndexStateService() {
	}

	/**
	 * Initiate this service with it's Java counterpart.
	 * 
	 * @param delegate
	 *            The Java counterpart.
	 */
	@NoExport
	public JsGeometryIndexStateService(GeometryIndexStateService delegate) {
		this.delegate = delegate;
	}

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
	public JsHandlerRegistration addGeometryIndexSelectedHandler(final GeometryIndexSelectedHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler() {

			public void onGeometryIndexSelected(GeometryIndexSelectedEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexSelectedEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexSelectedEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexSelected(e);
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryIndexSelectedHandler(h) });
	}

	/**
	 * Register a {@link GeometryIndexDeselectedHandler} to listen to deselection events of sub-geometries, vertices and
	 * edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexDeselectedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryIndexDeselectedHandler(final GeometryIndexDeselectedHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedHandler() {

			public void onGeometryIndexDeselected(GeometryIndexDeselectedEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexDeselectedEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexDeselectedEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexDeselected(e);
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryIndexDeselectedHandler(h) });
	}

	/**
	 * Select a list of vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The list of indices to select.
	 */
	public void select(GeometryIndex[] indices) {
		delegate.select(Arrays.asList(indices));
	}

	/**
	 * Deselect the given list of vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The list of indices to deselect.
	 */
	public void deselect(GeometryIndex[] indices) {
		delegate.deselect(Arrays.asList(indices));
	}

	/** Deselect all vertices/edges/sub-geometries. */
	public void deselectAll() {
		delegate.deselectAll();
	}

	/**
	 * Check whether or not some index (vertex/edge/sub-geometry) has been selected.
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isSelected(GeometryIndex index) {
		return delegate.isSelected(index);
	}

	/**
	 * Get the current selection. Do not make changes on this list!
	 * 
	 * @return The current selection (vertices/edges/sub-geometries).
	 */
	GeometryIndex[] getSelection() {
		List<GeometryIndex> indices = delegate.getSelection();
		return indices.toArray(new GeometryIndex[indices.size()]);
	}

	// ------------------------------------------------------------------------
	// Methods concerning disabling of indices:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryIndexEnabledHandler} to listen enable events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexEnabledHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryIndexEnabledHandler(final GeometryIndexEnabledHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledHandler() {

			public void onGeometryIndexEnabled(GeometryIndexEnabledEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexEnabledEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexEnabledEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexEnabled(e);
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryIndexEnabledHandler(h) });
	}

	/**
	 * Register a {@link GeometryIndexDisabledHandler} to listen disable events of sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexDisabledHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryIndexDisabledHandler(final GeometryIndexDisabledHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledHandler() {

			public void onGeometryIndexDisabled(GeometryIndexDisabledEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexDisabledEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexDisabledEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexDisabled(e);
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryIndexDisabledHandler(h) });
	}

	/**
	 * Enable the given list of vertices/edges/sub-geometries for further editing.
	 * 
	 * @param indices
	 *            The list of indices to enable.
	 */
	public void enable(GeometryIndex[] indices) {
		delegate.enable(Arrays.asList(indices));
	}

	/**
	 * Disable the given list of vertices/edges/sub-geometries for further editing.
	 * 
	 * @param indices
	 *            The list of indices to disable.
	 */
	public void disable(GeometryIndex[] indices) {
		delegate.disable(Arrays.asList(indices));
	}

	/** Enable everything for further editing. Nothing remains disabled. */
	public void enableAll() {
		delegate.enableAll();
	}

	/**
	 * Is the given vertex/edge/sub-geometry currently enabled or disabled for further editing?
	 * 
	 * @param index
	 *            The vertex/edge/sub-geometry to check.
	 * @return True or false.
	 */
	public boolean isEnabled(GeometryIndex index) {
		return delegate.isEnabled(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning highlighting:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryIndexHighlightBeginHandler} to listen to highlighting begin events of sub-geometries,
	 * vertices and edges.
	 * 
	 * @param h
	 *            The {@link GeometryIndexHighlightBeginHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryIndexHighlightBeginHandler(final GeometryIndexHighlightBeginHandler h) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginHandler h2;
		h2 = new org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginHandler() {

			public void onGeometryIndexHighlightBegin(GeometryIndexHighlightBeginEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexHighlightBeginEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexHighlightBeginEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				h.onGeometryIndexHighlightBegin(e);
			}
		};
		return new JsHandlerRegistration(
				new HandlerRegistration[] { delegate.addGeometryIndexHighlightBeginHandler(h2) });
	}

	/**
	 * Register a {@link GeometryIndexHighlightEndHandler} to listen to highlighting end events of sub-geometries,
	 * vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexHighlightEndHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryIndexHighlightEndHandler(final GeometryIndexHighlightEndHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndHandler() {

			public void onGeometryIndexHighlightEnd(GeometryIndexHighlightEndEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexHighlightEndEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexHighlightEndEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexHighlightEnd(e);
			}
		};
		return new JsHandlerRegistration(new HandlerRegistration[] { delegate.addGeometryIndexHighlightEndHandler(h) });
	}

	/**
	 * Start highlighting a set vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The set of indices to highlight.
	 */
	public void highlightBegin(GeometryIndex[] indices) {
		delegate.highlightBegin(Arrays.asList(indices));
	}

	/**
	 * Stop highlighting a set vertices/edges/sub-geometries.
	 * 
	 * @param indices
	 *            The set of indices to stop highlighting.
	 */
	public void highlightEnd(GeometryIndex[] indices) {
		delegate.highlightEnd(Arrays.asList(indices));
	}

	/** End all highlighting for all vertices/edges/sub-geometries. */
	public void highlightEndAll() {
		delegate.highlightEndAll();
	}

	/**
	 * Is a certain index (vertex/edge/sub-geometry) highlighted or not?
	 * 
	 * @param index
	 *            The index to check for highlighting.
	 * @return true or false.
	 */
	public boolean isHightlighted(GeometryIndex index) {
		return delegate.isHightlighted(index);
	}

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
	public JsHandlerRegistration addGeometryIndexMarkForDeletionBeginHandler(
			final GeometryIndexMarkForDeletionBeginHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginHandler() {

			public void onGeometryIndexMarkForDeletionBegin(GeometryIndexMarkForDeletionBeginEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexMarkForDeletionBeginEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexMarkForDeletionBeginEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexMarkForDeletionBegin(e);
			}
		};
		return new JsHandlerRegistration(
				new HandlerRegistration[] { delegate.addGeometryIndexMarkForDeletionBeginHandler(h) });
	}

	/**
	 * Register a {@link GeometryIndexMarkForDeletionEndHandler} to listen to mark for deletion end events of
	 * sub-geometries, vertices and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexMarkForDeletionEndHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryIndexMarkForDeletionEndHandler(
			final GeometryIndexMarkForDeletionEndHandler handler) {
		org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndHandler h;
		h = new org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndHandler() {

			public void onGeometryIndexMarkForDeletionEnd(GeometryIndexMarkForDeletionEndEvent event) {
				org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexMarkForDeletionEndEvent e;
				List<GeometryIndex> indices = event.getIndices();
				e = new org.geomajas.plugin.editing.jsapi.client.event.state.GeometryIndexMarkForDeletionEndEvent(
						event.getGeometry(), indices.toArray(new GeometryIndex[indices.size()]));
				handler.onGeometryIndexMarkForDeletionEnd(e);
			}
		};
		return new JsHandlerRegistration(
				new HandlerRegistration[] { delegate.addGeometryIndexMarkForDeletionEndHandler(h) });
	}

	/**
	 * Mark a set vertices/edges/sub-geometries for deletion.
	 * 
	 * @param indices
	 *            The set of indices to mark for deletion.
	 */
	public void markForDeletionBegin(GeometryIndex[] indices) {
		delegate.markForDeletionBegin(Arrays.asList(indices));
	}

	/**
	 * Unmark a set vertices/edges/sub-geometries for deletion.
	 * 
	 * @param indices
	 *            The set of indices to unmark for deletion.
	 */
	public void markForDeletionEnd(GeometryIndex[] indices) {
		delegate.markForDeletionEnd(Arrays.asList(indices));
	}

	/** Unmark all vertices/edges/sub-geometries for deletion. */
	public void markForDeletionEndAll() {
		delegate.markForDeletionEndAll();
	}

	/**
	 * Is a certain vertex/edge/sub-geometry marked for deletion or not?
	 * 
	 * @param index
	 *            The index to check.
	 * @return true or false.
	 */
	public boolean isMarkedForDeletion(GeometryIndex index) {
		return delegate.isMarkedForDeletion(index);
	}
}