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
package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.EmptyCommandRequest;

/**
 * Command name (for consistency with other commands). You don't have to use this, just pass null as request object.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public class CopyrightRequest extends EmptyCommandRequest {

	private static final long serialVersionUID = 190L;
	
	/**
	 * Command name for this request.
	 */
	public static final String COMMAND = "command.general.Copyright";


}
