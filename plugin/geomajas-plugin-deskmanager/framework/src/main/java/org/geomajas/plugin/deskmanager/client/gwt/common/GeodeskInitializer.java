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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskResponse;

/**
 * Loads a geodesk from the server, this command will get all geodesk information from the server, and check access for
 * the current user. Typically this is the first command to be executed when starting a deskmanager application. The
 * load command provides a callback where you may trigger custom geodesk implementations, handled by
 * {@link GeodeskInitializationHandler}.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public class GeodeskInitializer {

	private List<GeodeskInitializationHandler> handlers = new ArrayList<GeodeskInitializationHandler>();

	/**
	 * Load a geodesk. This will execute the InitializationCommand, and ask to select a valid user if needed.
	 * 
	 * @param geodeskId
	 *            the geodesk to load
	 * @param tokenRequestHandler
	 *            the login handler.
	 */
	public void loadApplication(String geodeskId, TokenRequestHandler tokenRequestHandler) {
		GwtCommandDispatcher.getInstance().setTokenRequestHandler(tokenRequestHandler);

		GwtCommand initializeGeodeskCommand = new GwtCommand(InitializeGeodeskResponse.COMMAND);
		final AbstractCommandCallback<InitializeGeodeskResponse> openLoketCallback = 
			new AbstractCommandCallback<InitializeGeodeskResponse>() {

			public void execute(InitializeGeodeskResponse response) {
				fireGeodeskInitialized(response);
			}
		};

		InitializeGeodeskRequest request = new InitializeGeodeskRequest();
		request.setGeodeskId(geodeskId);
		initializeGeodeskCommand.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(initializeGeodeskCommand, openLoketCallback);
	}

	/**
	 * Add a {@link GeodeskInitializationHandler}.
	 * 
	 * @param handler
	 *            the handler to add.
	 */
	public void addHandler(GeodeskInitializationHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Remove a {@link GeodeskInitializationHandler}.
	 * 
	 * @param handler
	 *            the handler to remove.
	 */
	public void removeHandler(GeodeskInitializationHandler handler) {
		handlers.remove(handler);
	}

	private void fireGeodeskInitialized(InitializeGeodeskResponse response) {
		for (GeodeskInitializationHandler handler : handlers) {
			handler.initialized(response);
		}
	}

}
