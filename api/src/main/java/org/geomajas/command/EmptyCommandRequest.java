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

package org.geomajas.command;

import org.geomajas.annotation.Api;
import org.geomajas.global.Json;


/**
 * Placeholder class, useful for commands which don't have any parameters.
 * There is no need to ever instantiate this object. Use "null" instead.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class EmptyCommandRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Dummy method, provided to assure Checkstyle doesn't complain.
	 *
	 * @return 0
	 */
	@Json(serialize = false)
	public int getDummy() {
		return 0; 
	}
}
