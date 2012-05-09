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

package org.geomajas.application.gwt.showcase.client.security;

import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.plugin.staticsecurity.client.util.SsecAccess;

/**
 * {@link TokenRequestHandler} which allows logging in to a specific userId and password.
 *
 * @author Joachim Van der Auwera
 */
public class ShowcaseTokenRequestHandler implements TokenRequestHandler {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** UserId to use for logging in. */
	public static String userId;
	/** Password to use for logging in. */
	public static String password;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	/** {@inheritDoc} */
	public void login(TokenChangedHandler tokenChangedHandler) {
		SsecAccess.login(userId, password, tokenChangedHandler);
	}
}
