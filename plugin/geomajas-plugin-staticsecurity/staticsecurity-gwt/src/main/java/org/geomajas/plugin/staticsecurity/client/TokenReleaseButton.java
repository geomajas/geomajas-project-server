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

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.plugin.staticsecurity.client.util.SsecAccess;
import org.geomajas.plugin.staticsecurity.client.util.SsecLayout;

/**
 * Simple button that logs the user out. This connects to {@link TokenChangedEvent}s which are emitted by the
 * {@link GwtCommandDispatcher}.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api
public class TokenReleaseButton extends IButton implements ClickHandler, TokenChangedHandler {

	private static final StaticSecurityMessages MESSAGES = GWT.create(StaticSecurityMessages.class);

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor. Adds handlers to the login and logout events that set the disabled status of this button. It
	 * makes sure that this logout button is only enabled when the user has been successfully logged in.
	 *
	 * @since 1.9.0
	 */
	@Api
	public TokenReleaseButton() {
		super();
		setTitle(MESSAGES.tokenReleaseButtonTitle());
		setIcon(SsecLayout.iconLogout);
		setDisabled(true);
		addClickHandler(this);
		GwtCommandDispatcher.getInstance().addTokenChangedHandler(this);
	}

	// -------------------------------------------------------------------------
	// ClickHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * Tries to log the user out. Uses the {@link SsecAccess#logout()} method.
	 */
	public void onClick(ClickEvent event) {
		GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();
		SsecAccess.logout();
		dispatcher.logout();
	}

	// -------------------------------------------------------------------------
	// TokenChangedHandler implementation:
	// -------------------------------------------------------------------------

	public void onTokenChanged(TokenChangedEvent event) {
		setDisabled(null == event.getToken());
	}
}
