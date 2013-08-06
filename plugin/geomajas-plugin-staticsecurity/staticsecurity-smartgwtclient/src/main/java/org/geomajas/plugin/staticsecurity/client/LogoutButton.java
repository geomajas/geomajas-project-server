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

package org.geomajas.plugin.staticsecurity.client;

import org.geomajas.annotation.Api;
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
 * @deprecated use {@link TokenReleaseButton}
 */
@Api
@Deprecated
public class LogoutButton extends IButton implements ClickHandler, LoginHandler, LogoutHandler {

	private static final StaticSecurityMessages I18N = GWT.create(StaticSecurityMessages.class);

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
		setTitle(I18N.logoutBtnTitle());
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
