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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Scale configuration of the map. The map needs a maximum scale (minimum is determined by maximum bounds) and
 * optionally a set of zoom levels.
 * 
 * @author Jan De Moerloose
 * @since 1.7.0
 */
@Api(allMethods = true)
public class ScaleConfigurationInfo implements Serializable {

	private static final long serialVersionUID = 170L;

	private ScaleInfo maximumScale = ScaleInfo.MAX_VALUE;

	private List<ScaleInfo> zoomLevels = new ArrayList<ScaleInfo>();

	private ScaleUnit scaleUnit = ScaleUnit.NORMAL;

	/**
	 * Returns the maximum scale of the map.
	 * 
	 * @return the maximum scale of the map
	 */
	public ScaleInfo getMaximumScale() {
		return maximumScale;
	}

	/**
	 * Sets the maximum scale of the map.
	 * 
	 * @param maximumScale
	 *            the maximum scale of the map
	 */
	public void setMaximumScale(ScaleInfo maximumScale) {
		this.maximumScale = maximumScale;
	}

	/**
	 * Returns a list of predefined zoom levels allowed by the map.
	 * 
	 * @return list of levels (scales)
	 */
	public List<ScaleInfo> getZoomLevels() {
		return zoomLevels;
	}

	/**
	 * Sets a list of predefined zoom levels allowed by the map.
	 * 
	 * @param zoomLevels
	 *            list of levels (scales)
	 */
	public void setZoomLevels(List<ScaleInfo> zoomLevels) {
		this.zoomLevels = zoomLevels;
	}

	/**
	 * Returns the unit used by the zoom levels and maximum scale.
	 * 
	 * @return a scale unit
	 */
	public ScaleUnit getScaleUnit() {
		return scaleUnit;
	}

	/**
	 * Sets the unit used by the zoom levels and maximum scale.
	 * 
	 * @param scaleUnit
	 *            a scale unit
	 */
	public void setScaleUnit(ScaleUnit scaleUnit) {
		this.scaleUnit = scaleUnit;
	}

}
