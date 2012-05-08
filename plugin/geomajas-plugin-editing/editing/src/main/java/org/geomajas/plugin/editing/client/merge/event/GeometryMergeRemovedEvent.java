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

package org.geomajas.plugin.editing.client.merge.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports a geometry has been removed from the list for merging.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometryMergeRemovedEvent extends GwtEvent<GeometryMergeRemovedHandler> {

	private final Geometry geometry;

	/**
	 * Main constructor.
	 * @param geometry that needs to be removed
	 */
	public GeometryMergeRemovedEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public Type<GeometryMergeRemovedHandler> getAssociatedType() {
		return GeometryMergeRemovedHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryMergeRemovedHandler handler) {
		handler.onGeometryMergingRemoved(this);
	}

	/**
	 * Get the geometry that has been removed from the list for merging.
	 * 
	 * @return The geometry that has been removed from the list for merging.
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}