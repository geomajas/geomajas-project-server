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
package org.geomajas.puregwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the selection of a layer.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerSelectedEvent extends GwtEvent<LayerSelectionHandler> {

	private Layer<?> layer;

	public LayerSelectedEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Type<LayerSelectionHandler> getAssociatedType() {
		return LayerSelectionHandler.TYPE;
	}

	protected void dispatch(LayerSelectionHandler selectLayerHandler) {
		selectLayerHandler.onSelectLayer(this);
	}

	public Layer<?> getLayer() {
		return layer;
	}
}