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
package org.geomajas.configuration.validation;

import org.geomajas.global.Api;

/**
 * The constrained attribute must have a size between the specified boundaries (included).
 *
 * @author Jan De Moerloose
 */
@Api(allMethods = true)
public class SizeConstraintInfo implements ConstraintInfo {

	private int min;
	private int max;

	/**
	 * Return the minimum size.
	 *
	 * @return the minimum allowed size
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Set the minimum size.
	 *
	 * @param min minimum allowed size
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Return the maximum size.
	 *
	 * @return the maximum allowed size
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum size.
	 *
	 * @param max maximum allowed size
	 */
	public void setMax(int max) {
		this.max = max;
	}
}
