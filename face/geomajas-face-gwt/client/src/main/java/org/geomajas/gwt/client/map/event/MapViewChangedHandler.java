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

package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.EventHandler;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for event handlers that catch {@link org.geomajas.gwt.client.map.event.MapViewChangedEvent}s.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface MapViewChangedHandler extends EventHandler {

	/**
	 * The event catching method.
	 *
	 * @param event
	 *            The actual {@link org.geomajas.gwt.client.map.event.MapViewChangedEvent}. Contains the new bounding
	 *            box and scale on the map.
	 */
	void onMapViewChanged(MapViewChangedEvent event);
}
