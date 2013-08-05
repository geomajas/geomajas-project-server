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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.Mathlib;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;

/**
 * <p>
 * Snapping algorithm that snaps to the closest end-point of a geometry. Only coordinates that are effectively stored in
 * the geometries come into account. This makes it a pretty fast algorithm.
 * </p>
 * <p>
 * Also at construction this class will turn the list of geometries into 2 sorted lists of coordinates: one sorted by
 * X-ordinates, one sorted by Y-ordinates. This may take some time initially, but afterwards you'll reap the results, as
 * possible snapping points can quickly be fetched using the binary search algorithm.
 * </p>
 *
 * @author Pieter De Graef
 */
public class ClosestPointAlgorithm extends SnappingAlgorithm {

	/**
	 * The maximum snapping distance, as defined in a snapping rules.
	 */
	private double ruleDistance;

	/**
	 * List of coordinates, all sorted (ascending) by their X-ordinate.
	 */
	private List<Coordinate> sortedX;

	/**
	 * List of coordinates, all sorted (ascending) by their Y-ordinate.
	 */
	private List<Coordinate> sortedY;

	//-------------------------------------------------------------------------
	// Constructor:
	//-------------------------------------------------------------------------

	public ClosestPointAlgorithm(List<Geometry> geometries, double ruleDistance) {
		super(geometries);
		this.ruleDistance = ruleDistance;

		List<Coordinate> coordinates = getCoordinates(geometries);
		sortedX = sortX(coordinates);
		sortedY = sortY(coordinates);
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

		List<Coordinate> coordinates = getPossibleCoordinates(original);
		for (Coordinate coordinate : coordinates) {
			double distance = Mathlib.distance(original, coordinate);
			if (distance < currThreshold && distance < ruleDistance) {
				currThreshold = distance;
				minimumDistance = distance;
				snappingPoint = coordinate;
			}
		}

		return snappingPoint;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Get a single list of coordinates from a list of geometries.
	 */
	private List<Coordinate> getCoordinates(List<Geometry> geometries) {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (Geometry geometry : geometries) {
			Coordinate[] geometryCoordinates = geometry.getCoordinates();
			for (int i = 0; i < geometryCoordinates.length; i++) {
				coordinates.add(geometryCoordinates[i]);
			}
		}
		return coordinates;
	}

	/**
	 * Return a new and sorted list of coordinates. They should be sorted by their X values.
	 */
	private List<Coordinate> sortX(List<Coordinate> coordinates) {
		List<Coordinate> sorted = new ArrayList<Coordinate>(coordinates);
		Collections.sort(sorted, new XComparator());
		return sorted;
	}

	/**
	 * Return a new and sorted list of coordinates. They should be sorted by their Y values.
	 */
	private List<Coordinate> sortY(List<Coordinate> coordinates) {
		List<Coordinate> sorted = new ArrayList<Coordinate>(coordinates);
		Collections.sort(sorted, new YComparator());
		return sorted;
	}

	/**
	 * Return a possible list of coordinates that are within range of the given coordinate. This function is very fast
	 * as it uses binary search, and it returns a small subset of coordinates. The perfect set to start calculating
	 * from.
	 */
	private List<Coordinate> getPossibleCoordinates(Coordinate coordinate) {
		int xMin = Collections.binarySearch(sortedX, new Coordinate(coordinate.getX() - ruleDistance, 0),
				new XComparator());
		int xMax = Collections.binarySearch(sortedX, new Coordinate(coordinate.getX() + ruleDistance, 0),
				new XComparator());
		int yMin = Collections.binarySearch(sortedY, new Coordinate(0, coordinate.getY() - ruleDistance),
				new YComparator());
		int yMax = Collections.binarySearch(sortedY, new Coordinate(0, coordinate.getY() + ruleDistance),
				new YComparator());
		if (xMin < 0) {
			xMin = Math.abs(xMin) - 1;
		}
		if (xMax < 0) {
			xMax = Math.abs(xMax) - 1;
		}
		if (yMin < 0) {
			yMin = Math.abs(yMin) - 1;
		}
		if (yMax < 0) {
			yMax = Math.abs(yMax) - 1;
		}

		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (int i = xMin; i < xMax; i++) {
			coordinates.add(sortedX.get(i));
		}
		for (int i = yMin; i < yMax; i++) {
			coordinates.add(sortedY.get(i));
		}

		return coordinates;
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * Private class that compares a coordinate's X values.
	 *
	 * @author Pieter De Graef
	 */
	private class XComparator implements Comparator<Coordinate> {

		public int compare(Coordinate c1, Coordinate c2) {
			if (c1.getX() < c2.getX()) {
				return -1;
			}
			if (c1.getX() > c2.getX()) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Private class that compares a coordinate's Y values.
	 *
	 * @author Pieter De Graef
	 */
	private class YComparator implements Comparator<Coordinate> {

		public int compare(Coordinate c1, Coordinate c2) {
			if (c1.getY() < c2.getY()) {
				return -1;
			}
			if (c1.getY() > c2.getY()) {
				return 1;
			}
			return 0;
		}
	}
}
