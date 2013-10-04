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
package org.geomajas.gwt2.client.service;

import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt2.client.widget.exception.ExceptionCallbackImpl;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Default implementation of {@link CommandService}.
 * 
 * @author Jan De Moerloose
 * @author Emiel Ackermann
 * @author Jan Venstermans
 */
public class CommandServiceImpl implements CommandService {
	
	/** No-arguments constructor. */
	public CommandServiceImpl() {
		ExceptionCallbackImpl callback = new ExceptionCallbackImpl();
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(callback);
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(callback);
	}

	@Override
	public Deferred execute(GwtCommand command, CommandCallback<?>... callback) {
		return GwtCommandDispatcher.getInstance().execute(command, callback);
	}

	@Override
	public void login() {
		GwtCommandDispatcher.getInstance().login();
	}

	@Override
	public void logout() {
		GwtCommandDispatcher.getInstance().logout();
	}

	@Override
	public String getUserToken() {
		return GwtCommandDispatcher.getInstance().getUserToken();
	}

	@Override
	public HandlerRegistration addTokenChangedHandler(TokenChangedHandler handler) {
		return GwtCommandDispatcher.getInstance().addTokenChangedHandler(handler);
	}

	@Override
	public void setTokenRequestHandler(TokenRequestHandler tokenRequestHandler) {
		GwtCommandDispatcher.getInstance().setTokenRequestHandler(tokenRequestHandler);		
	}
}