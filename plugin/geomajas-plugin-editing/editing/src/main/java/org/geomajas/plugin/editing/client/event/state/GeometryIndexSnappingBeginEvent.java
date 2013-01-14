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

package org.geomajas.plugin.editing.client.event.state;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.AbstractGeometryEditEvent;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Event which is passed when some part of a geometry has snapped to another geometry during editing.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometryIndexSnappingBeginEvent extends AbstractGeometryEditEvent<GeometryIndexSnappingBeginHandler> {

	/**
	 * Main constructor for this event.
	 * @param geometry that has snapped to another geometry
	 * @param indices 
	 */
	public GeometryIndexSnappingBeginEvent(Geometry geometry, List<GeometryIndex> indices) {
		super(geometry, indices);
	}

	/**
	 * {@inheritDoc}
	 */
	public Type<GeometryIndexSnappingBeginHandler> getAssociatedType() {
		return GeometryIndexSnappingBeginHandler.TYPE;
	}

	protected void dispatch(GeometryIndexSnappingBeginHandler handler) {
		handler.onGeometryIndexSnappingBegin(this);
	}
}