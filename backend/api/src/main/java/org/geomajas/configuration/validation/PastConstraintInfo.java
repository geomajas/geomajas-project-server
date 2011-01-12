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
package org.geomajas.configuration.validation;

import org.geomajas.global.Api;

/**
 * The constrained date attribute value must be a date in the past. Now is defined as the current time according to the
 * virtual machine The calendar used if the compared type is of type Calendar  is the calendar based on the current
 * timezone and the current locale.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class PastConstraintInfo implements ConstraintInfo {

}
