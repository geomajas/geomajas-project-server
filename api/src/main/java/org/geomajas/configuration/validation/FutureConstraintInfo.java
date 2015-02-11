/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.validation;

import org.geomajas.annotation.Api;
import org.geomajas.global.Json;

/**
 * The constrained date attribute value must be a date in the future. Now is defined as the current time according to
 * the virtual machine The calendar used if the compared type is of type Calendar  is the calendar based on the current
 * timezone and the current locale.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FutureConstraintInfo implements ConstraintInfo {

	private static final long serialVersionUID = 190L;

	/**
	 * Dummy method, provided to assure Checkstyle doesn't complain.
	 *
	 * @return 0
	 * @since 1.9.0
	 */
	@Json(serialize = false)
	public int getDummy() {
		return 0;
	}

}
