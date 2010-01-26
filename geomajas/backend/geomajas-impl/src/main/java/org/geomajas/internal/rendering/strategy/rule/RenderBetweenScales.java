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

package org.geomajas.internal.rendering.strategy.rule;

import org.geomajas.rendering.strategy.rule.RenderingStrategyRule;
import org.geomajas.rendering.tile.TileMetadata;

/**
 * <p>
 * <code>RenderingStrategyRule</code> that accepts only circumstances with a scale between certain levels.
 * </p>
 *
 * @author Pieter De Graef
 */
public class RenderBetweenScales implements RenderingStrategyRule {

	/**
	 * Required parameter indicating the minimum allowed scale.
	 */
	public static final String MINIMUM_SCALE = "minScale";

	/**
	 * Required parameter indicating the maximum allowed scale.
	 */
	public static final String MAXIMUM_SCALE = "maxScale";

	/**
	 * Holds the value for the minimum allowed scale.
	 */
	private double minScale = Double.MIN_VALUE;

	/**
	 * Holds the value for the maximum allowed scale.
	 */
	private double maxScale = Double.MAX_VALUE;

	/**
	 * Default constructor. Does nothing.
	 */
	public RenderBetweenScales() {
	}

	/**
	 * Takes the scale from the command, and then tests to see if it is between the minimum and maximum allowed scale
	 * value. If so, returns true, otherwise false.
	 */
	public boolean accept(TileMetadata metadata) {
		double scale = metadata.getScale();
		return scale < maxScale && scale > minScale;
	}

	/**
	 * Requires 2 parameters: "minScale" and "maxScale".
	 */
	/*
	public void setParameters(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			if (parameter.getDataSourceName().equalsIgnoreCase(MINIMUM_SCALE)) {
				minScale = Double.parseDouble(parameter.getValue());
			} else if (parameter.getDataSourceName().equalsIgnoreCase(MAXIMUM_SCALE)) {
				maxScale = Double.parseDouble(parameter.getValue());
			}
		}
	}
	*/
}