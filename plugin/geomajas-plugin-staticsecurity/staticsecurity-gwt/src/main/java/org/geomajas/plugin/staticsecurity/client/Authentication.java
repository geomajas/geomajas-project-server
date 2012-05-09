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

package org.geomajas.plugin.staticsecurity.client;

import org.geomajas.command.SuccessCommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.UserDetail;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.staticsecurity.client.event.LogoutSuccessEvent;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.util.BooleanCallback;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;

/**
 * <p>
 * Singleton that takes care of logging in and out. Stores a user token that it gets from the server when a login
 * attempt was successful.
 * </p>
 * 
 * @author Pieter De Graef
 * @deprecated use {@link org.geomajas.plugin.staticsecurity.client.util.SsecAccess}
 */
@Deprecated
public final class Authentication {

	private static Authentication login = new Authentication();

	private final HandlerManager manager;

	private String userToken;

	private String userId;

	private String loginCommandName = LoginRequest.COMMAND;

	private String logoutCommandName = LogoutRequest.COMMAND;

	private Authentication() {
		manager = new HandlerManager(this);
	}

	/**
	 * @return Return the singleton instance that manages logging in and out.
	 */
	public static Authentication getInstance() {
		return login;
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
			GwtCommand command = new GwtCommand(logoutCommandName);
			GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SuccessCommandResponse>() {

				public void execute(SuccessCommandResponse response) {
					if (response.isSuccess()) {
						userToken = null;
						Authentication.this.userId = null;
						manager.fireEvent(new LogoutSuccessEvent());
						loginUser(userId, password, callback);
					} else {
						manager.fireEvent(new LogoutFailureEvent());
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
		GwtCommand command = new GwtCommand(logoutCommandName);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<SuccessCommandResponse>() {

			public void execute(SuccessCommandResponse response) {
				if (response.isSuccess()) {
					userToken = null;
					Authentication.this.userId = null;
					if (callback != null) {
						callback.execute(true);
					}
					GwtCommandDispatcher.getInstance().logout();
					manager.fireEvent(new LogoutSuccessEvent());
				} else {
					if (callback != null) {
						callback.execute(false);
					}
					manager.fireEvent(new LogoutFailureEvent());
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

	/**
	 * @return Get the Spring bean name of the command to use for logging in.
	 */
	public String getLoginCommandName() {
		return loginCommandName;
	}

	/**
	 * Determine which server-side command to use for logging in.
	 * 
	 * @param loginCommandName
	 *            The Spring bean name of the command.
	 */
	public void setLoginCommandName(String loginCommandName) {
		this.loginCommandName = loginCommandName;
	}

	/**
	 * @return Get the Spring bean name of the command to use for logging out.
	 */
	public String getLogoutCommandName() {
		return logoutCommandName;
	}

	/**
	 * Determine which server-side command to use for logging out.
	 * 
	 * @param logoutCommandName
	 *            The Spring bean name of the command.
	 */
	public void setLogoutCommandName(String logoutCommandName) {
		this.logoutCommandName = logoutCommandName;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/** Effectively log in a certain user. */
	private void loginUser(final String userId, final String password, final BooleanCallback callback) {
		LoginRequest request = new LoginRequest();
		request.setLogin(userId);
		request.setPassword(password);
		GwtCommand command = new GwtCommand(loginCommandName);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<LoginResponse>() {

			public void execute(LoginResponse loginResponse) {
				if (loginResponse.getToken() == null) {
					if (callback != null) {
						callback.execute(false);
					}
					manager.fireEvent(new LoginFailureEvent(loginResponse.getErrorMessages()));
				} else {
					userToken = loginResponse.getToken();
					Authentication.this.userId = userId;
					UserDetail userDetail = new UserDetail();
					userDetail.setUserId(loginResponse.getUserId());
					userDetail.setUserName(loginResponse.getUserName());
					userDetail.setUserOrganization(loginResponse.getUserOrganization());
					userDetail.setUserDivision(loginResponse.getUserDivision());
					userDetail.setUserLocale(loginResponse.getUserLocale());
					GwtCommandDispatcher.getInstance().setUserToken(userToken, userDetail);
					if (callback != null) {
						callback.execute(true);
					}
					manager.fireEvent(new LoginSuccessEvent(userToken));
				}
			}
		});
	}
}