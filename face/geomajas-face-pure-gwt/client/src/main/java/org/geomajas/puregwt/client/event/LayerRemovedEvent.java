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
 * Event that is fired when a layer is removed from the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerRemovedEvent extends BaseLayerEvent<MapCompositionHandler> {

	public LayerRemovedEvent(Layer<?> layer) {
		super(layer);
	}

	public Type<MapCompositionHandler> getAssociatedType() {
		return MapCompositionHandler.TYPE;
	}

	protected void dispatch(MapCompositionHandler mapCompositionHandler) {
		mapCompositionHandler.onLayerRemoved(this);
	}
}