package org.geomajas.internal.security;

import org.geomajas.command.CommandHasRequest;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;

/**
 * Command that allows to run a test inside its scope.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SecurityTestCommand implements CommandHasRequest {

	private Runnable test;

	@Override
	public CommandRequest getEmptyCommandRequest() {
		// empty interface anonymous instantiation
		return new CommandRequest() {};
	}

	@Override
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}

	@Override
	public void execute(CommandRequest request, CommandResponse response) throws Exception {
		if (test != null) {
			test.run();
		}
	}

	public void setTest(Runnable test) {
		this.test = test;
	}
}
