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
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Event that is fired when new layers are added to the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerAddedEvent extends BaseLayerEvent<MapCompositionHandler> {
	
	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer that was added
	 */
	public LayerAddedEvent(Layer<?> layer) {
		super(layer);
	}

	/** {@inheritDoc} */
	@Override
	public Type<MapCompositionHandler> getAssociatedType() {
		return MapCompositionHandler.TYPE;
	}

	protected void dispatch(MapCompositionHandler mapCompositionHandler) {
		mapCompositionHandler.onLayerAdded(this);
	}
}