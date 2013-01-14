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

package org.geomajas.plugin.editing.client.merge.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the merging process for geometries has ended.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometryMergeStopEvent extends GwtEvent<GeometryMergeStopHandler> {

	private final Geometry geometry;

	/**
	 * Initialize this event with the resulting geometry of the merging process.
	 * 
	 * @param geometry
	 *            The geometry that is the result of the merging process.
	 */
	public GeometryMergeStopEvent(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public Type<GeometryMergeStopHandler> getAssociatedType() {
		return GeometryMergeStopHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryMergeStopHandler handler) {
		handler.onGeometryMergingStop(this);
	}

	/**
	 * Get the geometry.
	 * @return geometry last geometry that needs to be added before stopping
	 */
	public Geometry getGeometry() {
		return geometry;
	}
}