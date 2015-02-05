/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.GeometryAreaCommand}.
 * 
 * @author Jan De Moerloose
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryAreaRequest implements CommandRequest {

	private static final long serialVersionUID = 1110L;

	/** Command name for this request. **/
	public static final String COMMAND = "command.geometry.Area";

	private List<Geometry> geometries;
	
	private String crs;

	/**
	 * Get geometries.
	 * 
	 * @return geometries to calculate area with
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Set geometries.
	 * 
	 * @param geometries to calculate area with
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}
	
	/**
	 * Get the crs.
	 * 
	 * @return crs of the area to be calculated
	 */
	public String getCrs() {
		return crs;
	}
	
	/**
	 * Set the crs.
	 * @param crs of the area to be calculated
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the string representation of this request.
	 * @return string representation of this request
	 */
	@Override
	public String toString() {
		return "GeometryAreaRequest{" +
				"geometries=" + geometries +
				", crs='" + crs + '\'' +
				'}';
	}
}