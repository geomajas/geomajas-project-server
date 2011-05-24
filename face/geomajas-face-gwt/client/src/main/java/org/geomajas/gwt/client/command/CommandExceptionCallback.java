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
package org.geomajas.gwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.Api;


/**
 * Execution function that can be passed on to the CommandDispatcher to be executed when a command returns errors.
 * 
 * @since 1.9.0
 * @author Oliver May
 *
 */
@Api(allMethods = true)
public interface CommandExceptionCallback {

	void onCommandException(CommandResponse response);
}
