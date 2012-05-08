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

package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.UserImplemented;

/**
 * Handler for the editing events.
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api(allMethods = true)
@UserImplemented
public interface EditingHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<EditingHandler> TYPE = new GwtEvent.Type<EditingHandler>();

	/**
	 * Called when changes in editing state occur.
	 * 
	 * @param event
	 *            The {@link EditingEvent}.
	 */
	void onEditingChange(EditingEvent event);
}