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
package org.geomajas.configuration;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Snapping rule configuration information.
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class SnappingRuleInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	/**
	 * Snapping type.
	 */
	public static enum SnappingType {
		CLOSEST_ENDPOINT, NEAREST_POINT
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
