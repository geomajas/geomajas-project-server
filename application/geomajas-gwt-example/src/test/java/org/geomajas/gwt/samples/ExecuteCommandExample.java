package org.geomajas.gwt.samples;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

import com.my.program.command.dto.MySuperDoItRequest;

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
