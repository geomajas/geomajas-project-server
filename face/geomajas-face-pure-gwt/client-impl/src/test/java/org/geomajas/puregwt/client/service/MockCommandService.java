/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.service;

import java.util.Stack;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.junit.Assert;

import com.smartgwt.client.core.Function;

public class MockCommandService implements CommandService {

	Stack<CommandResponse> responseStack = new Stack<CommandResponse>();

	public Deferred execute(GwtCommand command, CommandCallback... callback) {
		if (responseStack.isEmpty()) {
			Assert.fail("Expected response for " + command.getCommandName());
		}
		for (CommandCallback commandCallback : callback) {
			commandCallback.execute(responseStack.pop());
		}
		return new ForbiddenDeferred();
	}

	public void pushResponse(CommandResponse item) {
		responseStack.push(item);
	}

	public void clear() {
		responseStack.clear();
	}

	public class ForbiddenDeferred extends Deferred {

		@Override
		public void addCallback(CommandCallback callback) {
			Assert.fail("Can't add callback to command service !");
		}

		@Override
		public void addSuccessCallback(CommandCallback callback) {
			Assert.fail("Can't add callback to command service !");
		}

		@Override
		public void addErrorCallback(Function onError) {
			Assert.fail("Can't add callback to command service !");
		}

	}

}
