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

import org.geomajas.global.Api;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that is fired when a layer is marked as invisible.
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerHideEvent extends GwtEvent<LayerVisibleHandler> {

	private Layer<?> layer;

	public LayerHideEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Layer<?> getLayer() {
		return layer;
	}

	@Override
	public Type<LayerVisibleHandler> getAssociatedType() {
		return LayerVisibleHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerVisibleHandler layerVisibleHandler) {
		layerVisibleHandler.onHide(this);
	}
}