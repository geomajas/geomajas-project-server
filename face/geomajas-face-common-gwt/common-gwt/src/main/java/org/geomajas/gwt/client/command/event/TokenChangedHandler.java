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

package org.geomajas.gwt.client.command.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Handler to be notified when a new token is registered.
 * 
 * @author Joachim Van der Auwera
 * @since 0.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface TokenChangedHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<TokenChangedHandler> TYPE = new GwtEvent.Type<TokenChangedHandler>();

	/**
	 * Called when the user token changes.
	 * 
	 * @param event event containing the token and possibly some user information
	 */
	void onTokenChanged(TokenChangedEvent event);
}
