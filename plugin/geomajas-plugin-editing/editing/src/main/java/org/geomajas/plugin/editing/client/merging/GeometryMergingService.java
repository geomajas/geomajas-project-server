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

package org.geomajas.plugin.editing.client.merging;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.editing.client.GeometryFunction;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedHandler;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedHandler;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartHandler;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopHandler;
import org.geomajas.plugin.editing.dto.GeometryMergeRequest;
import org.geomajas.plugin.editing.dto.GeometryMergeResponse;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * Service for the process of merging multiple geometries into a single geometry.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class GeometryMergingService {

	private final List<Geometry> geometries = new ArrayList<Geometry>();

	private final EventBus eventBus = new SimpleEventBus();

	private boolean busy;

	public GeometryMergingService() {
	}

	// ------------------------------------------------------------------------
	// Public methods for adding handlers:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometryMergingStartHandler(GeometryMergingStartHandler handler) {
		return eventBus.addHandler(GeometryMergingStartHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryMergingStopHandler(GeometryMergingStopHandler handler) {
		return eventBus.addHandler(GeometryMergingStopHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryMergingAddedHandler(GeometryMergingAddedHandler handler) {
		return eventBus.addHandler(GeometryMergingAddedHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometryMergingRemovedHandler(GeometryMergingRemovedHandler handler) {
		return eventBus.addHandler(GeometryMergingRemovedHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Public methods for merging work-flow:
	// ------------------------------------------------------------------------

	public void start() throws GeometryMergingException {
		if (busy) {
			throw new GeometryMergingException("Can't start a new merging process while another one is still busy.");
		}
		busy = true;
		eventBus.fireEvent(new GeometryMergingStartEvent());
	}

	public void addGeometry(Geometry geometry) throws GeometryMergingException {
		if (!busy) {
			throw new GeometryMergingException("Can't add a geometry if no merging process is active.");
		}
		geometries.add(geometry);
		eventBus.fireEvent(new GeometryMergingAddedEvent(geometry));
	}

	public void removeGeometry(Geometry geometry) throws GeometryMergingException {
		if (!busy) {
			throw new GeometryMergingException("Can't remove a geometry if no merging process is active.");
		}
		geometries.remove(geometry);
		eventBus.fireEvent(new GeometryMergingRemovedEvent(geometry));
	}

	public void clearGeometries() throws GeometryMergingException {
		if (!busy) {
			throw new GeometryMergingException("Can't clear geometry list if no merging process is active.");
		}
		for (Geometry geometry : geometries) {
			eventBus.fireEvent(new GeometryMergingRemovedEvent(geometry));
		}
		geometries.clear();
	}

	public void stop(final GeometryFunction callback) {
		merge(new GeometryFunction() {

			public void execute(Geometry geometry) {
				callback.execute(geometry);
				busy = false;
				geometries.clear();
				eventBus.fireEvent(new GeometryMergingStopEvent(geometry));
			}
		});
	}
	
	public boolean isBusy() {
		return busy;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void merge(final GeometryFunction callback) {
		GeometryMergeRequest request = new GeometryMergeRequest();
		request.setGeometries(geometries);
		request.setPrecision(5);
		GwtCommand command = new GwtCommand(GeometryMergeRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GeometryMergeResponse>() {

			public void execute(GeometryMergeResponse response) {
				callback.execute(response.getGeometry());
			}
		});
	}
}