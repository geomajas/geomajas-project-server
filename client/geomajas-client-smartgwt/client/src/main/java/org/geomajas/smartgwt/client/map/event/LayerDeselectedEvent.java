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
package org.geomajas.smartgwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.map.layer.Layer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the deselection of a layer.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerDeselectedEvent extends GwtEvent<LayerSelectionHandler> {

	private Layer<?> layer;

	/**
	 * Create an event for the specified layer.
	 *
	 * @param layer the layer that was deselected
	 */
	public LayerDeselectedEvent(Layer<?> layer) {
		this.layer = layer;
	}

	@Override
	public Type<LayerSelectionHandler> getAssociatedType() {
		return LayerSelectionHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerSelectionHandler layerSelectionHandler) {
		layerSelectionHandler.onDeselectLayer(this);
	}

	/**
	 * Get layer which was deselected.
	 *
	 * @return deselected layer
	 */
	public Layer<?> getLayer() {
		return layer;
	}
}
