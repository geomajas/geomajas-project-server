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
package org.geomajas.configuration.validation;

import org.geomajas.annotation.Api;

/**
 * The value of the constrained number attribute (integer type) must be lower or equal to the specified maximum.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class MaxConstraintInfo implements ConstraintInfo {

	private static final long serialVersionUID = 190L;

	private long value;

	/**
	 * Return the maximum value.
	 *
	 * @return the maximum allowed value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * Set the maximum value.
	 * 
	 * @param  value maximum allowed value
	 */
	public void setValue(long value) {
		this.value = value;
	}
}
