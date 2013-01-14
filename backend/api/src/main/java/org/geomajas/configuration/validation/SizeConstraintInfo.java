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
package org.geomajas.configuration.validation;

import org.geomajas.annotation.Api;

/**
 * The constrained attribute must have a size between the specified boundaries (included).
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SizeConstraintInfo implements ConstraintInfo {

	private static final long serialVersionUID = 190L;

	private int min;
	private int max;

	/**
	 * Return the minimum size.
	 *
	 * @return the minimum allowed size
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Set the minimum size.
	 *
	 * @param min minimum allowed size
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Return the maximum size.
	 *
	 * @return the maximum allowed size
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum size.
	 *
	 * @param max maximum allowed size
	 */
	public void setMax(int max) {
		this.max = max;
	}
}
