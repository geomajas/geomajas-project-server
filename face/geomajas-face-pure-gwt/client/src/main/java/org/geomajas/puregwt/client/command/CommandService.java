/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.FutureApi;
import org.geomajas.puregwt.client.GeomajasService;
import org.geomajas.puregwt.client.GeomajasServiceAsync;
import org.geomajas.puregwt.client.command.event.DispatchStartedEvent;
import org.geomajas.puregwt.client.command.event.DispatchStartedHandler;
import org.geomajas.puregwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.puregwt.client.command.event.DispatchStoppedHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Central service for executing commands. These commands are sent to the server for execution. This command pattern
 * uses the GWT RPC system as protocol.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class CommandService {

	private GeomajasServiceAsync service;

	private HandlerManager manager = new HandlerManager(this);

	private int nrOfDispatchedCommands;

	private String locale;

	private String userToken;

	// -------------------------------------------------------------------------
	// Initialization:
	// -------------------------------------------------------------------------

	private void initialize() {
		// The reason why this initialization is not in the constructor is because unit tests fail if GWT.create is used
		// in a constructor...so here we are.
		if (service == null) {
			try {
				locale = LocaleInfo.getCurrentLocale().getLocaleName();
				if ("default".equals(locale)) {
					locale = null;
				}
			} catch (NullPointerException e) {
				locale = null;
			}
			service = (GeomajasServiceAsync) GWT.create(GeomajasService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) service;
			String moduleRelativeURL = GWT.getModuleBaseURL() + "geomajasService";
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Add a handler for catching events that signal the start of a command.
	 * 
	 * @param handler
	 *            The handler object.
	 * @return Returns the registration for the handler.
	 */
	public HandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler) {
		return manager.addHandler(DispatchStartedEvent.getType(), handler);
	}

	/**
	 * Add a handler for catching events that signal the return of the response for a command.
	 * 
	 * @param handler
	 *            The handler object.
	 * @return Returns the registration for the handler.
	 */
	public HandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler) {
		return manager.addHandler(DispatchStoppedEvent.getType(), handler);
	}

	/**
	 * The execution function. Executes a server side command.
	 * 
	 * @param command
	 *            The command to be executed. This command is a wrapper around the actual request object.
	 * @param callbacks
	 *            A <code>CommandCallback</code> function to be executed when the command successfully returns.
	 * @return deferred object which can be used to add extra call-backs
	 */
	public Deferred execute(Command command, final CommandCallback... callbacks) {
		if (service == null) {
			initialize();
		}
		incrementDispatched();

		final Deferred deferred = new Deferred();
		for (CommandCallback callback : callbacks) {
			deferred.addCallback(callback);
		}

		command.setLocale(locale);
		command.setUserToken(userToken);
		service.execute(command, new AsyncCallback<CommandResponse>() {

			public void onFailure(Throwable error) {
				try {
					for (CommandCallback callback : deferred.getCallbacks()) {
						callback.onFailure(error);
					}
					GWT.log(error.getMessage());
				} catch (Throwable t) {
					GWT.log("Command failed on error callback", t);
				} finally {
					decrementDispatched();
				}
			}

			public void onSuccess(CommandResponse response) {
				try {
					if (response.isError()) {
						String message = "";
						for (String error : response.getErrorMessages()) {
							message += error + "\n";
						}
						GWT.log(message, null);
						// TODO do something with the error message besides simply logging...
					} else {
						if (!deferred.isCancelled()) {
							for (CommandCallback callback : deferred.getCallbacks()) {
								callback.onSuccess(response);
							}
						}
					}
				} catch (Throwable t) {
					GWT.log("Command failed on success callback", t);
				} finally {
					decrementDispatched();
				}
			}
		});
		return deferred;
	}

	/**
	 * Is the dispatcher busy ?
	 * 
	 * @return true if there are outstanding commands
	 */
	public boolean isBusy() {
		return nrOfDispatchedCommands != 0;
	}

	/**
	 * Set the user token, so it can be sent in very command.
	 * 
	 * @param userToken
	 *            user token
	 */
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	protected void incrementDispatched() {
		boolean started = nrOfDispatchedCommands == 0;
		nrOfDispatchedCommands++;
		if (started) {
			manager.fireEvent(new DispatchStartedEvent());
		}
	}

	protected void decrementDispatched() {
		nrOfDispatchedCommands--;
		if (nrOfDispatchedCommands == 0) {
			manager.fireEvent(new DispatchStoppedEvent());
		}
	}
}