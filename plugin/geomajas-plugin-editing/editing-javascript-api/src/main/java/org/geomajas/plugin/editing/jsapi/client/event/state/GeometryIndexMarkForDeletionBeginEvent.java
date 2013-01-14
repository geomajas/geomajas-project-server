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

package org.geomajas.plugin.editing.jsapi.client.event.state;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.jsapi.client.event.JsEvent;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Event which is passed when some part of a geometry has been marked for deletion during geometry editing. This event
 * does not signal the actual deletion though.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.plugin.editing.event")
public class GeometryIndexMarkForDeletionBeginEvent extends JsEvent<GeometryIndexMarkForDeletionBeginHandler> implements
		Exportable {

	private Geometry geometry;

	private GeometryIndex[] indices;

	/**
	 * Main constructor.
	 * 
	 * @param geometry geometry
	 * @param indices indices
	 */
	public GeometryIndexMarkForDeletionBeginEvent(Geometry geometry, GeometryIndex[] indices) {
		this.geometry = geometry;
		this.indices = indices;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<GeometryIndexMarkForDeletionBeginHandler> getType() {
		return GeometryIndexMarkForDeletionBeginHandler.class;
	}

	protected void dispatch(GeometryIndexMarkForDeletionBeginHandler handler) {
		handler.onGeometryIndexMarkForDeletionBegin(this);
	}

	/**
	 * Get the geometry that is currently being edited.
	 * 
	 * @return The geometry that is currently being edited.
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Get the list of geometries/vertices/edges that are the subject of this event. These indices apply onto the given
	 * geometry. If the returned list is null, the geometry itself is considered subject.
	 * 
	 * @return The list of geometries/vertices/edges that are the subject of this event.
	 */
	public GeometryIndex[] getIndices() {
		return indices;
	}
}