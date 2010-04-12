/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.command;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;

/**
 * Command definition.
 *
 * @param <REQUEST> type of request object, see {@link CommandRequest}
 * @param <RESPONSE> type of response object, see {@link CommandResponse}
 *
 * @author Joachim Van der Auwera  
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
