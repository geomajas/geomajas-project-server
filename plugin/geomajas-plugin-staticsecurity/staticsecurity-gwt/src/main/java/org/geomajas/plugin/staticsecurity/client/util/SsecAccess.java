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
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.UserDetail;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.staticsecurity.command.dto.LogoutRequest;

/**
 * Singleton that takes care of logging in and out.
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api
public final class SsecAccess {

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
		if (userId != null) {
			logout();
		}
		loginOnly(userId, password, callback);
	}

	/**
	 * Logs the user out.
	 */
	public static void logout() {
		GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();
		if (null != dispatcher.getUserToken()) {
			GwtCommand command = new GwtCommand(LogoutRequest.COMMAND);
			dispatcher.setUserToken(null);
			dispatcher.execute(command);
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/* Effectively log in a certain user. */
	private static void loginOnly(final String userId, final String password, final BooleanCallback callback) {
		LoginRequest request = new LoginRequest();
		request.setLogin(userId);
		request.setPassword(password);
		GwtCommand command = new GwtCommand(LoginRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<LoginResponse>() {

			public void execute(LoginResponse response) {
				UserDetail userDetail = new UserDetail();
				userDetail.setUserId(response.getUserId());
				userDetail.setUserName(response.getUserName());
				userDetail.setUserOrganization(response.getUserOrganization());
				userDetail.setUserDivision(response.getUserDivision());
				userDetail.setUserLocale(response.getUserLocale());
				GwtCommandDispatcher.getInstance().setUserToken(response.getToken(), userDetail);
				if (callback != null) {
					callback.execute(null != response.getToken());
				}
			}
		});
	}
}