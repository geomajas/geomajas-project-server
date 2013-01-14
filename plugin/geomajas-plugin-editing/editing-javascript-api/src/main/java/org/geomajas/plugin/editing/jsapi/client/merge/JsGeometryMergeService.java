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

package org.geomajas.plugin.editing.jsapi.client.merge;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.GeometryFunction;
import org.geomajas.plugin.editing.client.merge.GeometryMergeException;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeAddedEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeRemovedEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartEvent;
import org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopEvent;
import org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeAddedHandler;
import org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeRemovedHandler;
import org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeStartHandler;
import org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeStopHandler;
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
@Export("GeometryMergeService")
@ExportPackage("org.geomajas.plugin.editing.merge")
@Api(allMethods = true)
public class JsGeometryMergeService implements Exportable {

	private final GeometryMergeService delegate;

	/**
	 * Default constructor.
	 */
	public JsGeometryMergeService() {
		this.delegate = new GeometryMergeService();
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
	public JsHandlerRegistration addGeometryMergeStartHandler(final GeometryMergeStartHandler handler) {
		org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartHandler h;
		h = new org.geomajas.plugin.editing.client.merge.event.GeometryMergeStartHandler() {

			public void onGeometryMergingStart(GeometryMergeStartEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeStartEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeStartEvent();
				handler.onGeometryMergeStart(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergeStartHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryMergeStopHandler} to listen to events that signal the merging process has ended
	 * (either through stop or cancel).
	 * 
	 * @param handler
	 *            The {@link GeometryMergeStopHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergeStopHandler(final GeometryMergeStopHandler handler) {
		org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopHandler h;
		h = new org.geomajas.plugin.editing.client.merge.event.GeometryMergeStopHandler() {

			public void onGeometryMergingStop(GeometryMergeStopEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeStopEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeStopEvent();
				handler.onGeometryMergeStop(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergeStopHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryMergeAddedHandler} to listen to events that signal a geometry has been added to the
	 * list for merging.
	 * 
	 * @param handler
	 *            The {@link GeometryMergeAddedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergeAddedHandler(final GeometryMergeAddedHandler handler) {
		org.geomajas.plugin.editing.client.merge.event.GeometryMergeAddedHandler h;
		h = new org.geomajas.plugin.editing.client.merge.event.GeometryMergeAddedHandler() {

			public void onGeometryMergingAdded(GeometryMergeAddedEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeAddedEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeAddedEvent(
						event.getGeometry());
				handler.onGeometryMergeAdded(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergeAddedHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometryMergeRemovedHandler} to listen to events that signal a geometry has been removed from
	 * the list for merging.
	 * 
	 * @param handler
	 *            The {@link GeometryMergeRemovedHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometryMergeRemovedHandler(final GeometryMergeRemovedHandler handler) {
		org.geomajas.plugin.editing.client.merge.event.GeometryMergeRemovedHandler h;
		h = new org.geomajas.plugin.editing.client.merge.event.GeometryMergeRemovedHandler() {

			public void onGeometryMergingRemoved(GeometryMergeRemovedEvent event) {
				org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeRemovedEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.merge.event.GeometryMergeRemovedEvent(
						event.getGeometry());
				handler.onGeometryMergeRemoved(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometryMergeRemovedHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
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
		delegate.start();
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
		delegate.addGeometry(geometry);
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
		delegate.removeGeometry(geometry);
	}

	/**
	 * Clear the entire list of geometries for merging, basically resetting the process.
	 * 
	 * @throws GeometryMergeException
	 *             In case the merging process has not been started.
	 */
	public void clearGeometries() throws GeometryMergeException {
		delegate.clearGeometries();
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
		delegate.stop(callback);
	}

	/**
	 * End the merging process without executing the merge operation. This method will simply clean up.
	 * 
	 * @throws GeometryMergeException
	 *             In case the merging process has not been started.
	 */
	public void cancel() throws GeometryMergeException {
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