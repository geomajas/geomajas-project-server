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

import org.geomajas.annotation.Api;

import java.io.Serializable;

/**
 * DTO object that holds additional information needed to perform a union operation.
 * 
 * @author Jan De Moerloose
 * @since 1.11.0
 * 
 */
@Api(allMethods = true)
public class UnionInfo implements Serializable {

	private static final long serialVersionUID = 1110L;

	private int precision = -1;

	private boolean usePrecisionAsBuffer;

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
	 * Check if the precision is being used as buffer.
	 * 
	 * @return usePrecisionAsBuffer true if precision is being used as buffer
	 */
	public boolean isUsePrecisionAsBuffer() {
		return usePrecisionAsBuffer;
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
	 * Get the string representation of this info object.
	 * 
	 * @return string representation of this info object
	 */
	@Override
	public String toString() {
		return "UnionInfo{" + "precision=" + precision + ", usePrecisionAsBuffer=" + usePrecisionAsBuffer + '}';
	}
}
