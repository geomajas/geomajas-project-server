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

package org.geomajas.plugin.staticsecurity.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.client.command.TokenRequestCallback;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;

/**
 * {@link TokenRequestHandler} which uses the static security login window to obtain credentials.
 *
 * @author Joachim Van der Auwera
 */
public class StaticSecurityTokenRequestHandler implements TokenRequestHandler {

	private static final StaticSecurityMessages MESSAGES = GWT.create(StaticSecurityMessages.class);
	private String slogan;

	/** Default constructor, use the standard login window. */
	public StaticSecurityTokenRequestHandler() {
	}

	/**
	 * Login handler which should display the given slogan in the login window.
	 *
	 * @param slogan slogan to display in login window
	 */
	public StaticSecurityTokenRequestHandler(String slogan) {
		this.slogan = slogan;
	}

	/** @{inheritDoc} */
	public void login(final TokenRequestCallback tokenRequestCallback) {
		final TokenRequestWindow tokenRequestWindow = new TokenRequestWindow(new TokenChangedHandler() {
			public void onTokenChanged(TokenChangedEvent event) {
				tokenRequestCallback.onLogin(event.getToken());
			}
		});
		tokenRequestWindow.setSlogan(slogan);
		tokenRequestWindow.show();
	}
}
