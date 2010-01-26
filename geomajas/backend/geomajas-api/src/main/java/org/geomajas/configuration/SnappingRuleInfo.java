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

	private SnappingType type;

	private String layer;

	public SnappingRuleInfo() {
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public SnappingType getType() {
		return type;
	}

	public void setType(SnappingType type) {
		this.type = type;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}
}
