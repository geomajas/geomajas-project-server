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

import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;

/**
 * DTO object for MapComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.MapComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class MapComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/**
	 * Map id
	 */
	private String applicationId;

	/**
	 * Map id
	 */
	private String mapId;

	/**
	 * The lower left corner in map units
	 */
	private Coordinate location;

	/**
	 * resolution to be used for raster layers (unit = DPI, default = 72, which corresponds to 1 pixel per unit of user
	 * space in PDF)
	 */
	private double rasterResolution = 72;

	/**
	 * The number of points (user space units) per map unit
	 */
	private float ppUnit = 1.0f;

	public MapComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.JUSTIFIED);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.JUSTIFIED);
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public double getRasterResolution() {
		return rasterResolution;
	}

	public void setRasterResolution(double rasterResolution) {
		this.rasterResolution = rasterResolution;
	}

	public float getPpUnit() {
		return ppUnit;
	}

	public void setPpUnit(float ppUnit) {
		this.ppUnit = ppUnit;
	}

}
