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

package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.FutureApi;
import org.geomajas.geometry.Geometry;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired when the shape of the geometry has changed in any way (inserting, moving, deleting vertices or
 * undo/redo).
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class GeometryEditShapeChangedEvent extends GwtEvent<GeometryEditShapeChangedHandler> {

	private final Geometry geometry;

	public GeometryEditShapeChangedEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	public Type<GeometryEditShapeChangedHandler> getAssociatedType() {
		return GeometryEditShapeChangedHandler.TYPE;
	}

	protected void dispatch(GeometryEditShapeChangedHandler handler) {
		handler.onGeometryShapeChanged(this);
	}

	public Geometry getGeometry() {
		return geometry;
	}
}