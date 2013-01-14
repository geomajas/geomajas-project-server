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
 */
public class GeometryIndexStateServiceImpl implements GeometryIndexStateService {

	private final List<GeometryIndex> highlights = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> markedForDeletion = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> selection = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> disabled = new ArrayList<GeometryIndex>();

	private final List<GeometryIndex> snapped = new ArrayList<GeometryIndex>();

	private final GeometryEditServiceImpl editingService;

	// ------------------------------------------------------------------------
	// Public constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this service with a reference to the editing service to which this service belongs.
	 * 
	 * @param editingService
	 *            The editing service to which this service belongs.
	 */
	protected GeometryIndexStateServiceImpl(GeometryEditServiceImpl editingService) {
		this.editingService = editingService;
	}

	// ------------------------------------------------------------------------
	// Methods concerning Vertex/Edge selection:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexSelectedHandler(GeometryIndexSelectedHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexSelectedHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexDeselectedHandler(GeometryIndexDeselectedHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexDeselectedHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public void deselectAll() {
		if (selection.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(selection);
			selection.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexDeselectedEvent(editingService.getGeometry(), clone));
		}
	}

	/** {@inheritDoc} */
	public boolean isSelected(GeometryIndex index) {
		return selection.contains(index);
	}

	/** {@inheritDoc} */
	public List<GeometryIndex> getSelection() {
		return selection;
	}

	// ------------------------------------------------------------------------
	// Methods concerning disabling of indices:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexDisabledHandler(GeometryIndexDisabledHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexDisabledHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexEnabledHandler(GeometryIndexEnabledHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexEnabledHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public void enableAll() {
		if (disabled.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(disabled);
			disabled.clear();
			editingService.getEventBus().fireEvent(new GeometryIndexEnabledEvent(editingService.getGeometry(), clone));
		}
	}

	/** {@inheritDoc} */
	public boolean isEnabled(GeometryIndex index) {
		return !disabled.contains(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning highlighting:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexHighlightBeginHandler(GeometryIndexHighlightBeginHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexHighlightBeginHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexHighlightEndHandler(GeometryIndexHighlightEndHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexHighlightEndHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public void highlightEndAll() {
		if (highlights.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(highlights);
			highlights.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexHighlightEndEvent(editingService.getGeometry(), clone));
		}
	}

	/** {@inheritDoc} */
	public boolean isHightlighted(GeometryIndex index) {
		return highlights.contains(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning the mark for deletion:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexMarkForDeletionBeginHandler(
			GeometryIndexMarkForDeletionBeginHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexMarkForDeletionBeginHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexMarkForDeletionEndHandler(GeometryIndexMarkForDeletionEndHandler h) {
		return editingService.getEventBus().addHandler(GeometryIndexMarkForDeletionEndHandler.TYPE, h);
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public void markForDeletionEndAll() {
		if (markedForDeletion.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(markedForDeletion);
			markedForDeletion.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexMarkForDeletionEndEvent(editingService.getGeometry(), clone));
		}
	}

	/** {@inheritDoc} */
	public boolean isMarkedForDeletion(GeometryIndex index) {
		return markedForDeletion.contains(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning snapping:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexSnappingBeginHandler(GeometryIndexSnappingBeginHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexSnappingBeginHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addGeometryIndexSnappingEndHandler(GeometryIndexSnappingEndHandler handler) {
		return editingService.getEventBus().addHandler(GeometryIndexSnappingEndHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	public boolean isSnapped(GeometryIndex index) {
		return snapped.contains(index);
	}

	/** {@inheritDoc} */
	public void snappingEndAll() {
		if (snapped.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(snapped);
			snapped.clear();
			editingService.getEventBus().fireEvent(
					new GeometryIndexSnappingEndEvent(editingService.getGeometry(), clone));
		}
	}
}