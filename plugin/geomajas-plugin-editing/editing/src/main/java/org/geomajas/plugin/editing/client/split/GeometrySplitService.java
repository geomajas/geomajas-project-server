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

package org.geomajas.plugin.editing.client.split;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditServiceImpl;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartHandler;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopHandler;
import org.geomajas.plugin.editing.dto.GeometrySplitRequest;
import org.geomajas.plugin.editing.dto.GeometrySplitResponse;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * Service for geometry splitting. It uses a {@link GeometryEditService} to let the user draw a splitting line. Only by
 * calling the <code>stop</code> method, the splitting is executed.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometrySplitService {

	private final GeometryEditService service;

	private final EventBus eventBus;

	private Geometry geometry;

	private Geometry splitLine;

	private boolean started;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public GeometrySplitService() {
		this(new GeometryEditServiceImpl());
	}

	public GeometrySplitService(GeometryEditService service) {
		this.service = service;
		eventBus = new SimpleEventBus();

		service.addGeometryEditStopHandler(new GeometryEditStopHandler() {

			public void onGeometryEditStop(GeometryEditStopEvent event) {
				if (started) {
					stop(null);
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// Public regarding the splitting work-flow:
	// ------------------------------------------------------------------------

	public HandlerRegistration addGeometrySplitStartHandler(GeometrySplitStartHandler handler) {
		return eventBus.addHandler(GeometrySplitStartHandler.TYPE, handler);
	}

	public HandlerRegistration addGeometrySplitStopHandler(GeometrySplitStopHandler handler) {
		return eventBus.addHandler(GeometrySplitStopHandler.TYPE, handler);
	}

	public void start(Geometry geometry) {
		this.geometry = geometry;

		splitLine = new Geometry(Geometry.LINE_STRING, 0, 0);
		service.start(splitLine);
		service.setInsertIndex(service.getIndexService().create(GeometryIndexType.TYPE_VERTEX, 0));
		service.setEditingState(GeometryEditState.INSERTING);

		started = true;
		eventBus.fireEvent(new GeometrySplitStartEvent(geometry));
	}

	public void stop(final GeometryArrayFunction callback) {
		started = false;
		service.stop();
		if (callback != null) {
			calculate(new GeometryArrayFunction() {

				public void execute(Geometry[] geometries) {
					callback.execute(geometries);
					splitLine = null;
					eventBus.fireEvent(new GeometrySplitStopEvent(geometry));
				}
			});
		} else {
			eventBus.fireEvent(new GeometrySplitStopEvent(geometry));
			splitLine = null;
		}
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	public Geometry getGeometry() {
		return geometry;
	}

	public Geometry getSplitLine() {
		return splitLine;
	}

	public GeometryEditService getGeometryEditService() {
		return service;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void calculate(final GeometryArrayFunction callback) {
		geometry.setPrecision(-1);
		splitLine.setPrecision(-1);
		GeometrySplitRequest request = new GeometrySplitRequest(geometry, splitLine);
		GwtCommand command = new GwtCommand(GeometrySplitRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GeometrySplitResponse>() {

			public void execute(GeometrySplitResponse response) {
				callback.execute(response.getGeometries().toArray(new Geometry[response.getGeometries().size()]));
			}
		});
	}
}