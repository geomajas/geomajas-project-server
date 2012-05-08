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

package org.geomajas.plugin.editing.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event fired when the shape of the geometry has changed in any way (inserting, moving, deleting vertices or
 * undo/redo).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.event")
public class GeometryEditShapeChangedEvent extends JsEvent<GeometryEditShapeChangedHandler> implements Exportable {

	private Geometry geometry;

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry
	 */
	public GeometryEditShapeChangedEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometryEditShapeChangedHandler> getType() {
		return GeometryEditShapeChangedHandler.class;
	}

	protected void dispatch(GeometryEditShapeChangedHandler handler) {
		handler.onShapeChanged(this);
	}

	/**
	 * Get the geometry that is currently being edited.
	 * 
	 * @return The geometry that is currently being edited.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}