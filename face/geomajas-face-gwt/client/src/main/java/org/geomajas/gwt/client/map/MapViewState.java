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

package org.geomajas.gwt.client.map;

import org.geomajas.geometry.Coordinate;

/**
 * An immutable class that holds the map view state.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MapViewState {

	private static final double EQUAL_CHECK_DELTA = 1e-10;

	private final double scale;

	private final Coordinate origin;

	private final Coordinate panOrigin;

	private final boolean panDragging;

	/**
	 * Creates a map view state with scale 1 and both screen and pan origin in the origin of the map.
	 */
	public MapViewState() {
		this(1.0, new Coordinate(0, 0), new Coordinate(0, 0), false);
	}

	public MapViewState(double scale, Coordinate origin, Coordinate panOrigin, boolean panDragging) {
		this.scale = scale;
		this.origin = (Coordinate) origin.clone();
		this.panOrigin = (Coordinate) panOrigin.clone();
		this.panDragging = panDragging;
	}

	public MapViewState copyAndSetPanDragging(boolean panDragging) {
		return new MapViewState(scale, origin, panOrigin, panDragging);
	}

	public MapViewState copyAndSetScale(double scale) {
		return new MapViewState(scale, origin, panOrigin, panDragging);
	}

	public MapViewState copyAndSetOrigin(double x, double y) {
		return new MapViewState(scale, new Coordinate(x, y), panOrigin, panDragging);
	}

	public MapViewState copyAndSetPanOrigin(double x, double y) {
		return new MapViewState(scale, origin, new Coordinate(x, y), panDragging);
	}

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
	public boolean isPannableFrom(MapViewState other) {
		return Math.abs(scale - other.getScale()) < EQUAL_CHECK_DELTA
				&& Math.abs(getPanX() - other.getPanX()) < EQUAL_CHECK_DELTA
				&& Math.abs(getPanY() - other.getPanY()) < EQUAL_CHECK_DELTA;
	}

	/**
	 * Does this view state have the same scale as another view state ?
	 * @param other the view state to compare with
	 * @return true if same scale
	 */
	public boolean isSameScale(MapViewState other) {
		return Math.abs(scale - other.getScale()) < EQUAL_CHECK_DELTA;
	}

	public String toString() {
		return "[origin=" + origin + ",panOrigin=" + panOrigin + ",panDragging=" + panDragging + "]";
	}

}
