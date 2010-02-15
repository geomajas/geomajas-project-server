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

package org.geomajas.gwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.GeomajasService;
import org.geomajas.gwt.client.GeomajasServiceAsync;
import org.geomajas.gwt.client.command.event.DispatchStartedEvent;
import org.geomajas.gwt.client.command.event.DispatchStartedHandler;
import org.geomajas.gwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.gwt.client.command.event.DispatchStoppedHandler;
import org.geomajas.gwt.client.command.event.HasDispatchHandlers;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.smartgwt.client.util.SC;

/**
 * The central client side dispatcher for all commands. Use the {@link #execute(GwtCommand, CommandCallback)} function
 * to execute an asynchronous command on the server.
 * 
 * @author Pieter De Graef
 */
public final class GwtCommandDispatcher implements HasDispatchHandlers {

	private static GwtCommandDispatcher INSTANCE;

	private GeomajasServiceAsync service;

	private HandlerManager manager = new HandlerManager(this);

	private int nrOfDispatchedCommands;

	private String locale;

	private String userToken;

	private GwtCommandDispatcher() {
		locale = LocaleInfo.getCurrentLocale().getLocaleName();
		service = (GeomajasServiceAsync) GWT.create(GeomajasService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "geomajasService";
		endpoint.setServiceEntryPoint(moduleRelativeURL);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/** Get the only static instance of this class. This should be the object you work with. */
	public static GwtCommandDispatcher getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GwtCommandDispatcher();
		}
		return INSTANCE;
	}

	public HandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler) {
		return manager.addHandler(DispatchStartedEvent.getType(), handler);
	}

	public HandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler) {
		return manager.addHandler(DispatchStoppedEvent.getType(), handler);
	}

	/**
	 * The execution function. Executes a server side command.
	 * 
	 * @param command
	 *            The command to be executed. This command is a wrapper around the actual request object.
	 * @param onSuccess
	 *            A <code>CommandCallback</code> function to be executed when the command successfully returns.
	 */
	public void execute(GwtCommand command, final CommandCallback onSuccess) {
		incrementDispatched();
		command.setLocale(locale);
		command.setUserToken(userToken);
		service.execute(command, new AsyncCallback<CommandResponse>() {

			public void onFailure(Throwable error) {
				SC.warn(I18nProvider.getGlobal().commandError() + ":\n" + error.getMessage(), null);
				decrementDispatched();
			}

			public void onSuccess(CommandResponse response) {
				if (response.isError()) {
					String message = I18nProvider.getGlobal().commandError() + ":";
					for (String error : response.getErrorMessages()) {
						message += "\n" + error;
					}
					SC.warn(message, null);
				} else {
					onSuccess.execute(response);
				}
				decrementDispatched();
			}
		});
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
