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

package org.geomajas.plugin.editing.jsapi.client.split;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopEvent;
import org.geomajas.plugin.editing.jsapi.client.GeometryArrayCallback;
import org.geomajas.plugin.editing.jsapi.client.GeometryArrayCallback.GeometryArrayHolder;
import org.geomajas.plugin.editing.jsapi.client.service.JsGeometryEditService;
import org.geomajas.plugin.editing.jsapi.client.split.event.GeometrySplitStartHandler;
import org.geomajas.plugin.editing.jsapi.client.split.event.GeometrySplitStopHandler;
import org.geomajas.plugin.jsapi.client.event.JsHandlerRegistration;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Service for geometry splitting. It uses a {@link JsGeometryEditService} to let the user draw a splitting line. Only
 * by calling the <code>stop</code> method, the splitting is executed.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometrySplitService")
@ExportPackage("org.geomajas.plugin.editing.split")
@Api(allMethods = true)
public class JsGeometrySplitService implements Exportable {

	private GeometrySplitService delegate;

	private JsGeometryEditService editService;

	/**
	 * Default constructor.
	 */
	public JsGeometrySplitService() {
		this.delegate = new GeometrySplitService();
		editService = new JsGeometryEditService(delegate.getGeometryEditService());
	}

	/**
	 * Constructor with a {@link GeometryEditService}, that if stopped, stops this service as well.
	 * 
	 * @param service
	 *            the {@link GeometryEditService} that needs to be used
	 */
	@NoExport
	public JsGeometrySplitService(GeometryEditService editService) {
		this.editService = new JsGeometryEditService(editService);
		this.delegate = new GeometrySplitService(editService);
	}

	//
	// public void setGeometryEditService(JsGeometryEditService editService) {
	// this.editService = editService;
	// this.delegate = new GeometrySplitService(editService.getDelegate());
	// }

	// ------------------------------------------------------------------------
	// Public methods for adding handlers:
	// ------------------------------------------------------------------------

	/**
	 * Get the delegating {@link GeometryEditService}.
	 * 
	 * @return delegate delegate
	 */
	@NoExport
	public GeometrySplitService getDelegate() {
		return delegate;
	}

	/**
	 * Register a {@link GeometrySplitStartHandler} to listen to events that signal the splitting process has started.
	 * 
	 * @param handler
	 *            The {@link GeometrySplitStartHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometrySplitStartHandler(final GeometrySplitStartHandler handler) {
		org.geomajas.plugin.editing.client.split.event.GeometrySplitStartHandler h;
		h = new org.geomajas.plugin.editing.client.split.event.GeometrySplitStartHandler() {

			public void onGeometrySplitStart(GeometrySplitStartEvent event) {
				Geometry geometry = event.getGeometry();
				org.geomajas.plugin.editing.jsapi.client.split.event.GeometrySplitStartEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.split.event.GeometrySplitStartEvent(geometry);
				handler.onGeometrySplitStart(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometrySplitStartHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	/**
	 * Register a {@link GeometrySplitStopHandler} to listen to events that signal the splitting process has ended
	 * (either through stop or cancel).
	 * 
	 * @param handler
	 *            The {@link GeometrySplitStopHandler} to add as listener.
	 * @return The registration of the handler.
	 */
	public JsHandlerRegistration addGeometrySplitStopHandler(final GeometrySplitStopHandler handler) {
		org.geomajas.plugin.editing.client.split.event.GeometrySplitStopHandler h;
		h = new org.geomajas.plugin.editing.client.split.event.GeometrySplitStopHandler() {

			public void onGeometrySplitStop(GeometrySplitStopEvent event) {
				Geometry geometry = event.getGeometry();
				org.geomajas.plugin.editing.jsapi.client.split.event.GeometrySplitStopEvent e;
				e = new org.geomajas.plugin.editing.jsapi.client.split.event.GeometrySplitStopEvent(geometry);
				handler.onGeometrySplitStop(e);
			}
		};
		HandlerRegistration registration = delegate.addGeometrySplitStopHandler(h);
		return new JsHandlerRegistration(new HandlerRegistration[] { registration });
	}

	// ------------------------------------------------------------------------
	// Public methods for splitting work-flow:
	// ------------------------------------------------------------------------

	/**
	 * Start splitting the given geometry.
	 * 
	 * @param geometry
	 *            to be split
	 */
	public void start(Geometry geometry) {
		delegate.start(geometry);
	}

	/**
	 * Stop splitting the geometry.
	 * 
	 * @param callback
	 *            the {@link GeometryArrayCallback} to be executed after the splitting has stopped
	 */
	public void stop(final GeometryArrayCallback callback) {
		if (callback == null) {
			delegate.stop(null);
		} else {
			delegate.stop(new GeometryArrayFunction() {

				public void execute(Geometry[] geometries) {
					callback.execute(new GeometryArrayHolder(geometries));
				}
			});
		}
	}

	// ------------------------------------------------------------------------
	// Getters:
	// ------------------------------------------------------------------------

	/**
	 * Get the geometry that needs to be split.
	 * 
	 * @return geometry that needs to be split
	 */
	public Geometry getGeometry() {
		return delegate.getGeometry();
	}

	/**
	 * Get the geometry that represents the split line.
	 * 
	 * @return geometry that represents the split line.
	 */
	public Geometry getSplitLine() {
		return delegate.getSplitLine();
	}

	/**
	 * Get the {@link JsGeometryEditService} that is used to edit the split line.
	 * 
	 * @return the {@link JsGeometryEditService} that is used to edit the split line
	 */
	public JsGeometryEditService getGeometryEditService() {
		return editService;
	}
}