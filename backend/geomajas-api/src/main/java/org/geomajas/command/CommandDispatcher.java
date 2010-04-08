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

import java.io.Serializable;

/**
 * The <code>CommandDispatcher</code> is the main command execution center. It accepts command from the client and
 * executes them on the server.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public interface CommandDispatcher extends Serializable {

	/**
	 * General command execution function.
	 *
	 * @param commandName name of command to execute
	 * @param commandRequest {@link CommandRequest} object for the command (if any)
	 * @param userToken token to identify user
	 * @param locale which should be used for the error messages in the response
	 * @return {@link CommandResponse} command response
	 */
	CommandResponse execute(String commandName, CommandRequest commandRequest, String userToken, String locale);
}
