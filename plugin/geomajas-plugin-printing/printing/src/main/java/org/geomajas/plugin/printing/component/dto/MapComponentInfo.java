/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.annotation.Api;

/**
 * DTO object for MapComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.MapComponent
 * @since 2.0.0
 */
@Api(allMethods = true)
public class MapComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/** Application id. */
	private String applicationId;

	/** Map id. */
	private String mapId;

	/** The lower left corner in map units. */
	private Coordinate location;

	/**
	 * Resolution to be used for raster layers (unit = DPI, default = 72, which corresponds to 1 pixel per unit of user
	 * space in PDF).
	 */
	private double rasterResolution = 72;

	/** The number of points (user space units) per map unit. */
	private float ppUnit = 1.0f;

	/** No-arguments constructor. */
	public MapComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.JUSTIFIED);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.JUSTIFIED);
	}

	/**
	 * Get application id.
	 *
	 * @return application id
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Set application id.
	 *
	 * @param applicationId application id
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Get map id.
	 *
	 * @return map id
	 */
	public String getMapId() {
		return mapId;
	}

	/**
	 * Set map id.
	 *
	 * @param mapId map id
	 */
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	/**
	 * Get the component location. This is the lower left corner in map units.
	 *
	 * @return component location
	 */
	public Coordinate getLocation() {
		return location;
	}

	/**
	 * Set the component location. This is the lower left corner in map units.
	 *
	 * @param location component location
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}

	/**
	 * Get the resolution to be used for raster layers (unit = DPI, default = 72, which corresponds to 1 pixel per
	 * unit of user space in PDF).
	 *
	 * @return raster resolution
	 */
	public double getRasterResolution() {
		return rasterResolution;
	}

	/**
	 * Set the resolution to be used for raster layers (unit = DPI, default = 72, which corresponds to 1 pixel per
	 * unit of user space in PDF).
	 *
	 * @param rasterResolution raster resolution
	 */
	public void setRasterResolution(double rasterResolution) {
		this.rasterResolution = rasterResolution;
	}

	/**
	 * Get the number of points (user space units) per map unit.
	 *
	 * @return number of points per map unit
	 */
	public float getPpUnit() {
		return ppUnit;
	}

	/**
	 * Set the number of points (user space units) per map unit.
	 *
	 * @param ppUnit number of points per map unit
	 */
	public void setPpUnit(float ppUnit) {
		this.ppUnit = ppUnit;
	}

}
