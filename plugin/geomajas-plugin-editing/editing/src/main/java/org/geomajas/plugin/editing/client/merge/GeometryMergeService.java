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

package org.geomajas.plugin.editing.client.merge;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.editing.client.GeometryFunction;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeAddedEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeAddedHandler;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeRemovedEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeRemovedHandler;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartHandler;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopHandler;
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
@Api(allMethods = true)
public class GeometryMergeService {

	private final List<Geometry> geometries = new ArrayList<Geometry>();

	private final EventBus eventBus = new SimpleEventBus();

	private boolean busy;

	private int precision = -1;

	private boolean usePrecisionAsBuffer;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public GeometryMergeService() {
	}

	// ------------------------------------------------------------------------
	// Public methods for adding handlers:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryMergeStartHandler} to listen to events that signal the merging process has started.
	 * 
	 * @param handler
	 *            The {@link GeometryMergeStartHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public HandlerRegistration addGeometryMergeStartHandler(GeometryMergeStartHandler handler) {
		return eventBus.addHandler(GeometryMergeStartHandler.TYPE, handler);
	}

	/**
	 * Register a {@link GeometryMergeStopHandler} to listen to events that signal the merging process has ended (either
	 * through stop or cancel).
	 * 
	 * @param handler
	 *            The {@link GeometryMergeStopHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public HandlerRegistration addGeometryMergeStopHandler(GeometryMergeStopHandler handler) {
		return eventBus.addHandler(GeometryMergeStopHandler.TYPE, handler);
	}

	/**
	 * Register a {@link GeometryMergeAddedHandler} to listen to events that signal a geometry has been added to the
	 * list for merging.
	 * 
	 * @param handler
	 *            The {@link GeometryMergeAddedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public HandlerRegistration addGeometryMergeAddedHandler(GeometryMergeAddedHandler handler) {
		return eventBus.addHandler(GeometryMergeAddedHandler.TYPE, handler);
	}

	/**
	 * Register a {@link GeometryMergeRemovedHandler} to listen to events that signal a geometry has been removed from
	 * the list for merging.
	 * 
	 * @param handler
	 *            The {@link GeometryMergeRemovedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public HandlerRegistration addGeometryMergeRemovedHandler(GeometryMergeRemovedHandler handler) {
		return eventBus.addHandler(GeometryMergeRemovedHandler.TYPE, handler);
	}

	// ------------------------------------------------------------------------
	// Public methods for merging work-flow:
	// ------------------------------------------------------------------------

	/**
	 * Start the merging process. From this point on add some geometries and call either <code>stop</code> or
	 * <code>cancel</code>.
	 * 
	 * @throws GeometryMergeException
	 *             In case a merging process is already started.
	 */
	public void start() throws GeometryMergeException {
		if (busy) {
			throw new GeometryMergeException("Can't start a new merging process while another one is still busy.");
		}
		busy = true;
		eventBus.fireEvent(new GeometryMergeStartEvent());
	}

	/**
	 * Add a geometry to the list for merging. When <code>stop</code> is called, it is this list that is merged.
	 * 
	 * @param geometry
	 *            The geometry to add.
	 * @throws GeometryMergeException
	 *             In case the merging process has not been started.
	 */
	public void addGeometry(Geometry geometry) throws GeometryMergeException {
		if (!busy) {
			throw new GeometryMergeException("Can't add a geometry if no merging process is active.");
		}
		geometries.add(geometry);
		eventBus.fireEvent(new GeometryMergeAddedEvent(geometry));
	}

	/**
	 * Remove a geometry from the merging list again.
	 * 
	 * @param geometry
	 *            The geometry to remove.
	 * @throws GeometryMergeException
	 *             In case the merging process has not been started.
	 */
	public void removeGeometry(Geometry geometry) throws GeometryMergeException {
		if (!busy) {
			throw new GeometryMergeException("Can't remove a geometry if no merging process is active.");
		}
		geometries.remove(geometry);
		eventBus.fireEvent(new GeometryMergeRemovedEvent(geometry));
	}

	/**
	 * Clear the entire list of geometries for merging, basically resetting the process.
	 * 
	 * @throws GeometryMergeException
	 *             In case the merging process has not been started.
	 */
	public void clearGeometries() throws GeometryMergeException {
		if (!busy) {
			throw new GeometryMergeException("Can't clear geometry list if no merging process is active.");
		}
		for (Geometry geometry : geometries) {
			eventBus.fireEvent(new GeometryMergeRemovedEvent(geometry));
		}
		geometries.clear();
	}

	/**
	 * End the merging process by effectively executing the merge operation and returning the result through a
	 * call-back.
	 * 
	 * @param callback
	 *            The call-back function that will receive the merged geometry.
	 * @throws GeometryMergeException
	 *             Thrown in case the merging process has not been started or some other merging error.
	 */
	public void stop(final GeometryFunction callback) throws GeometryMergeException {
		if (!busy) {
			throw new GeometryMergeException("Can't stop the merging process since it is not activated.");
		}
		if (callback == null) {
			cancel();
			return;
		}
		merge(new GeometryFunction() {

			public void execute(Geometry geometry) {
				callback.execute(geometry);
				try {
					clearGeometries();
				} catch (GeometryMergeException e) {
				}
				busy = false;
				eventBus.fireEvent(new GeometryMergeStopEvent(geometry));
			}
		});
	}

	/**
	 * End the merging process without executing the merge operation. This method will simply clean up.
	 * 
	 * @throws GeometryMergeException
	 *             In case the merging process has not been started.
	 */
	public void cancel() throws GeometryMergeException {
		clearGeometries();
		busy = false;
		eventBus.fireEvent(new GeometryMergeStopEvent(null));
	}

	/**
	 * Is the merging process currently active or not?
	 * 
	 * @return Is the merging process currently active or not?
	 */
	public boolean isBusy() {
		return busy;
	}

	/**
	 * Get the current precision to be used when merging geometries.
	 * 
	 * @return The current precision to be used when merging geometries.
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Set the precision to be used when merging geometries. Basically there are 2 options:
	 * <ul>
	 * <li>-1: Use a floating point precision model. This is the default value.</li>
	 * <li>&ge; 0: Use a fixed precision model. Know that larger values, although increasingly precise, can run into
	 * robustness problems.</li>
	 * </ul>
	 * 
	 * @param precision
	 *            The new value.
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	/**
	 * Set the boolean that triggers the usage of the precision value as buffer.
	 * <p><b>Default</b> = false<p>
	 * <p><b>Note</b>: If false all lines and points are ignored during the merging process.<p>
	 * 
	 * @param usePrecisionAsBuffer
	 */
	public void setUsePrecisionAsBuffer(boolean usePrecisionAsBuffer) {
		this.usePrecisionAsBuffer = usePrecisionAsBuffer;
	}
	
	/**
	 * Get the boolean that triggers the usage of the precision value as buffer.
	 * 
	 * @return usePrecisionAsBuffer
	 */
	public boolean usePrecisionAsBuffer() {
		return usePrecisionAsBuffer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void merge(final GeometryFunction callback) {
		GeometryMergeRequest request = new GeometryMergeRequest();
		request.setGeometries(geometries);
		request.setPrecision(precision);
		request.setUsePrecisionAsBuffer(usePrecisionAsBuffer);
		GwtCommand command = new GwtCommand(GeometryMergeRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GeometryMergeResponse>() {

			public void execute(GeometryMergeResponse response) {
				callback.execute(response.getGeometry());
			}
		});
	}
}