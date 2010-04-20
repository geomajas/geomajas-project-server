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
package org.geomajas.configuration.client;

import org.geomajas.global.Api;

/**
 * Client side raster layer metadata.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientRasterLayerInfo extends ClientLayerInfo {

	private String style = "";

	/**
	 * Returns the opacity of the raster layer.
	 * 
	 * @return the opacity of the raster layer.
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Sets the opacity of the raster layer.
	 * 
	 * @param style the opacity as a string (eg '0.5')
	 */
	public void setStyle(String style) {
		this.style = style;
	}

}
