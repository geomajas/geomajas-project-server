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
 * Interface for event handlers that catch {@link GeometryIndexHighlightBeginEvent}s.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface GeometryIndexHighlightBeginHandler extends EventHandler {

	GwtEvent.Type<GeometryIndexHighlightBeginHandler> TYPE = new GwtEvent.Type<GeometryIndexHighlightBeginHandler>();

	/**
	 * Called when highlighting a part of a geometry has begun.
	 * 
	 * @param event
	 *            {@link GeometryIndexHighlightBeginEvent}
	 */
	void onGeometryIndexHighlightBegin(GeometryIndexHighlightBeginEvent event);
}