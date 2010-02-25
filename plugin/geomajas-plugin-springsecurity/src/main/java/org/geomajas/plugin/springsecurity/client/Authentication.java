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

package org.geomajas.plugin.springsecurity.client;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.springsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.springsecurity.client.event.LoginHandler;
import org.geomajas.plugin.springsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.springsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.springsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.springsecurity.client.event.LogoutSuccessEvent;
import org.geomajas.plugin.springsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.springsecurity.command.dto.LoginResponse;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.util.BooleanCallback;

/**
 * <p>
 * Singleton that takes care of logging in and out. Stores a user token that it gets from the server when a login
 * attempt was successful.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class Authentication {

	private static Authentication LOGIN;

	private HandlerManager manager;

	private String userToken;

	private String userId;

	private Authentication() {
		manager = new HandlerManager(this);
	}

	/**
	 * @return Return the singleton instance that manages logging in and out.
	 */
	public static Authentication getInstance() {
		if (LOGIN == null) {
			LOGIN = new Authentication();
		}
		return LOGIN;
	}

	/**
	 * Add a login handler that reacts on successful and failed login attempts.
	 * 
	 * @param handler
	 *            The login handler
	 * @return Returns the handler registration.
	 */
	public HandlerRegistration addLoginHandler(LoginHandler handler) {
		return manager.addHandler(LoginHandler.TYPE, handler);
	}

	/**
	 * Add a logout handler that reacts on a successful logout.
	 * 
	 * @param handler
	 *            The logout handler
	 * @return Returns the handler registration.
	 */
	public HandlerRegistration addLogoutHandler(LogoutHandler handler) {
		return manager.addHandler(LogoutHandler.TYPE, handler);
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
	public void login(final String userId, final String password, final BooleanCallback callback) {
		if (this.userId == null) {
			loginUser(userId, password, callback);
		} else if (this.userId.equals(userId)) {
			// Already logged in...
			return;
		} else {
			GwtCommand command = new GwtCommand("command.Logout");
			GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

				public void execute(CommandResponse response) {
					if (response instanceof SuccessCommandResponse) {
						SuccessCommandResponse successResponse = (SuccessCommandResponse) response;
						if (successResponse.isSuccess()) {
							userToken = null;
							Authentication.this.userId = null;
							manager.fireEvent(new LogoutSuccessEvent());
							loginUser(userId, password, callback);
						} else {
							manager.fireEvent(new LogoutFailureEvent());
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
	public void logout(final BooleanCallback callback) {
		GwtCommand command = new GwtCommand("command.Logout");
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response instanceof SuccessCommandResponse) {
					SuccessCommandResponse successResponse = (SuccessCommandResponse) response;
					if (successResponse.isSuccess()) {
						userToken = null;
						Authentication.this.userId = null;
						if (callback != null) {
							callback.execute(true);
						}
						manager.fireEvent(new LogoutSuccessEvent());
					} else {
						if (callback != null) {
							callback.execute(false);
						}
						manager.fireEvent(new LogoutFailureEvent());
					}
				}
			}
		});
	}

	/**
	 * @return Get the user token. Only after a successful login attempt will this token contain a value.
	 */
	public String getUserToken() {
		return userToken;
	}

	/**
	 * @return Get the unique user id. Only after a successful login attempt will this token contain a value.
	 */
	public String getUserId() {
		return userId;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/** Effectively log in a certain user. */
	private void loginUser(final String userId, final String password, final BooleanCallback callback) {
		LoginRequest request = new LoginRequest();
		request.setLogin(userId);
		request.setPassword(password);
		GwtCommand command = new GwtCommand("command.Login");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response instanceof LoginResponse) {
					LoginResponse loginResponse = (LoginResponse) response;
					if (loginResponse.getToken() == null) {
						if (callback != null) {
							callback.execute(false);
						}
						manager.fireEvent(new LoginFailureEvent(loginResponse.getErrorMessages()));
					} else {
						userToken = loginResponse.getToken();
						Authentication.this.userId = userId;
						GwtCommandDispatcher.getInstance().setUserToken(userToken);
						if (callback != null) {
							callback.execute(true);
						}
						manager.fireEvent(new LoginSuccessEvent(userToken));
					}
				}
			}
		});
	}

}
