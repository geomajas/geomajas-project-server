/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
