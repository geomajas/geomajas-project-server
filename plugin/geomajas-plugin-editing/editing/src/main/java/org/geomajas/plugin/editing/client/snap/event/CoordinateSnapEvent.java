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
package org.geomajas.plugin.editing.client.snap.event;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports a coordinate has snapped.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class CoordinateSnapEvent extends GwtEvent<CoordinateSnapHandler> {

	private final Coordinate from;

	private final Coordinate to;

	/**
	 * Create a coordinate snap event with the required snapping information.
	 * 
	 * @param from
	 *            The original location (before it was snapped).
	 * @param to
	 *            The target location (after it was snapped).
	 */
	public CoordinateSnapEvent(Coordinate from, Coordinate to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public Type<CoordinateSnapHandler> getAssociatedType() {
		return CoordinateSnapHandler.TYPE;
	}

	@Override
	protected void dispatch(CoordinateSnapHandler geometryEditChangeStateHandler) {
		geometryEditChangeStateHandler.onCoordinateSnapAttempt(this);
	}

	/**
	 * Has the snapping actually returned result or not?
	 * 
	 * @return
	 */
	public boolean hasSnapped() {
		return !from.equals(to);
	}

	/**
	 * Get the original coordinate before it was snapped.
	 * 
	 * @return The original coordinate before it was snapped.
	 */
	public Coordinate getFrom() {
		return from;
	}

	/**
	 * Get the resulting coordinate after it was snapped. This is the target location.
	 * 
	 * @return The resulting coordinate after it was snapped.
	 */
	public Coordinate getTo() {
		return to;
	}
}