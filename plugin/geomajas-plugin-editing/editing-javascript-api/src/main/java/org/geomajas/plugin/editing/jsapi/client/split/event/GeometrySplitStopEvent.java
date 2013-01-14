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

package org.geomajas.plugin.editing.jsapi.client.split.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event that reports the splitting process of a geometry has ended.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.split.event")
public class GeometrySplitStopEvent extends JsEvent<GeometrySplitStopHandler> implements Exportable {

	private final Geometry geometry;

	/**
	 * Initialize this event with the resulting geometry of the splitting process.
	 * 
	 * @param geometry
	 *            The geometry that is the result of the splitting process.
	 */
	public GeometrySplitStopEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometrySplitStopHandler> getType() {
		return GeometrySplitStopHandler.class;
	}

	protected void dispatch(GeometrySplitStopHandler handler) {
		handler.onGeometrySplitStop(this);
	}

	/**
	 * Get the resulting geometry from the splitting process..
	 * 
	 * @return The result.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}