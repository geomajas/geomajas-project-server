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
package org.geomajas.command.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

/**
 * Result object for {@link org.geomajas.command.geometry.GeometryBufferCommand}.
 * 
 * @author Emiel Ackermann
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryBufferResponse extends CommandResponse {

	private static final long serialVersionUID = 1110L;

	private List<Geometry> geometries;

	/**
	 * Get buffered geometries.
	 *
	 * @return geometries that were buffered
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Set buffered geometries.
	 *
	 * @param geometries that were buffered
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	@Override
	public String toString() {
		return "GeometryBufferResponse{" +
				"geometries=" + geometries +
				'}';
	}
}