/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.command.dto;

import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.annotation.Api;

/**
 * Request object for {@link org.geomajas.plugin.staticsecurity.command.staticsecurity.LogoutCommand}.
 * Provided for consistency with other commands. You don't have to use this, just pass null as request object.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public class LogoutRequest extends EmptyCommandRequest {

	private static final long serialVersionUID = 190L;

	/**
	 * Command name for this request.
	 */
	public static final String COMMAND = "command.staticsecurity.Logout";

}
