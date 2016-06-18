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
 * The value of the constrained number attribute must have a maximum number of integer and/or fractional digits.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class DigitsConstraintInfo implements ConstraintInfo {

	private static final long serialVersionUID = 190L;

	private int integer;

	private int fractional;

	/**
	 * Return the maximum number of integer digits.
	 * 
	 * @return the maximum allowed number of integer digits
	 */
	public int getInteger() {
		return integer;
	}

	/**
	 * Set the maximum number of integer digits.
	 * 
	 * @param integer
	 *            the maximum allowed number of integer digits
	 */
	public void setInteger(int integer) {
		this.integer = integer;
	}

	/**
	 * Return the maximum number of fractional digits.
	 * 
	 * @return the maximum allowed number of fractional digits
	 */
	public int getFractional() {
		return fractional;
	}

	/**
	 * Set the maximum number of fractional digits.
	 * 
	 * @param fractional
	 *            the maximum allowed number of fractional digits
	 */
	public void setFractional(int fractional) {
		this.fractional = fractional;
	}
}
