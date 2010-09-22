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
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.global.Api;

/**
 * DTO object for ViewPortComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.ViewPortComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class ViewPortComponentInfo extends MapComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/**
	 * Ratio of the view port scale and the map scale
	 */
	private float zoomScale;

	/**
	 * X coordinate of view port in user coordinates
	 */
	private float userX = -1;

	/**
	 * Y coordinate of view port in user coordinates
	 */
	private float userY = -1;

	public float getZoomScale() {
		return zoomScale;
	}

	public void setZoomScale(float zoomScale) {
		this.zoomScale = zoomScale;
	}

	public float getUserX() {
		return userX;
	}

	public void setUserX(float userX) {
		this.userX = userX;
	}

	public float getUserY() {
		return userY;
	}

	public void setUserY(float userY) {
		this.userY = userY;
	}

}
