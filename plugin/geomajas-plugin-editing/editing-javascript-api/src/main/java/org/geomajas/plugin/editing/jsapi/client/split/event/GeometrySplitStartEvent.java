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
 * Event that reports the splitting process of a geometry has begun.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.split.event")
public class GeometrySplitStartEvent extends JsEvent<GeometrySplitStartHandler> implements Exportable {

	private final Geometry geometry;

	/**
	 * Initialize the event with the geometry that is to be splitted.
	 * 
	 * @param geometry
	 *            The target geometry for splitting.
	 */
	public GeometrySplitStartEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometrySplitStartHandler> getType() {
		return GeometrySplitStartHandler.class;
	}

	protected void dispatch(GeometrySplitStartHandler handler) {
		handler.onGeometrySplitStart(this);
	}

	/**
	 * Get the geometry that will be splitted.
	 * 
	 * @return The geometry that is to be splitted.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}