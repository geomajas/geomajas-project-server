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

package org.geomajas.plugin.editing.jsapi.client.merging;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.GeometryFunction;
import org.geomajas.plugin.editing.client.merging.GeometryMergingException;
import org.geomajas.plugin.editing.client.merging.GeometryMergingService;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartEvent;
import org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopEvent;
import org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingAddedHandler;
import org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingRemovedHandler;
import org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingStartHandler;
import org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingStopHandler;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Service for the process of merging multiple geometries into a single geometry.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryMergingService")
@ExportPackage("org.geomajas.plugin.editing.merging")
@Api(allMethods = true)
public class JsGeometryMergingService implements Exportable {

	private final GeometryMergingService delegate;

	public JsGeometryMergingService() {
		this.delegate = new GeometryMergingService();
	}

	// ------------------------------------------------------------------------
	// Public methods for adding handlers:
	// ------------------------------------------------------------------------

	/**
	 * Register a {@link GeometryMergingStartHandler} to listen to events that signal the merging process has started.
	 * 
	 * @param handler
	 *            The {@link GeometryMergingStartHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergingStartHandler(final GeometryMergingStartHandler handler) {
		org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartHandler h;
		h = new org.geomajas.plugin.editing.client.merging.event.GeometryMergingStartHandler() {

			public void onGeometryMergingStart(GeometryMergingStartEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingStartEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingStartEvent();
				handler.onGeometryMergingStart(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergingStartHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryMergingStopHandler} to listen to events that signal the merging process has ended
	 * (either through stop or cancel).
	 * 
	 * @param handler
	 *            The {@link GeometryMergingStopHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergingStopHandler(final GeometryMergingStopHandler handler) {
		org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopHandler h;
		h = new org.geomajas.plugin.editing.client.merging.event.GeometryMergingStopHandler() {

			public void onGeometryMergingStop(GeometryMergingStopEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingStopEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingStopEvent();
				handler.onGeometryMergingStop(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergingStopHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryMergingAddedHandler} to listen to events that signal a geometry has been added to the
	 * list for merging.
	 * 
	 * @param handler
	 *            The {@link GeometryMergingAddedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergingAddedHandler(final GeometryMergingAddedHandler handler) {
		org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedHandler h;
		h = new org.geomajas.plugin.editing.client.merging.event.GeometryMergingAddedHandler() {

			public void onGeometryMergingAdded(GeometryMergingAddedEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingAddedEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingAddedEvent(
						event.getGeometry());
				handler.onGeometryMergingAdded(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergingAddedHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryMergingRemovedHandler} to listen to events that signal a geometry has been removed from
	 * the list for merging.
	 * 
	 * @param handler
	 *            The {@link GeometryMergingRemovedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergingRemovedHandler(final GeometryMergingRemovedHandler handler) {
		org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedHandler h;
		h = new org.geomajas.plugin.editing.client.merging.event.GeometryMergingRemovedHandler() {

			public void onGeometryMergingRemoved(GeometryMergingRemovedEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingRemovedEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merging.event.GeometryMergingRemovedEvent(
						event.getGeometry());
				handler.onGeometryMergingRemoved(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergingRemovedHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	// ------------------------------------------------------------------------
	// Public methods for merging work-flow:
	// ------------------------------------------------------------------------

	/**
	 * Start the merging process. From this point on add some geometries and call either <code>stop</code> or
	 * <code>cancel</code>.
	 * 
	 * @throws GeometryMergingException
	 *             In case a merging process is already started.
	 */
	public void start() throws GeometryMergingException {
		delegate.start();
	}

	/**
	 * Add a geometry to the list for merging. When <code>stop</code> is called, it is this list that is merged.
	 * 
	 * @param geometry
	 *            The geometry to add.
	 * @throws GeometryMergingException
	 *             In case the merging process has not been started.
	 */
	public void addGeometry(Geometry geometry) throws GeometryMergingException {
		delegate.addGeometry(geometry);
	}

	/**
	 * Remove a geometry from the merging list again.
	 * 
	 * @param geometry
	 *            The geometry to remove.
	 * @throws GeometryMergingException
	 *             In case the merging process has not been started.
	 */
	public void removeGeometry(Geometry geometry) throws GeometryMergingException {
		delegate.removeGeometry(geometry);
	}

	/**
	 * Clear the entire list of geometries for merging, basically resetting the process.
	 * 
	 * @throws GeometryMergingException
	 *             In case the merging process has not been started.
	 */
	public void clearGeometries() throws GeometryMergingException {
		delegate.clearGeometries();
	}

	/**
	 * End the merging process by effectively executing the merge operation and returning the result through a
	 * call-back.
	 * 
	 * @param callback
	 *            The call-back function that will receive the merged geometry.
	 * @throws GeometryMergingException
	 *             Thrown in case the merging process has not been started or some other merging error.
	 */
	public void stop(final GeometryFunction callback) throws GeometryMergingException {
		delegate.stop(callback);
	}

	/**
	 * End the merging process without executing the merge operation. This method will simply clean up.
	 * 
	 * @throws GeometryMergingException
	 *             In case the merging process has not been started.
	 */
	public void cancel() throws GeometryMergingException {
		delegate.cancel();
	}

	/**
	 * Is the merging process currently active or not?
	 * 
	 * @return Is the merging process currently active or not?
	 */
	public boolean isBusy() {
		return delegate.isBusy();
	}

	/**
	 * Get the current precision to be used when merging geometries.
	 * 
	 * @return The current precision to be used when merging geometries.
	 */
	public int getPrecision() {
		return delegate.getPrecision();
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
		delegate.setPrecision(precision);
	}
}