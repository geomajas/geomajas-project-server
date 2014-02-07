/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Request object for {@link org.geomajas.command.geometry.GeometryConvexHullCommand}.
 * 
 * @author Emiel Ackermann
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryConvexHullRequest implements CommandRequest {

	private static final long serialVersionUID = 1110L;

	/** Command name for this request. **/
	public static final String COMMAND = "command.geometry.ConvexHull";

	private List<Geometry> geometries;

	/**
	 * Get geometries.
	 * 
	 * @return geometries for the convex hull command
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Set geometries.
	 * 
	 * @param geometries for the convex hull command
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Get the string representation of this request.
	 * 
	 * @return string representation of this request
	 */
	@Override
	public String toString() {
		return "GeometryConvexHullRequest{" + "geometries=" + geometries + '}';
	}
}