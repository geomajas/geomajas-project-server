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
package org.geomajas.puregwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the initialization of the map.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MapInitializationEvent extends GwtEvent<MapInitializationHandler> {

	public static final Type<MapInitializationHandler> TYPE = new Type<MapInitializationHandler>();

	@Override
	public Type<MapInitializationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MapInitializationHandler mapInitializationHandler) {
		mapInitializationHandler.onMapInitialized(this);
	}
}