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
package org.geomajas.puregwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.FutureApi;

/**
 * Execution function that can be passed on to the CommandDispatcher to be executed when a command successfully returns.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface CommandCallback {

	/**
	 * The actual execution function. If the command returns successfully, this will be executed.
	 * 
	 * @param response
	 *            The response from the server automatically indicating that the command was executed successfully.
	 */
	void onSuccess(CommandResponse response);

	/**
	 * The actual execution function. If the command returns successfully, this will be executed.
	 * 
	 * @param response
	 *            The response from the server automatically indicating that the command was executed successfully.
	 */
	void onFailure(Throwable error);
}