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
import org.geomajas.command.CommandResponse;

/**
 * Result object for {@link org.geomajas.command.geometry.GeometryAreaCommand}.
 * 
 * @author Jan De Moerloose
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryAreaResponse extends CommandResponse {

	private static final long serialVersionUID = 1110L;

	private List<Double> areas;

	/**
	 * Get areas.
	 * @return areas calculated areas
	 */
	public List<Double> getAreas() {
		return areas;
	}
	
	/**
	 * Set areas.
	 * @param areas calculated areas
	 */
	public void setAreas(List<Double> areas) {
		this.areas = areas;
	}

	/**
	 * Get the string representation of this response.
	 * @return string representation of this response
	 */
	@Override
	public String toString() {
		return "GeometryAreaResponse{" +
				"areas=" + areas +
				'}';
	}
}