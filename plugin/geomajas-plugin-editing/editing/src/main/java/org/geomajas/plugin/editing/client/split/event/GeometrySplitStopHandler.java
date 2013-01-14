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

package org.geomajas.plugin.editing.client.split.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for catching geometry splitting stop events.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface GeometrySplitStopHandler extends EventHandler {

	/** Type of this handler. */
	GwtEvent.Type<GeometrySplitStopHandler> TYPE = new GwtEvent.Type<GeometrySplitStopHandler>();

	/**
	 * Executed when the geometry splitting process has ended.
	 * 
	 * @param event
	 *            The geometry splitting stop event.
	 */
	void onGeometrySplitStop(GeometrySplitStopEvent event);
}