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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.LineSegment;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;

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
