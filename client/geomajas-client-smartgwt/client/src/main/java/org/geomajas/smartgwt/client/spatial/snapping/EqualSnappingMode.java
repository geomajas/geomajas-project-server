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
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.MultiLineString;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPoint;
import org.geomajas.smartgwt.client.spatial.geometry.MultiPolygon;

/**
 * <p>
 * Implementation of the <code>SnappingHandler</code> that treats all geometries equally. All geometries have an equal
 * chance of providing the snapped point.
 * </p>
 * <p>
 * When the {@link Snapper} uses <code>mode = SnapMode.ALL_GEOMETRIES_EQUAL</code> then this implementation of the
 * <code>SnappingHandler</code> is used.
 * </p>
 *
 * @author Pieter De Graef
 */
public class EqualSnappingMode extends SnappingMode {

	public EqualSnappingMode(SnappingRuleInfo rule) {
		super(rule);
	}

	public void execute(List<Feature> features) {
		List<Geometry> geometries = new ArrayList<Geometry>();

		for (Feature feature : features) {
			Geometry geometry = feature.getGeometry();
			// For MultiPolygons and MultiLinestrings, we calculate bounds intersection
			// for each partial geometry. This way we can send parts of the complex
			// geometries to the snapping list, and not always the entire geometry.(=faster)
			if (geometry instanceof MultiLineString || geometry instanceof MultiPoint
					|| geometry instanceof MultiPolygon) {
				for (int n = 0; n < geometry.getNumGeometries(); n++) {
					Geometry geometryN = geometry.getGeometryN(n);
					if (geometryN.getBounds().intersects(bounds)) {
						geometries.add(geometryN);
					}
				}
			} else {
				if (geometry.getBounds().intersects(bounds)) {
					geometries.add(geometry);
				}
			}
		}

		if (!geometries.isEmpty()) {
			SnappingAlgorithm algorithm;
			if (rule.getType() == SnappingType.CLOSEST_ENDPOINT) {
				algorithm = new ClosestPointAlgorithm(geometries, rule.getDistance());
			} else {
				algorithm = new NearestAlgorithm(geometries, rule.getDistance());
			}
			Coordinate snapPointIfFound = algorithm.getSnappingPoint(coordinate, distance);
			if (snapPointIfFound != null) {
				snappedCoordinate = snapPointIfFound;
				distance = algorithm.getMinimumDistance();
			}
		}
	}
}
