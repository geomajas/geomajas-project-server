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

package org.geomajas.plugin.staticsecurity.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * <p>
 * Handler for logging out events.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.7.1
 * @deprecated use {@link org.geomajas.gwt.client.command.event.TokenChangedHandler}
 */
@Api(allMethods = true)
@UserImplemented
@Deprecated
public interface LogoutHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<LogoutHandler> TYPE = new GwtEvent.Type<LogoutHandler>();

	/**
	 * Called when the user has successfully logged out.
	 * 
	 * @param event
	 *            The logout success event.
	 */
	void onLogoutSuccess(LogoutSuccessEvent event);

	/**
	 * Called when the user's attempt to log out has failed.
	 * 
	 * @param event
	 *            The logout failure event.
	 */
	void onLogoutFailure(LogoutFailureEvent event);
}
