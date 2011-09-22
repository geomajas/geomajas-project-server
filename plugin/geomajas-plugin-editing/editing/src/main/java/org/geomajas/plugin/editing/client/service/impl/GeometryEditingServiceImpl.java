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

package org.geomajas.plugin.editing.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditChangeStateHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditDeleteEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditDeselectedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightBeginEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightEndEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditHighlightHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditInsertEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionBeginEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionEndEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditMarkForDeletionHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditMoveEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditOperationHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditSelectedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditSelectionHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditWorkflowHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexService;

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

	protected GeometryIndexServiceImpl indexService;

	private List<GeometryIndex> highlights = new ArrayList<GeometryIndex>();

	private List<GeometryIndex> markedForDeletion = new ArrayList<GeometryIndex>();

	private List<GeometryIndex> selection = new ArrayList<GeometryIndex>();

	private GeometryEditingState state = GeometryEditingState.IDLE;

	private boolean started;

	// ------------------------------------------------------------------------
	// Public constructors:
	// ------------------------------------------------------------------------

	public GeometryEditingServiceImpl() {
		eventBus = new SimpleEventBus();
		indexService = new GeometryIndexServiceImpl();
	}

	// ------------------------------------------------------------------------
	// Handler registration:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometryEditSelectionHandler(GeometryEditSelectionHandler handler) {
		return eventBus.addHandler(GeometryEditSelectionHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryEditWorkflowHandler(GeometryEditWorkflowHandler handler) {
		return eventBus.addHandler(GeometryEditWorkflowHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryEditOperationHandler(GeometryEditOperationHandler handler) {
		return eventBus.addHandler(GeometryEditOperationHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryEditHighlightHandler(GeometryEditHighlightHandler handler) {
		return eventBus.addHandler(GeometryEditHighlightHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryEditMarkForDeletionHandler(GeometryEditMarkForDeletionHandler handler) {
		return eventBus.addHandler(GeometryEditMarkForDeletionHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryEditChangeStateHandler(GeometryEditChangeStateHandler handler) {
		return eventBus.addHandler(GeometryEditChangeStateHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Methods concerning Workflow:
	// ------------------------------------------------------------------------

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

	public Geometry stop() {
		started = false;
		setEditingState(GeometryEditingState.IDLE);
		deselectAll();

		if (highlights.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(highlights);
			highlights.clear();
			eventBus.fireEvent(new GeometryEditHighlightEndEvent(geometry, clone));
		}

		if (markedForDeletion.size() > 0) {
			List<GeometryIndex> clone2 = new ArrayList<GeometryIndex>(markedForDeletion);
			markedForDeletion.clear();
			eventBus.fireEvent(new GeometryEditMarkForDeletionEndEvent(geometry, clone2));
		}

		eventBus.fireEvent(new GeometryEditStopEvent(geometry));
		return geometry;
	}

	// ------------------------------------------------------------------------
	// Edit operations:
	// ------------------------------------------------------------------------

	public void move(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryIndexNotFoundException {
		// TODO Dangerous! If it fails halfway, some changes have been made already. This contradicts the Javadoc...
		if (indices.size() == coordinates.size()) {
			for (int i = 0; i < indices.size(); i++) {
				GeometryIndex index = indices.get(i);
				List<Coordinate> coords = coordinates.get(i);
				switch (index.getType()) {
					case TYPE_VERTEX:
						indexService.setVertex(geometry, index, coords.get(0));
						break;
					default:
						throw new RuntimeException("Not implemented.");
				}
			}
			eventBus.fireEvent(new GeometryEditMoveEvent(geometry, indices));
		}
	}

	public void insert(List<GeometryIndex> indices, List<List<Coordinate>> coordinates)
			throws GeometryIndexNotFoundException {
		// TODO Dangerous! If it fails halfway, some changes have been made already. This contradicts the Javadoc...
		for (int i = 0; i < indices.size(); i++) {
			GeometryIndex index = indices.get(i);
			List<Coordinate> coords = coordinates.get(i);
			indexService.insert(geometry, index, coords);
		}
		eventBus.fireEvent(new GeometryEditInsertEvent(geometry, indices));
	}

	public void delete(List<GeometryIndex> indices) throws GeometryIndexNotFoundException {
		// TODO Dangerous! If it fails halfway, some changes have been made already. This contradicts the Javadoc...
		for (GeometryIndex index : indices) {
			indexService.delete(geometry, index);
		}
		eventBus.fireEvent(new GeometryEditDeleteEvent(geometry, indices));
	}

	// ------------------------------------------------------------------------
	// Methods concerning Vertex/Edge selection:
	// ------------------------------------------------------------------------

	public void select(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!selection.contains(index)) {
				temp.add(index);
			}
		}
		selection.addAll(temp);
		eventBus.fireEvent(new GeometryEditSelectedEvent(geometry, temp));
	}

	public void deselect(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (selection.contains(index)) {
				temp.add(index);
			}
		}
		selection.removeAll(temp);
		eventBus.fireEvent(new GeometryEditDeselectedEvent(geometry, temp));
	}

	public void deselectAll() {
		if (selection.size() > 0) {
			List<GeometryIndex> clone = new ArrayList<GeometryIndex>(selection);
			selection.clear();
			eventBus.fireEvent(new GeometryEditDeselectedEvent(geometry, clone));
		}
	}

	public boolean isSelected(GeometryIndex index) {
		return selection.contains(index);
	}

	public List<GeometryIndex> getSelection() {
		return selection;
	}

	// ------------------------------------------------------------------------
	// Methods concerning editing state:
	// ------------------------------------------------------------------------

	public GeometryEditingState getEditingState() {
		return state;
	}

	public void setEditingState(GeometryEditingState state) {
		this.state = state;
		eventBus.fireEvent(new GeometryEditChangeStateEvent(state));
	}

	// ------------------------------------------------------------------------
	// Methods concerning highlighting:
	// ------------------------------------------------------------------------

	public void highlightBegin(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!highlights.contains(index)) {
				temp.add(index);
			}
		}
		highlights.addAll(temp);
		eventBus.fireEvent(new GeometryEditHighlightBeginEvent(geometry, temp));
	}

	public void highlightEnd(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (highlights.contains(index)) {
				temp.add(index);
			}
		}
		highlights.removeAll(temp);
		eventBus.fireEvent(new GeometryEditHighlightEndEvent(geometry, temp));
	}

	public boolean isHightlighted(GeometryIndex index) {
		return highlights.contains(index);
	}

	// ------------------------------------------------------------------------
	// Methods concerning the mark for deletion:
	// ------------------------------------------------------------------------

	public void markForDeletionBegin(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (!markedForDeletion.contains(index)) {
				temp.add(index);
			}
		}
		markedForDeletion.addAll(temp);
		eventBus.fireEvent(new GeometryEditMarkForDeletionBeginEvent(geometry, temp));
	}

	public void markForDeletionEnd(List<GeometryIndex> indices) {
		List<GeometryIndex> temp = new ArrayList<GeometryIndex>();
		for (GeometryIndex index : indices) {
			if (markedForDeletion.contains(index)) {
				temp.add(index);
			}
		}
		markedForDeletion.removeAll(temp);
		eventBus.fireEvent(new GeometryEditMarkForDeletionEndEvent(geometry, temp));
	}

	public boolean isMarkedForDeletion(GeometryIndex index) {
		return markedForDeletion.contains(index);
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	public Geometry getGeometry() {
		return geometry;
	}

	public GeometryIndexService getIndexService() {
		return indexService;
	}
}