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

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapModel;

/**
 * Event that reports when a {@link org.geomajas.gwt.client.map.MapModel} will be cleared.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
public class MapModelClearEvent extends GwtEvent<MapModelClearHandler> {

	private MapModel mapModel;

	/**
	 * Constructor.
	 *
	 * @param mapModel map model to clear
	 */
	public MapModelClearEvent(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	/**
	 * Get the {@link org.geomajas.gwt.client.map.MapModel} will be cleared.
	 *
	 * @return map model
	 */
	public MapModel getMapModel() {
		return mapModel;
	}

	/** {@inheritDoc} */
	public Type<MapModelClearHandler> getAssociatedType() {
		return MapModelClearHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(MapModelClearHandler mapModelClearHandler) {
		mapModelClearHandler.onMapModelClear(this);
	}
}
