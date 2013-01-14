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
package org.geomajas.command;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Command definition.
 *
 * @param <REQUEST> type of request object, see {@link CommandRequest}
 * @param <RESPONSE> type of response object, see {@link CommandResponse}
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface Command<REQUEST extends CommandRequest, RESPONSE extends CommandResponse> {

	/**
	 * Get an empty response object which will be filled when executing the command and partially by the dispatcher.
	 * @return response object, extends {@link CommandResponse}
	 */
	RESPONSE getEmptyCommandResponse();

	/**
	 * Execute the command.
	 *
	 * @param request request parameters
	 * @param response response object
	 * @throws Exception in case of problems
	 */
	void execute(REQUEST request, RESPONSE response) throws Exception;
}
