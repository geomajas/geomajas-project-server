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
package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports <code>MapModel</code> changes.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class MapModelEvent extends GwtEvent<MapModelHandler> {

	public static final Type<MapModelHandler> TYPE = new Type<MapModelHandler>();

	@Override
	public Type<MapModelHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MapModelHandler mapViewHandler) {
		mapViewHandler.onMapModelChange(this);
	}
}
