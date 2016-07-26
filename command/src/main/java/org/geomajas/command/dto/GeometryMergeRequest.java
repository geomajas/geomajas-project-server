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

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.GeometryMergeCommand}.
 * 
 * @author Pieter De Graef
 * @since 1.11.0
 */
@Api(allMethods = true)
public class GeometryMergeRequest implements CommandRequest {

	private static final long serialVersionUID = 1110L;

	/** Command name for this request. */
	public static final String COMMAND = "command.geometry.Merge";

	private List<Geometry> geometries;

	private int precision;

	private boolean usePrecisionAsBuffer;

	/**
	 * Get the geometries  for the merge command.
	 * 
	 * @return geometries for the merge command
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Set the geometries  for the merge command.
	 * 
	 * @param geometries for the merge command
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Get optional precision to use when merging geometries.
	 * 
	 * @return precision of the merging
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Set optional precision to use when merging geometries.
	 * 
	 * @param precision of the merging
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * Optional buffer to extend the merging result.
	 * 
	 * @param usePrecisionAsBuffer true if precision is being used as buffer
	 */
	public void setUsePrecisionAsBuffer(boolean usePrecisionAsBuffer) {
		this.usePrecisionAsBuffer = usePrecisionAsBuffer;
	}

	/**
	 * Check if the precision is being used as buffer.
	 * 
	 * @return usePrecisionAsBuffer true if precision is being used as buffer
	 */
	public boolean usePrecisionAsBuffer() {
		return usePrecisionAsBuffer;
	}

	/**
	 * Get the string representation of this request.
	 * 
	 * @return string representation of this request
	 */
	@Override
	public String toString() {
		return "GeometryMergeRequest{" + "geometries=" + geometries + ", precision=" + precision
				+ ", usePrecisionAsBuffer=" + usePrecisionAsBuffer + '}';
	}
}