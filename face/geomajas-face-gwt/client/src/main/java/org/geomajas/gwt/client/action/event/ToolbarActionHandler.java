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

package org.geomajas.gwt.client.action.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.UserImplemented;

/**
 * <p>
 * Handler for enabled and disabled events on a single <code>ToolbarBaseAction</code>.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ToolbarActionHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<ToolbarActionHandler> TYPE = new GwtEvent.Type<ToolbarActionHandler>();

	/**
	 * Called when a toolbar action is enabled.
	 * 
	 * @param event
	 *            {@link ToolbarActionEnabledEvent}
	 */
	void onToolbarActionEnabled(ToolbarActionEnabledEvent event);

	/**
	 * Called when a toolbar action is disabled.
	 * 
	 * @param event
	 *            {@link ToolbarActionDisabledEvent}
	 */
	void onToolbarActionDisabled(ToolbarActionDisabledEvent event);
}
