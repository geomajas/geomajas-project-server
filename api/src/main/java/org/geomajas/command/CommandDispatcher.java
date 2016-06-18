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
package org.geomajas.command;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * The <code>CommandDispatcher</code> is the main command execution center. It accepts command from the client and
 * executes them on the server.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
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
	@Api
	CommandResponse execute(String commandName, CommandRequest commandRequest, String userToken, String locale);

	/**
	 * Get the current command count value. Can be used for testing the number of executed commands.
	 *
	 * @return current command count value
	 */
	long getCommandCount();

}
