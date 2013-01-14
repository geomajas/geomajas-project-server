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

package org.geomajas.plugin.editing.client.snap.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.MathService;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;

/**
 * <p>
 * Snapping algorithm that snaps to the closest end-point (vertex) of a geometry. Only coordinates that are effectively
 * stored in the geometries come into account. This makes it a pretty fast algorithm.
 * </p>
 * <p>
 * Also, when applying the list of geometries to snap to, this list is sorted into 2lists of coordinates: one sorted by
 * X-ordinates, one sorted by Y-ordinates. This may take some time initially, but afterwards you'll reap the results, as
 * possible snapping points can quickly be fetched using the binary search algorithm.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class NearestVertexSnapAlgorithm implements SnapAlgorithm {

	/** List of coordinates, all sorted (ascending) by their X-ordinate. */
	private List<Coordinate> sortedX;

	/** List of coordinates, all sorted (ascending) by their Y-ordinate. */
	private List<Coordinate> sortedY;

	private double calculatedDistance;

	private boolean hasSnapped;

	// ------------------------------------------------------------------------
	// SnappingAlgorithm implementation:
	// ------------------------------------------------------------------------

	/**
	 * Execute the snap operation.
	 * 
	 * @param coordinate
	 *            The original location.
	 * @param distance
	 *            The maximum distance allowed for snapping.
	 * @return The new location. If no snapping target was found, this may return the original location.
	 */
	public Coordinate snap(Coordinate coordinate, double distance) {
		// Some initialization:
		calculatedDistance = distance;
		hasSnapped = false;
		Coordinate snappingPoint = coordinate;

		if (sortedX != null && sortedY != null) {
			// Calculate the distances for all candidates:
			List<Coordinate> coordinates = getPossibleCoordinates(coordinate, distance);
			for (Coordinate candidate : coordinates) {
				double d = MathService.distance(coordinate, candidate);
				if (d < calculatedDistance) {
					snappingPoint = candidate;
					calculatedDistance = d;
					hasSnapped = true;
				}
			}
		}

		return snappingPoint;
	}

	/**
	 * Set the full list of target geometries. These are the geometries where to this snapping algorithm can snap.
	 * 
	 * @param geometries
	 *            The list of target geometries.
	 */
	public void setGeometries(Geometry[] geometries) {
		List<Coordinate> coordinates = getCoordinates(geometries);
		sortedX = sortX(coordinates);
		sortedY = sortY(coordinates);
	}

	/**
	 * Get the effective distance that was bridged during the snap operation. In case snapping occurred, this distance
	 * will be smaller than the given "distance" value during the last call to snap.
	 * 
	 * @return The effective snapping distance. Only valid if snapping actually occurred.
	 */
	public double getCalculatedDistance() {
		return calculatedDistance;
	}

	/**
	 * Has snapping actually occurred during the last call to the <code>snap</code> method? If so the returned snap
	 * location was different from the original location.
	 * 
	 * @return Returns if the returned location from the snap method differs from the original location.
	 */
	public boolean hasSnapped() {
		return hasSnapped;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/** Get a single list of coordinates from an array of geometries. */
	private List<Coordinate> getCoordinates(Geometry[] geometries) {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (Geometry geometry : geometries) {
			addCoordinateArrays(geometry, coordinates);
		}
		return coordinates;
	}

	/** Add all coordinates within the geometry to the list. */
	private void addCoordinateArrays(Geometry geometry, List<Coordinate> coordinates) {
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				addCoordinateArrays(child, coordinates);
			}
		} else if (geometry.getCoordinates() != null) {
			for (Coordinate coordinate : geometry.getCoordinates()) {
				coordinates.add(coordinate);
			}
		}
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
	private List<Coordinate> getPossibleCoordinates(Coordinate coordinate, double max) {
		int xMin = Collections.binarySearch(sortedX, new Coordinate(coordinate.getX() - max, 0), new XComparator());
		int xMax = Collections.binarySearch(sortedX, new Coordinate(coordinate.getX() + max, 0), new XComparator());
		int yMin = Collections.binarySearch(sortedY, new Coordinate(0, coordinate.getY() - max), new YComparator());
		int yMax = Collections.binarySearch(sortedY, new Coordinate(0, coordinate.getY() + max), new YComparator());
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