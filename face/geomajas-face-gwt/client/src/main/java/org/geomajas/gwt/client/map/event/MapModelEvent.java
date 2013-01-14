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

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports {@link org.geomajas.gwt.client.map.MapModel} changes.
 * <p/>
 * Note that this only report the first intialization of the map model. For all changes, use
 * {@link MapModelChangedEvent}.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 * @deprecated use {@link MapModelChangedEvent}
 */
@Api(allMethods = true)
@Deprecated
public class MapModelEvent extends GwtEvent<MapModelHandler> {

	/** Event type. */
	public static final Type<MapModelHandler> TYPE = new Type<MapModelHandler>();

	/** {@inheritDoc} */
	public Type<MapModelHandler> getAssociatedType() {
		return TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(MapModelHandler mapViewHandler) {
		mapViewHandler.onMapModelChange(this);
	}
}
