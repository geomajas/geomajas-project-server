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

package org.geomajas.gwt.samples;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

import com.my.program.command.dto.MySuperDoItRequest;

/**
 * Execute command example, for inclusion in documentation
 *
 * @author Pieter De Graef
 */
public class ExecuteCommandExample {

	public void executeCommand() {
		// @extract-start GwtCommandExecution, Example use of executing a command.
		MySuperDoItRequest commandRequest = new MySuperDoItRequest();
		// .... add parameters to the request.

		// Create the command, with the correct Spring bean name:
		GwtCommand command = new GwtCommand("command.MySuperDoIt");
		command.setCommandRequest(commandRequest);

		// Execute the command, and do something with the response:
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				// Command returned successfully. Do something with the result.
			}
		});
		// @extract-end
	}
}
