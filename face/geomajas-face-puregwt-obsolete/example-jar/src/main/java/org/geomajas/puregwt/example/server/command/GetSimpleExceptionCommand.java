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

package org.geomajas.puregwt.example.server.command;

import org.geomajas.command.Command;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Command that generates an exception of class Exception, with message
 * "Server-side generated exception.".
 * </p>
 * 
 * @author Pieter De Graef / Jan Venstermans
 */
@Component
public class GetSimpleExceptionCommand implements
		Command<EmptyCommandRequest, CommandResponse> {

	public void execute(EmptyCommandRequest request, CommandResponse response)
			throws Exception {
		throw new Exception("Server-side generated exception.");
	}

	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
