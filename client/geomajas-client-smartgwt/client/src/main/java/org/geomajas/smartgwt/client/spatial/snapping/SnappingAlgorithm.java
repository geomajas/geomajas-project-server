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

package org.geomajas.smartgwt.client.spatial.snapping;

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;

/**
 * <p>
 * General definition of a snapping algorithm. All snapping algorithms get their target geometries to snap to at
 * construction. It is recommended to set up the geometries at this time (if they need sorting or whatever). Later on
 * the "getSnappingPoint" method gets actual snapped coordinates for the given coordinate. Together with a snapped
 * coordinate, it is also possible to ask for the eventual snapping distance through the getMinimumDistance method.
 * </p>
 *
 * @author Pieter De Graef
 */
public abstract class SnappingAlgorithm {

	/**
	 * The minimum distance at which a coordinate was last snapped.
	 */
	protected double minimumDistance;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * The only constructor. Always requires a list of geometries.
	 */
	protected SnappingAlgorithm(List<Geometry> geometries) {
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Calculates a snapping point from the given coordinate.
	 *
	 * @param original
	 *            The original and unsnapped coordinate.
	 * @param threshold
	 *            A threshold value that needs to be beaten in order to snap. Only if the distance between the original
	 *            and the candidate coordinate is smaller then this threshold, can the candidate coordinate be a snapped
	 *            coordinate.
	 * @return Returns the eventual snapped coordinate, or null if no snapping occurred.
	 */
	abstract Coordinate getSnappingPoint(Coordinate original, double threshold);

	/**
	 * Get the distance between the original and lastly snapped point. If the "getSnappingPoint" returned "null", then
	 * this will be Double.MAX_VALUE. This method will only contain a useful value, if a snapping point was actually
	 * found.
	 */
	public double getMinimumDistance() {
		return minimumDistance;
	}
}
