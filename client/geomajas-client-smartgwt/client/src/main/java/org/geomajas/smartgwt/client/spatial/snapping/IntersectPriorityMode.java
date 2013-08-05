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

import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.configuration.SnappingRuleInfo.SnappingType;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.spatial.Mathlib;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPoint;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;

/**
 * <p>
 * Implementation of the <code>SnappingHandler</code> that gives priority to geometries that intersect the original
 * coordinate that is to be snapped. If no such geometry is found, then it continues the normal way: trying to snap to
 * any geometry.
 * </p>
 * <p>
 * When the {@link Snapper} uses <code>mode = SnapMode.PRIORITY_TO_INTERSECTING_GEOMETRIES</code> then this
 * implementation of the <code>SnappingHandler</code> is used.
 * </p>
 *
 * @author Pieter De Graef
 */
public class IntersectPriorityMode extends SnappingMode {

	/**
	 * The snapping distance found on intersecting geometries only.
	 */
	private double intersectDistance;

	/**
	 * The snapped coordinate found when using intersecting geometries only.
	 */
	private Coordinate intersectSnappedCoordinate;

	//-------------------------------------------------------------------------
	// Constructor:
	//-------------------------------------------------------------------------

	protected IntersectPriorityMode(SnappingRuleInfo rule) {
		super(rule);
	}

	public void execute(List<Feature> features) {
		List<Geometry> geometries = new ArrayList<Geometry>();

		for (Feature feature : features) {
			Geometry geometry = feature.getGeometry();
			// For multipolygons and multilinestrings, we calculate bounds intersection
			// for each partial geometry. This way we can send parts of the complex
			// geometries to the snapping list, and not always the entire geometry.(=faster)
			List<Geometry> currentGeometries = new ArrayList<Geometry>();
			if (geometry instanceof MultiLineString || geometry instanceof MultiPoint
					|| geometry instanceof MultiPolygon) {
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					Geometry geometryN = geometry.getGeometryN(n);
					if (geometryN.getBounds().intersects(bounds)) {
						currentGeometries.add(geometryN);
					}
				}
			} else {
				if (geometry.getBounds().intersects(bounds)) {
					currentGeometries.add(geometry);
				}
			}
			if (currentGeometries.size() != 0) {
				if (Mathlib.isWithin(geometry, coordinate)) {
					SnappingAlgorithm algorithm;
					if (rule.getType() == SnappingType.CLOSEST_ENDPOINT) {
						algorithm = new ClosestPointAlgorithm(currentGeometries, rule.getDistance());
					} else {
						algorithm = new NearestAlgorithm(currentGeometries, rule.getDistance());
					}
					Coordinate snapPointIfFound = algorithm.getSnappingPoint(coordinate, intersectDistance);
					if (snapPointIfFound != null) {
						snappedCoordinate = snapPointIfFound;
						intersectSnappedCoordinate = snappedCoordinate;
						intersectDistance = algorithm.getMinimumDistance();
					}
				} else {
					geometries.addAll(currentGeometries);
				}
			}
		}

		if (intersectSnappedCoordinate == null && !geometries.isEmpty()) {
			SnappingAlgorithm algorithm;
			if (rule.getType() == SnappingType.CLOSEST_ENDPOINT) {
				algorithm = new ClosestPointAlgorithm(geometries, rule.getDistance());
			} else {
				algorithm = new NearestAlgorithm(geometries, rule.getDistance());
			}
			Coordinate snapPointIfFound = algorithm.getSnappingPoint(coordinate, intersectDistance);
			if (snapPointIfFound != null) {
				snappedCoordinate = snapPointIfFound;
				distance = algorithm.getMinimumDistance();
			}
		}
	}

	public void setCoordinate(Coordinate coordinate) {
		super.setCoordinate(coordinate);
		intersectDistance = Double.MAX_VALUE;
		intersectSnappedCoordinate = null;
	}
}
