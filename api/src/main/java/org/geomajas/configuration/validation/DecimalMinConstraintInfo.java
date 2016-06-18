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

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * The value of the constrained number attribute must be higher or equal to the specified minimum.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class DecimalMinConstraintInfo implements ConstraintInfo {

	private static final long serialVersionUID = 190L;

	@NotNull
	private String value;

	/**
	 * Return the minimum value according to BigDecimal string representation.
	 *
	 * @return the minimum allowed value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the minimum value according to BigDecimal string representation.
	 *
	 * @param  value minimum allowed value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
