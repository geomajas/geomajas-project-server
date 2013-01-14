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

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.MathService;
import org.geomajas.plugin.editing.client.snap.SnapAlgorithm;

/**
 * <p>
 * Snapping algorithm that snaps to both end-points and any point on an edge of intersecting geometries. Only geometries
 * with closed rings that actually intersect the given coordinate are considered for snapping. All other geometries are
 * discarded.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class NearestEdgeOfIntersection implements SnapAlgorithm {

	private Geometry[] geometries;

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

		// Go over all rings and consider only the intersecting ones:
		for (Geometry geometry : geometries) {
			if (MathService.isWithin(geometry, coordinate)) {
				NearestEdgeSnapAlgorithm nearestEdge = new NearestEdgeSnapAlgorithm();
				nearestEdge.setGeometries(new Geometry[] { geometry });
				Coordinate temp = nearestEdge.snap(coordinate, calculatedDistance);
				if (nearestEdge.hasSnapped()) {
					hasSnapped = true;
					calculatedDistance = nearestEdge.getCalculatedDistance();
					snappingPoint = temp;
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
		this.geometries = geometries;
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
}