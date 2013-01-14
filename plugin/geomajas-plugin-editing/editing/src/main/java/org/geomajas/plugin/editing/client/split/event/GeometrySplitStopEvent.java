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
package org.geomajas.plugin.editing.client.split.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Geometry;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the splitting process of a geometry has ended.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometrySplitStopEvent extends GwtEvent<GeometrySplitStopHandler> {

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

	@Override
	public Type<GeometrySplitStopHandler> getAssociatedType() {
		return GeometrySplitStopHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometrySplitStopHandler handler) {
		handler.onGeometrySplitStop(this);
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