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

package org.geomajas.plugin.editing.client.snap.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for handling the coordinate snap events.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface CoordinateSnapHandler extends EventHandler {

	/** Type of this handler. */
	GwtEvent.Type<CoordinateSnapHandler> TYPE = new GwtEvent.Type<CoordinateSnapHandler>();

	/**
	 * Executed whenever the snap method is called. Whether or not snapping actually happened, is stored within the
	 * event.
	 * 
	 * @param event
	 *            The coordinate snapping event.
	 */
	void onCoordinateSnapAttempt(CoordinateSnapEvent event);
}