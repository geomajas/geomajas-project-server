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

package org.geomajas.plugin.staticsecurity.client;

import org.geomajas.global.Api;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.staticsecurity.client.event.LogoutSuccessEvent;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * <p>
 * Simple button that logs the user out. To connect to the correct events, add a {@link LogoutHandler} by giving it to
 * this widget's constructor. It will add the handler to the <code>Authentication</code> final class.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.7.1
 */
@Api
public class LogoutButton extends IButton implements ClickHandler, LoginHandler, LogoutHandler {

	private StaticSecurityMessages i18n;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor. Adds handlers to the login and logout events that set the disabled status of this button. It
	 * makes sure that this logout button is only enabled when the user has been successfully logged in.
	 *
	 * @since 1.7.1
	 */
	@Api
	public LogoutButton() {
		super();
		i18n = GWT.create(StaticSecurityMessages.class);
		setTitle(i18n.logoutBtnTitle());
		setIcon("[ISOMORPHIC]/geomajas/staticsecurity/key_delete.png");
		setDisabled(true);
		addClickHandler(this);
		Authentication.getInstance().addLoginHandler(this);
		Authentication.getInstance().addLogoutHandler(this);
	}

	/**
	 * Calls the default constructor, and adds an extra logout handler to the logout events.
	 * 
	 * @param logoutHandler
	 *            Add this logout handler to the logout events.
	 * @since 1.7.1
	 */
	@Api
	public LogoutButton(LogoutHandler logoutHandler) {
		this();
		Authentication.getInstance().addLogoutHandler(logoutHandler);
	}

	// -------------------------------------------------------------------------
	// ClickHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * Tries to log the user out. Uses the <code>Authentication.logout()</code> method.
	 */
	public void onClick(ClickEvent event) {
		Authentication.getInstance().logout(null);
	}

	// -------------------------------------------------------------------------
	// LoginHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * Does nothing...
	 */
	public void onLoginFailure(LoginFailureEvent event) {
		// Do nothing...
	}

	/**
	 * A successful login: enables this button.
	 */
	public void onLoginSuccess(LoginSuccessEvent event) {
		setDisabled(false);
	}

	// -------------------------------------------------------------------------
	// LogoutHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * Does nothing...
	 */
	public void onLogoutFailure(LogoutFailureEvent event) {
		// Do nothing...
	}

	/**
	 * A successful logout: disables this button.
	 */
	public void onLogoutSuccess(LogoutSuccessEvent event) {
		setDisabled(true);
	}
}
