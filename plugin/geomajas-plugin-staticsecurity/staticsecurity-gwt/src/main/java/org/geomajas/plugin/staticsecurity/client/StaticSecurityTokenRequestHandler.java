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
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;

/**
 * {@link TokenRequestHandler} which uses the static security login window to obtain credentials.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api
public class StaticSecurityTokenRequestHandler implements TokenRequestHandler {

	private String slogan;

	/**
	 * Default constructor, use the standard login window.
	 */
	@Api
	public StaticSecurityTokenRequestHandler() {
	}

	/**
	 * Login handler which should display the given slogan in the login window.
	 *
	 * @param slogan slogan to display in login window
	 */
	@Api
	public StaticSecurityTokenRequestHandler(String slogan) {
		this();
		this.slogan = slogan;
	}

	/** {@inheritDoc} */
	public void login(final TokenChangedHandler tokenChangedHandler) {
		final TokenRequestWindow tokenRequestWindow =
				new TokenRequestWindow(new LoginTokenChangedHandler(tokenChangedHandler));
		tokenRequestWindow.setSlogan(slogan);
		tokenRequestWindow.draw();
	}

	/** Forward token changed. */
	private static final class LoginTokenChangedHandler implements TokenChangedHandler {

		private TokenChangedHandler tokenChangedHandler;

		private LoginTokenChangedHandler(TokenChangedHandler tokenChangedHandler) {
			this.tokenChangedHandler = tokenChangedHandler;
		}

		/** {@inheritDoc} */
		public void onTokenChanged(TokenChangedEvent event) {
			tokenChangedHandler.onTokenChanged(event);
		}

	}
}
