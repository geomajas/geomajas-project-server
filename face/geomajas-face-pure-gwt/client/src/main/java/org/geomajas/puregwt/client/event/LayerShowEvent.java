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

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports <code>Layer</code> changes.
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerShowEvent extends GwtEvent<LayerVisibilityHandler> {

	private Layer<?> layer;

	public LayerShowEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Layer<?> getLayer() {
		return layer;
	}

	@Override
	public Type<LayerVisibilityHandler> getAssociatedType() {
		return LayerVisibilityHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerVisibilityHandler layerVisibleHandler) {
		layerVisibleHandler.onShow(this);
	}
}