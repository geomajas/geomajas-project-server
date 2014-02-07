/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Snapping rule configuration information.
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SnappingRuleInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	/**
	 * Snapping type.
	 */
	public static enum SnappingType {
		/** Closest endpoint. */
		CLOSEST_ENDPOINT,
		/** Nearest point on the geometry. */
		NEAREST_POINT
	}

	private double distance;

	@NotNull
	private SnappingType type;

	@NotNull
	private String layerId;

	/**
	 * Create snapping rule.
	 */
	public SnappingRuleInfo() {
	}

	/**
	 * Get distance, the size of the raster which needs to be snapped to.
	 *
	 * @return snapping distance or granularity
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Set the snapping distance of granularity.
	 *
	 * @param distance snapping distance or granularity
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Get snapping type.
	 *
	 * @return snapping type
	 */
	public SnappingType getType() {
		return type;
	}

	/**
	 * Set the snapping type.
	 *
	 * @param type snapping type
	 */
	public void setType(SnappingType type) {
		this.type = type;
	}

	/**
	 * Get the id of the layer this snapping info applies to.
	 *
	 * @return layer id
	 */
	public String getLayerId() {
		return layerId;
	}

	/**
	 * Set the id of the layer this snapping info applies to.
	 *
	 * @param layerId layer id
	 */
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
}
