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

import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.CommandExceptionCallback;
import org.geomajas.gwt.client.command.CommunicationExceptionCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.util.Log;

/**
 * Default implementation of {@link CommandService}.
 * 
 * @author Jan De Moerloose
 * 
 */

public class CommandServiceImpl implements CommandService {

	private String authenticationToken;

	/**
	 * Simple callback implementation that writes all exceptions to {@link Log}.
	 * 
	 * @author Emiel Ackermann
	 * 
	 */
	public class CommandExceptionCallbackImpl implements
			CommandExceptionCallback, CommunicationExceptionCallback {

		public void onCommandException(CommandResponse response) {
			for (ExceptionDto error : response.getExceptions()) {
				Log.logError(getDetails(error));
			}
		}
	
		/**
		 * Build details message for an exception.
		 *
		 * @param error error to build message for
		 * @return string with details message
		 */
		private String getDetails(ExceptionDto error) {
			if (null == error) {
				return "";
			}
			StringBuilder content = new StringBuilder();
			String header = error.getClassName();
			if (error.getExceptionCode() != 0) {
				header += " (" + error.getExceptionCode() + ")";
			}
			content.append(header);
			for (StackTraceElement el : error.getStackTrace()) {
				content.append("  " + el.toString() + "\n");
			}
			content.append(getDetails(error.getCause()));
			return content.toString();
		}

		public void onCommunicationException(Throwable error) {
			Log.logError("Communication exception", error);			
		}

	}

	public CommandServiceImpl() {
		CommandExceptionCallbackImpl callback = new CommandExceptionCallbackImpl();
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(callback);
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(callback);
	}

	public Deferred execute(GwtCommand command, CommandCallback... callback) {
		return GwtCommandDispatcher.getInstance().execute(command, callback);
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}
}
