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

package org.geomajas.gwt.client.command;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;

/**
 * Execution function that can be passed on to the {@link GwtCommandDispatcher} to be executed when a command response
 * includes errors.
 * 
 * @author Oliver May
 * @since 0.0.0
 */
@Api(allMethods = true)
public interface CommandExceptionCallback {

	/**
	 * Called when the CommandResponse includes exception details.
	 *
	 * @param response command response
	 */
	void onCommandException(CommandResponse response);
}
