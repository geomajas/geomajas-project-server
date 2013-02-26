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
package org.geomajas.plugin.deskmanager.client.gwt.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.common.impl.DeskmanagerTokenRequestHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.ManagerInitializationHandler;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodesksResponse;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

/**
 * Loads geodesk manager from the server, this command will check access for
 * the current user. Typically this is the first command to be executed when starting the manager application. The
 * load command provides a callback where you may trigger custom manager implementations, handled by 
 * {@link ManagerInitializationHandler}.
 * 
 * @author Oliver May
 */
public class ManagerInitializer {

	private List<ManagerInitializationHandler> handlers = new ArrayList<ManagerInitializationHandler>();

	/**
	 * Load the manager application. This will execute the InitializationCommand, and ask to select a valid user if 
	 * needed.
	 * 
	 * @param tokenRequestHandler
	 *            the login handler.
	 */
	public void loadManagerApplication(final DeskmanagerTokenRequestHandler tokenRequestHandler) {
		GwtCommandDispatcher.getInstance().setTokenRequestHandler(tokenRequestHandler);

		//We execute the getgeodeskscommand because that is only accessible by the manager, we ignore the response.
		GwtCommand command = new GwtCommand(GetGeodesksRequest.COMMAND);

		final AbstractCommandCallback<GetGeodesksResponse> callback = 
			new AbstractCommandCallback<GetGeodesksResponse>() {
			public void execute(GetGeodesksResponse response) {
				fireGeodeskInitialized(tokenRequestHandler.getProfile());
			}
		};
		GwtCommandDispatcher.getInstance().execute(command, callback);
	}

	/**
	 * Add a {@link ManagerInitializationHandler}.
	 * 
	 * @param handler
	 *            the handler to add.
	 */
	public void addHandler(ManagerInitializationHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Remove a {@link ManagerInitializationHandler}.
	 * 
	 * @param handler
	 *            the handler to remove.
	 */
	public void removeHandler(ManagerInitializationHandler handler) {
		handlers.remove(handler);
	}

	private void fireGeodeskInitialized(ProfileDto profile) {
		for (ManagerInitializationHandler handler : handlers) {
			handler.initialized(profile);
		}
	}

}
