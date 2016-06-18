/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

/**
 * Result object for {@link org.geomajas.command.geometry.GeometryMergeCommand}.
 * 
 * @author Pieter De Graef
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryMergeResponse extends CommandResponse {

	private static final long serialVersionUID = 1110L;

	private Geometry geometry;

	/**
	 * Get merged geometries.
	 *
	 * @return merged geometries
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Set merged geometries.
	 *
	 * @param geometry merged geometries
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return "GeometryMergeResponse{" +
				"geometry=" + geometry +
				'}';
	}
}