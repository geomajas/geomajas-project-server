/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
	
	/**
	 * Optional buffer to extend the merging result.
	 * 
	 * @param buffer
	 */
	public void setUsePrecisionAsBuffer(boolean usePrecisionAsBuffer) {
		this.usePrecisionAsBuffer = usePrecisionAsBuffer;
	}
	
	public boolean usePrecisionAsBuffer() {
		return usePrecisionAsBuffer;
	}
}