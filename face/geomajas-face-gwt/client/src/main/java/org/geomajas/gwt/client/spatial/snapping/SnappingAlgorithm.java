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

package org.geomajas.gwt.client.spatial.snapping;

import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;

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
