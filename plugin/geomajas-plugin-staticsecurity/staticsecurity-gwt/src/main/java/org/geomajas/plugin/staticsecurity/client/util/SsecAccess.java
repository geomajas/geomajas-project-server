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

package org.geomajas.plugin.staticsecurity.client.util;

import com.smartgwt.client.util.BooleanCallback;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;

/**
 * <p>
 * Singleton that takes care of logging in and out. Stores a user token that it gets from the server when a login
 * attempt was successful.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public final class SsecAccess {

	private static String userToken;

	private SsecAccess() {
		// static class, cannot be instantiated
	}

	/**
	 * Execute a login attempt, given a user name and password. If a user has already logged in, a logout will be called
	 * first.
	 * 
	 * @param userId
	 *            The unique user ID.
	 * @param password
	 *            The user's password.
	 * @param callback
	 *            A possible callback to be executed when the login has been done (successfully or not). Can be null.
	 */
	public static void login(final String userId, final String password, final BooleanCallback callback) {
		if (userId == null) {
			loginUser(userId, password, callback);
		} else {
			GwtCommand command = new GwtCommand(LogoutRequest.COMMAND);
			GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

				public void execute(CommandResponse response) {
					if (response instanceof SuccessCommandResponse) {
						SuccessCommandResponse successResponse = (SuccessCommandResponse) response;
						if (successResponse.isSuccess()) {
							userToken = null;
							//manager.fireEvent(new LogoutSuccessEvent());
							loginUser(userId, password, callback);
						} else {
							//manager.fireEvent(new LogoutFailureEvent());
						}
					}
				}
			});
		}
	}

	/**
	 * Logs the user out.
	 * 
	 * @param callback
	 *            A possible callback to be executed when the logout has been done (successfully or not). Can be null.
	 */
	public static void logout() {
		GwtCommand command = new GwtCommand(LogoutRequest.COMMAND);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response instanceof SuccessCommandResponse) {
					SuccessCommandResponse successResponse = (SuccessCommandResponse) response;
					if (successResponse.isSuccess()) {
						userToken = null;
						GwtCommandDispatcher.getInstance().setUserToken(null);
						//manager.fireEvent(new LogoutSuccessEvent());
					} else {
						//manager.fireEvent(new LogoutFailureEvent());
					}
				}
			}
		});
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/* Effectively log in a certain user. */
	private static void loginUser(final String userId, final String password, final BooleanCallback callback) {
		LoginRequest request = new LoginRequest();
		request.setLogin(userId);
		request.setPassword(password);
		GwtCommand command = new GwtCommand(LoginRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response instanceof LoginResponse) {
					LoginResponse loginResponse = (LoginResponse) response;
					if (loginResponse.getToken() == null) {
						if (callback != null) {
							callback.execute(false);
						}
						//manager.fireEvent(new LoginFailureEvent(loginResponse.getErrorMessages()));
					} else {
						userToken = loginResponse.getToken();
						GwtCommandDispatcher.getInstance().setUserToken(userToken);
						if (callback != null) {
							callback.execute(true);
						}
						//manager.fireEvent(new LoginSuccessEvent(userToken));
					}
				}
			}
		});
	}
}