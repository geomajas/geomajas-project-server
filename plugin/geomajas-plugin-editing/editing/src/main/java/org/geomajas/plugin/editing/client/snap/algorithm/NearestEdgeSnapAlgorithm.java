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
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.MathService;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;

/**
 * <p>
 * Snapping algorithm that not only snaps to end-points of nearby geometries, but to any point on any of the edges as
 * well. As you might guess, this is a heavier calculation then the {@link NearestVertexSnapAlgorithm}. It tries to
 * calculate distances between the given coordinate, and any of the edges (one-by-one) and decides on the shortest.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class NearestEdgeSnapAlgorithm implements SnapAlgorithm {

	/**
	 * A list of coordinate arrays. It contains coordinate arrays from either LineStrings or LinearRings or point. The
	 * point is that only between coordinates in the same array can edges exist.
	 */
	private List<Coordinate[]> coordinates = new ArrayList<Coordinate[]>();

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

		// Calculate the distances for all coordinate arrays:
		for (Coordinate[] coordinateArray : coordinates) {
			if (coordinateArray.length > 1) {
				for (int j = 1; j < coordinateArray.length; j++) {
					double d = MathService.distance(coordinateArray[j], coordinateArray[j - 1], coordinate);
					if (d < calculatedDistance || (d == calculatedDistance && !hasSnapped)) {
						snappingPoint = MathService.nearest(coordinateArray[j], coordinateArray[j - 1], coordinate);
						calculatedDistance = d;
						hasSnapped = true;
					}
				}
			} else if (coordinateArray.length == 1) {
				// In the case of Points, see if we can snap to them:
				double d = MathService.distance(coordinateArray[0], coordinate);
				if (d < calculatedDistance) {
					snappingPoint = coordinateArray[0];
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
		coordinates.clear();
		for (Geometry geometry : geometries) {
			addCoordinateArrays(geometry, coordinates);
		}
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

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void addCoordinateArrays(Geometry geometry, List<Coordinate[]> coords) {
		if (geometry.getGeometries() != null) {
			for (Geometry child : geometry.getGeometries()) {
				addCoordinateArrays(child, coords);
			}
		} else if (geometry.getCoordinates() != null) {
			coords.add(geometry.getCoordinates());
		}
	}
}