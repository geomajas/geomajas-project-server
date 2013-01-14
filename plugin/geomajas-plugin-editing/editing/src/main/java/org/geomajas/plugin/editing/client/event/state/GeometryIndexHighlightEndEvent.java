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

import org.geomajas.annotation.FutureApi;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.event.AbstractGeometryEditEvent;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

/**
 * Event which is passed when highlighting some part of a geometry has ended during geometry editing.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class GeometryIndexHighlightEndEvent extends AbstractGeometryEditEvent<GeometryIndexHighlightEndHandler> {

	public GeometryIndexHighlightEndEvent(Geometry geometry, List<GeometryIndex> indices) {
		super(geometry, indices);
	}

	public Type<GeometryIndexHighlightEndHandler> getAssociatedType() {
		return GeometryIndexHighlightEndHandler.TYPE;
	}

	protected void dispatch(GeometryIndexHighlightEndHandler handler) {
		handler.onGeometryIndexHighlightEnd(this);
	}
}