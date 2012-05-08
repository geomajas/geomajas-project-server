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

package org.geomajas.gwt.client.map.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapModel;

/**
 * Event that reports that the {@link org.geomajas.gwt.client.map.MapModel} has changed.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public class MapModelChangedEvent extends GwtEvent<MapModelChangedHandler> {

	private MapModel mapModel;

	/**
	 * Constructor.
	 *
	 * @param mapModel changed map model
	 */
	public MapModelChangedEvent(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	/**
	 * Get the {@link MapModel} which has changed.
	 *
	 * @return map model
	 */
	public MapModel getMapModel() {
		return mapModel;
	}

	/** {@inheritDoc} */
	public Type<MapModelChangedHandler> getAssociatedType() {
		return MapModelChangedHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(MapModelChangedHandler mapModelChangedHandler) {
		mapModelChangedHandler.onMapModelChanged(this);
	}
}
