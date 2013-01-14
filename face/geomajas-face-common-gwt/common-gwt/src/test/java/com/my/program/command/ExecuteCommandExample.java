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

package com.my.program.command;

import com.my.program.command.dto.MySuperDoItRequest;
import com.my.program.command.dto.MySuperDoItResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * Execute command example, for inclusion in documentation.
 *
 * @author Pieter De Graef
 */
public class ExecuteCommandExample {

	public void executeCommand() {
		// @extract-start GwtCommandExecution, Example use of executing a command.
		MySuperDoItRequest commandRequest = new MySuperDoItRequest();
		// .... add parameters to the request.

		// Create the command, with the correct Spring bean name:
		GwtCommand command = new GwtCommand(MySuperDoItRequest.COMMAND);
		command.setCommandRequest(commandRequest);

		// Execute the command, and do something with the response:
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<MySuperDoItResponse>() {

			public void execute(MySuperDoItResponse response) {
				// Command returned successfully. Do something with the result.
			}
		});
		// @extract-end
	}
}
