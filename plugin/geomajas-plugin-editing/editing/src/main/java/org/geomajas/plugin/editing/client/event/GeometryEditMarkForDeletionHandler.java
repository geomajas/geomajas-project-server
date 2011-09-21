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

package org.geomajas.plugin.editing.client.event;

import org.geomajas.annotation.FutureApi;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for event handlers that catch {@link GeometryEditMarkForDeletionBeginEvent}s and
 * {@link GeometryEditMarkForDeletionEndEvent}s.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface GeometryEditMarkForDeletionHandler extends EventHandler {

	GwtEvent.Type<GeometryEditMarkForDeletionHandler> TYPE = new GwtEvent.Type<GeometryEditMarkForDeletionHandler>();

	/**
	 * Called when a part of the geometry has been marked for deletion (not deleted just yet).
	 * 
	 * @param event
	 *            {@link GeometryEditMarkForDeletionBeginEvent}
	 */
	void onMarkForDeletionBegin(GeometryEditMarkForDeletionBeginEvent event);

	/**
	 * Called when a part of the geometry has been unmarked for deletion. It is safe again.
	 * 
	 * @param event
	 *            {@link GeometryEditMarkForDeletionEndEvent}
	 */
	void onMarkForDeletionEnd(GeometryEditMarkForDeletionEndEvent event);
}