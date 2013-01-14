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

package org.geomajas.plugin.editing.client.event.state;

import org.geomajas.annotation.FutureApi;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for event handlers that catch {@link GeometryIndexMarkForDeletionBeginEvent}s.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface GeometryIndexMarkForDeletionBeginHandler extends EventHandler {

	GwtEvent.Type<GeometryIndexMarkForDeletionBeginHandler> TYPE = new GwtEvent.
			Type<GeometryIndexMarkForDeletionBeginHandler>();

	/**
	 * Called when a part of the geometry has been marked for deletion (not deleted just yet).
	 * 
	 * @param event
	 *            {@link GeometryIndexMarkForDeletionBeginEvent}
	 */
	void onGeometryIndexMarkForDeletionBegin(GeometryIndexMarkForDeletionBeginEvent event);
}