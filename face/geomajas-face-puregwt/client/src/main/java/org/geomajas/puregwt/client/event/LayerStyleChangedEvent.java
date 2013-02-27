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
package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Event that reports changes in layer style.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerStyleChangedEvent extends BaseLayerEvent<LayerStyleChangedHandler> {

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer of which the style has changed
	 */
	public LayerStyleChangedEvent(Layer layer) {
		super(layer);
	}

	/** {@inheritDoc} */
	@Override
	public Type<LayerStyleChangedHandler> getAssociatedType() {
		return LayerStyleChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerStyleChangedHandler layerStyleChangedHandler) {
		layerStyleChangedHandler.onLayerStyleChanged(this);
	}
}