/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.puregwt.client.map;

import org.geomajas.geometry.Coordinate;

/**
 * An immutable class that holds the view port state.
 * 
 * @author Jan De Moerloose
 */
public class ViewPortState {

	private static final double EQUAL_CHECK_DELTA = 1e-10;

	private final double scale;

	private final Coordinate origin;

	private final Coordinate panOrigin;

	private final boolean panDragging;
	
	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Creates a map view state with scale 1 and both screen and pan origin in the origin of the map.
	 */
	protected ViewPortState() {
		this(1.0, new Coordinate(0, 0), new Coordinate(0, 0), false);
	}

	protected ViewPortState(double scale, Coordinate origin, Coordinate panOrigin, boolean panDragging) {
		this.scale = scale;
		this.origin = (Coordinate) origin.clone();
		this.panOrigin = (Coordinate) panOrigin.clone();
		this.panDragging = panDragging;
	}
	
	// ------------------------------------------------------------------------
	// General getters and setters:
	// ------------------------------------------------------------------------

	public double getX() {
		return origin.getX();
	}

	public double getY() {
		return origin.getY();
	}

	public double getPanX() {
		return panOrigin.getX();
	}

	public double getPanY() {
		return panOrigin.getY();
	}

	public double getScale() {
		return scale;
	}

	/**
	 * Is the view state in the dragging part of the panning movement ?
	 * @return true if dragging
	 */
	public boolean isPanDragging() {
		return panDragging;
	}

	/**
	 * Can this view state be reached by panning from another view state ?
	 * @param viewState the view state to compare with
	 * @return true if reachable by panning
	 */
	public boolean isPannableFrom(ViewPortState other) {
		return Math.abs(scale - other.getScale()) < EQUAL_CHECK_DELTA
				&& Math.abs(getPanX() - other.getPanX()) < EQUAL_CHECK_DELTA
				&& Math.abs(getPanY() - other.getPanY()) < EQUAL_CHECK_DELTA;
	}

	/**
	 * Does this view state have the same scale as another view state ?
	 * @param other the view state to compare with
	 * @return true if same scale
	 */
	public boolean isSameScale(ViewPortState other) {
		return Math.abs(scale - other.getScale()) < EQUAL_CHECK_DELTA;
	}

	public String toString() {
		return "[origin=" + origin + ",panOrigin=" + panOrigin + ",panDragging=" + panDragging + "]";
	}
	
	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected ViewPortState copyAndSetPanDragging(boolean panDragging) {
		return new ViewPortState(scale, origin, panOrigin, panDragging);
	}

	protected ViewPortState copyAndSetScale(double scale) {
		return new ViewPortState(scale, origin, panOrigin, false);
	}

	protected ViewPortState copyAndSetOrigin(double x, double y) {
		return new ViewPortState(scale, new Coordinate(x, y), panOrigin, false);
	}

	protected ViewPortState copyAndSetPanOrigin(double x, double y) {
		return new ViewPortState(scale, origin, new Coordinate(x, y), false);
	}
}