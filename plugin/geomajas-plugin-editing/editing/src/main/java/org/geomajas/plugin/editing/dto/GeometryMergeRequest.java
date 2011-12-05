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
package org.geomajas.plugin.editing.dto;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.MergePolygonCommand}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class GeometryMergeRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	/** Command name for this request. **/
	public static final String COMMAND = "command.GeometryMerge";

	private List<Geometry> geometries;

	private int precision;

	public List<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	public int getPrecision() {
		return precision;
	}

	/**
	 * Optional precision to use when merging geometries.
	 * 
	 * @param precision
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}
}