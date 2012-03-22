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
package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that reports the initialization of the map. This is when the actual configuration has been loaded and when
 * layers become available.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MapInitializationEvent extends Event<MapInitializationHandler> {

	@Override
	public Type<MapInitializationHandler> getAssociatedType() {
		return MapInitializationHandler.TYPE;
	}

	@Override
	protected void dispatch(MapInitializationHandler mapInitializationHandler) {
		mapInitializationHandler.onMapInitialized(this);
	}
}