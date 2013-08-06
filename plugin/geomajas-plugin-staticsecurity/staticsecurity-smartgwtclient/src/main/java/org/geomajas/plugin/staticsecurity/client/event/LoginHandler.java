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

package org.geomajas.plugin.staticsecurity.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * <p>
 * Handler for a logging in.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.7.1
 * @deprecated use {@link org.geomajas.gwt.client.command.event.TokenChangedHandler}
 */
@Api(allMethods = true)
@UserImplemented
@Deprecated
public interface LoginHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<LoginHandler> TYPE = new GwtEvent.Type<LoginHandler>();

	/**
	 * Called when a login attempt was successful.
	 * 
	 * @param event
	 *            The login success event. Contains the user token.
	 */
	void onLoginSuccess(LoginSuccessEvent event);

	/**
	 * Called when a login attempt has failed.
	 * 
	 * @param event
	 *            The login failure event. Contains the error message.
	 */
	void onLoginFailure(LoginFailureEvent event);
}
