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
 * Interface for handling operation events for geometry editing. Supported operations are: move, insert, delete. For
 * each such operation specific events exist. These events will contain exactly what has been moved/inserted/deleted.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface GeometryEditOperationHandler extends EventHandler {

	GwtEvent.Type<GeometryEditOperationHandler> TYPE = new GwtEvent.Type<GeometryEditOperationHandler>();

	void onGeometryEditMove(GeometryEditMoveEvent event);

	void onGeometryEditInsert(GeometryEditInsertEvent event);

	void onGeometryEditDelete(GeometryEditDeleteEvent event);
}
