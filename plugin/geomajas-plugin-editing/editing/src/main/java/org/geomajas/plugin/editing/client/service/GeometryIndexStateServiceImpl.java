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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDeselectedHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexDisabledHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexEnabledHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexHighlightEndHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexMarkForDeletionEndHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSelectedHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSnappingBeginEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSnappingBeginHandler;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSnappingEndEvent;
import org.geomajas.plugin.editing.client.event.state.GeometryIndexSnappingEndHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Implementation of the {@link GeometryIndexStateService}.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexStateServiceImpl implements GeometryIndexStateService {

	private final List<GeometryIndex> highlights = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> markedForDeletion = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> selection = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> disabled = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> snapped = new ArrayList<GeometryIndex>();

	private final GeometryEditingServiceImpl editingService;

	// ------------------------------------------------------------------------
	// Public constructors:
	// ------------------------------------------------------------------------

	protected GeometryIndexStateServiceImpl(GeometryEditingServiceImpl editingService) {
		this.editingService = editingService;
	}

	// ------------------------------------------------------------------------
	// Methods concerning Vertex/Edge selection:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometryIndexSelectedHandler(GeometryIndexSelectedHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexSelectedHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryIndexDeselectedHandler(GeometryIndexDeselectedHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexDeselectedHandler.TYPE, handler);
	}

	public void select(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!selection.contains(index)) {
				temp.add(index);
			}
		}
		selection.addAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexSelectedEvent(editingService.getGeometry(), temp));
	}

	public void deselect(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (selection.contains(index)) {
				temp.add(index);
			}
		}
		selection.removeAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexDeselectedEvent(editingService.getGeometry(), temp));
	}

	public void deselectAll() {
		if (selection.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(selection);
			selection.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexDeselectedEvent(editingService.getGeometry(), clone));
		}
	}

	public boolean isSelected(GeometryIndex index) {
		return selection.contains(index);
	}

	public List<GeometryIndex> getSelection() {
		return selection;
	}

	// ------------------------------------------------------------------------
	// Methods concerning disabling of indices:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometryIndexDisabledHandler(GeometryIndexDisabledHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexDisabledHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryIndexEnabledHandler(GeometryIndexEnabledHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexEnabledHandler.TYPE, handler);
	}

	public void enable(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (disabled.contains(index)) {
				temp.add(index);
			}
		}
		disabled.removeAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexEnabledEvent(editingService.getGeometry(), temp));
	}

	public void disable(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!disabled.contains(index)) {
				temp.add(index);
			}
		}
		disabled.addAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexDisabledEvent(editingService.getGeometry(), temp));
	}

	public void enableAll() {
		if (disabled.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(disabled);
			disabled.clear();
			editingService.getEventBus().fireEvent(new GeometryIndexEnabledEvent(editingService.getGeometry(), clone));
		}
	}

	public boolean isEnabled(GeometryIndex index) {
		return !disabled.contains(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning highlighting:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometryIndexHighlightBeginHandler(GeometryIndexHighlightBeginHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexHighlightBeginHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryIndexHighlightEndHandler(GeometryIndexHighlightEndHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexHighlightEndHandler.TYPE, handler);
	}

	public void highlightBegin(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!highlights.contains(index)) {
				temp.add(index);
			}
		}
		highlights.addAll(temp);
		editingService.getEventBus()
				.fireEvent(new GeometryIndexHighlightBeginEvent(editingService.getGeometry(), temp));
	}

	public void highlightEnd(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (highlights.contains(index)) {
				temp.add(index);
			}
		}
		highlights.removeAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexHighlightEndEvent(editingService.getGeometry(), temp));
	}

	public void highlightEndAll() {
		if (highlights.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(highlights);
			highlights.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexHighlightEndEvent(editingService.getGeometry(), clone));
		}
	}

	public boolean isHightlighted(GeometryIndex index) {
		return highlights.contains(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning the mark for deletion:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometryIndexMarkForDeletionBeginHandler(
			GeometryIndexMarkForDeletionBeginHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexMarkForDeletionBeginHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryIndexMarkForDeletionEndHandler(GeometryIndexMarkForDeletionEndHandler h) {
		return editingService.getEventBus().addHandler(GeometryIndexMarkForDeletionEndHandler.TYPE, h);
	}

	public void markForDeletionBegin(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!markedForDeletion.contains(index)) {
				temp.add(index);
			}
		}
		markedForDeletion.addAll(temp);
		editingService.getEventBus().fireEvent(
				new GeometryIndexMarkForDeletionBeginEvent(editingService.getGeometry(), temp));
	}

	public void markForDeletionEnd(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (markedForDeletion.contains(index)) {
				temp.add(index);
			}
		}
		markedForDeletion.removeAll(temp);
		editingService.getEventBus().fireEvent(
				new GeometryIndexMarkForDeletionEndEvent(editingService.getGeometry(), temp));
	}

	public void markForDeletionEndAll() {
		if (markedForDeletion.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(markedForDeletion);
			markedForDeletion.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexMarkForDeletionEndEvent(editingService.getGeometry(), clone));
		}
	}

	public boolean isMarkedForDeletion(GeometryIndex index) {
		return markedForDeletion.contains(index);
	}

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
	public HandlerRegistration addGeometryIndexSnappingBeginHandler(GeometryIndexSnappingBeginHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexSnappingBeginHandler.TYPE, handler);
	}

	/**
	 * Register a {@link GeometryIndexSnappingEndHandler} to listen to snapping end events of sub-geometries, vertices
	 * and edges.
	 * 
	 * @param handler
	 *            The {@link GeometryIndexSnappingEndHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public HandlerRegistration addGeometryIndexSnappingEndHandler(GeometryIndexSnappingEndHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexSnappingEndHandler.TYPE, handler);
	}

	/**
	 * Add the given list of indices to the list of snapped indices.
	 * 
	 * @param indices
	 *            The indices that have snapped to some external geometry.
	 */
	public void snappingBegin(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!snapped.contains(index)) {
				temp.add(index);
			}
		}
		snapped.addAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexSnappingBeginEvent(editingService.getGeometry(), temp));
	}

	/**
	 * Unmark the given indices as being snapped. They return to their normal state and location.
	 * 
	 * @param indices
	 *            The indices that have stopped snapping.
	 */
	public void snappingEnd(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (snapped.contains(index)) {
				temp.add(index);
			}
		}
		snapped.removeAll(temp);
		editingService.getEventBus().fireEvent(new GeometryIndexSnappingEndEvent(editingService.getGeometry(), temp));
	}

	/**
	 * Has a certain index snapped to some external geometry or not?
	 * 
	 * @param index
	 *            The index to check.
	 * @return True or false.
	 */
	public boolean isSnapped(GeometryIndex index) {
		return snapped.contains(index);
	}

	/** Empty the list of snapped indices. */
	public void snappingEndAll() {
		if (snapped.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(snapped);
			snapped.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexSnappingEndEvent(editingService.getGeometry(), clone));
		}
	}
}