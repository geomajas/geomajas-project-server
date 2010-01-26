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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.LineSegment;
import org.geomajas.gwt.client.spatial.geometry.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Snapping algorithm that not only snaps to end-points of nearby geometries, but to any point on any of the edges as
 * well. As you might guess, this is a heavier calculation then the {@link ClosestPointAlgorithm}. It tries to calculate
 * distances between the given coordinate, and any of the edges (one-by-one) and decides on the shortest.
 * </p>
 *
 * @author Pieter De Graef
 */
public class NearestAlgorithm extends SnappingAlgorithm {

	/**
	 * The maximum snapping distance, as defined in a snapping rules.
	 */
	private double ruleDistance;

	/**
	 * A list of coordinate arrays. It contains coordinate arrays from either LineStrings or LinearRings or point. The
	 * point is that only between coordinates in the same array can edges exist.
	 */
	private List<Coordinate[]> coordinates;

	//-------------------------------------------------------------------------
	// Constructor:
	//-------------------------------------------------------------------------

	public NearestAlgorithm(List<Geometry> geometries, double ruleDistance) {
		super(geometries);
		this.ruleDistance = ruleDistance;
		coordinates = getCoordinateArrays(geometries);
	}

	//-------------------------------------------------------------------------
	// SnappingAlgorithm implementation:
	//-------------------------------------------------------------------------

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
	Coordinate getSnappingPoint(Coordinate original, double threshold) {
		minimumDistance = Double.MAX_VALUE;
		Coordinate snappingPoint = null;
		double currThreshold = threshold;

		for (Coordinate[] coordinateArray : coordinates) {
			for (int j = 1; j < coordinateArray.length; j++) {
				LineSegment line = new LineSegment(coordinateArray[j], coordinateArray[j - 1]);
				double distance = line.distance(original);
				if (distance < currThreshold && distance < ruleDistance) {
					currThreshold = distance;
					minimumDistance = distance;
					snappingPoint = line.nearest(original);
				}
			}
		}

		return snappingPoint;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Transform the list of geometries into a list of coordinate arrays.
	 */
	private List<Coordinate[]> getCoordinateArrays(List<Geometry> geometries) {
		List<Coordinate[]> res = new ArrayList<Coordinate[]>();
		for (Geometry geometry : geometries) {
			res.add(geometry.getCoordinates());
		}
		return res;
	}
}
