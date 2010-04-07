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

import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.configuration.SnappingRuleInfo.SnappingType;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.layer.LayerType;

/**
 * <p>
 * This is the main handler for snapping to coordinates. It supports different modes of operating and different
 * algorithms for the actual snapping. The different algorithms to use are defined in the vector layer configurations
 * objects, while the modes are defined by the {@link SnappingMode} class.
 * </p>
 * <p>
 * All you have to do to make use of snapping, is to make an instance of this class, and call the <code>snap</code>
 * method.
 * </p>
 *
 * @author Pieter De Graef
 */
public class Snapper {

	/**
	 * General definition for the different snapping modes. The ALL_GEOMETRIES_EQUAL will use a SnappingMode that treats
	 * all geometries equally (@see EqualSnappingMode), while the PRIORITY_TO_INTERSECTING_GEOMETRIES will give priority
	 * to geometries that intersect the given coordinate (@see IntersectPriorityMode).
	 */
	public static enum SnapMode {

		ALL_GEOMETRIES_EQUAL, PRIORITY_TO_INTERSECTING_GEOMETRIES
	}

	;

	/**
	 * The MapModel that contains the layers to which snapping is possible. If a snapping rule should exist that points
	 * to a layer that does not exist within this MapModel, then no snapping will occur.
	 */
	private MapModel mapModel;

	/**
	 * The full list of snapping rules to be used.
	 */
	private List<SnappingRuleInfo> rules;

	/**
	 * The current snapping mode to use.
	 */
	private SnapMode mode;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a snapper with a certain set of rules and the given MapModel. As mode, the SnapMode.ALL_GEOMTRIES_EQUAL is
	 * used by default.
	 */
	public Snapper(MapModel mapModel, List<SnappingRuleInfo> rules) {
		this(mapModel, rules, SnapMode.ALL_GEOMETRIES_EQUAL);
	}

	/**
	 * Create a snapper with a certain set of rules and the given MapModel, thereby immediately setting the snapping
	 * mode to use.
	 */
	public Snapper(MapModel mapModel, List<SnappingRuleInfo> rules, SnapMode mode) {
		this.mapModel = mapModel;
		this.rules = rules;
		setMode(mode);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Execute the actual snapping!
	 *
	 * @param coordinate
	 *            The original coordinate that needs snapping.
	 * @return Returns the given coordinate, or a snapped coordinate if one was found.
	 */
	public Coordinate snap(Coordinate coordinate) {
		if (rules == null || mapModel == null) {
			return coordinate;
		}
		Coordinate snappedCoordinate = coordinate;

		for (int i = 0; i < rules.size(); i++) {
			SnappingRuleInfo rule = rules.get(i);

			// Check for supported snapping algorithms: TODO use factory
			if (rule.getType() != SnappingType.CLOSEST_ENDPOINT && rule.getType() != SnappingType.NEAREST_POINT) {
				throw new IllegalArgumentException("Unknown snapping rule type was found: " + rule.getType());
			}

			// Get the target snap layer:
			VectorLayer snapLayer;
			try {
				snapLayer = mapModel.getVectorLayer(rule.getLayerId());
			} catch (Exception e) {
				throw new IllegalArgumentException("Target snapping layer (" + rule.getLayerId()
						+ ") was not a vector layer.");
			}

			SnapMode tempMode = this.mode;
			if (snapLayer.getLayerInfo().getLayerType() != LayerType.POLYGON
					&& snapLayer.getLayerInfo().getLayerType() != LayerType.MULTIPOLYGON) {
				// For mode=MODE_PRIORITY_TO_INTERSECTING_GEOMETRIES, an area > 0 is required.
				tempMode = SnapMode.ALL_GEOMETRIES_EQUAL;
			}

			// TODO: don't create the handler every time...
			SnappingMode handler;
			if (tempMode == SnapMode.ALL_GEOMETRIES_EQUAL) {
				handler = new EqualSnappingMode(rule);
			} else {
				handler = new IntersectPriorityMode(rule);
			}

			// Calculate snapping:
			handler.setCoordinate(snappedCoordinate);
			snapLayer.getFeatureStore().query(handler.getBounds(), handler);
			snappedCoordinate = handler.getSnappedCoordinate();
		}
		return snappedCoordinate;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public SnapMode getMode() {
		return mode;
	}

	public void setMode(SnapMode mode) {
		this.mode = mode;
	}

	public List<SnappingRuleInfo> getRules() {
		return rules;
	}

	public void setRules(List<SnappingRuleInfo> rules) {
		this.rules = rules;
	}
}
