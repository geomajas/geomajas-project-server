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

package org.geomajas.global;

import org.geomajas.geometry.Bbox;

import javax.validation.constraints.NotNull;

/**
 * Description object for configuring the transformable area for CRS conversions.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class CrsTransformInfo {

	@NotNull
	private String source;

	@NotNull
	private String target;

	private Bbox transformableArea;

	/**
	 * Get source CRS code.
	 *
	 * @return crs code
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Set source CRS code.
	 *
	 * @param source source CRS code
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Get target CRS code.
	 *
	 * @return target CRS code
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Set target CRS code.
	 *
	 * @param target target CRS code
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Get bbox for transformable area.
	 *
	 * @return transformable area
	 */
	public Bbox getTransformableArea() {
		return transformableArea;
	}

	/**
	 * Set the bbox with the transformable area.
	 *
	 * @param transformableArea transformable area
	 */
	public void setTransformableArea(Bbox transformableArea) {
		this.transformableArea = transformableArea;
	}
}
