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
 * Event that reports a layer has been refreshed.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerRefreshedEvent extends BaseLayerEvent<LayerRefreshedHandler> {

	public LayerRefreshedEvent(Layer<?> layer) {
		super(layer);
	}

	public Type<LayerRefreshedHandler> getAssociatedType() {
		return LayerRefreshedHandler.TYPE;
	}

	protected void dispatch(LayerRefreshedHandler handler) {
		handler.onLayerRefreshed(this);
	}
}