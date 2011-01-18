package org.geomajas.internal.security;

import org.geomajas.command.Command;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;

/**
 * Command that allows to run a test inside its scope.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SecurityTestCommand implements Command {

	private Runnable test;

	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}

	public void execute(CommandRequest request, CommandResponse response) throws Exception {
		if (test != null) {
			test.run();
		}
	}

	public void setTest(Runnable test) {
		this.test = test;
	}

}
