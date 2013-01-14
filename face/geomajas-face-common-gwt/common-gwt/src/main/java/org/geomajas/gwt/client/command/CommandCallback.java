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
import org.geomajas.annotation.UserImplemented;
import org.geomajas.command.CommandResponse;

/**
 * Execution function that can be passed on to the CommandDispatcher to be executed when a command successfully returns.
 * <p />
 * It is recommended to extend {@link AbstractCommandCallback} instead of implementing this.
 *
 * @param <RESPONSE> type of response object for the command
 * @since 0.0.0
 * @author Pieter De Graef
 */
@Api(allMethods = true)
@UserImplemented
public interface CommandCallback<RESPONSE extends CommandResponse> {

	/**
	 * The actual execution function. If the command returns successfully, this will be executed.
	 *
	 * @param response command response
	 */
	void execute(RESPONSE response);
}
