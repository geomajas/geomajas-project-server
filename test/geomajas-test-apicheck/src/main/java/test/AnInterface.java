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

package test;

import org.geomajas.global.Api;

/**
 * Interface example.
 *
 * @author Joachim Van der Auwera
 */
@Api
public interface AnInterface {

	String BLA = "bla";

	String ALB = "alb";

	/**
	 * Get a string.
	 *
	 * @return a string
	 * @since 1.2.4 
	 */
	@Api
	String getString();

	double getDouble();
}
