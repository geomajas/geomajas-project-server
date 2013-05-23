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
 * Event that reports the deselection of a layer.
 *
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerDeselectedEvent extends BaseLayerEvent<LayerSelectionHandler> {

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer that was deselected
	 */
	public LayerDeselectedEvent(Layer layer) {
		super(layer);
	}

	@Override
	public Type<LayerSelectionHandler> getAssociatedType() {
		return LayerSelectionHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerSelectionHandler layerSelectionHandler) {
		layerSelectionHandler.onDeselectLayer(this);
	}
}