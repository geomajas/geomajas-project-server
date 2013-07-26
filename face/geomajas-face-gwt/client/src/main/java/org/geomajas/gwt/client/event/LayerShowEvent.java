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
package org.geomajas.gwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.layer.Layer;

/**
 * Event that is fired when a layer becomes visible. This can be caused because some layer are only visible between
 * certain scale levels, or because the user turned a layer on. This event is often triggered by a
 * {@link LayerVisibilityMarkedEvent}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerShowEvent extends BaseLayerEvent<LayerVisibilityHandler> {

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer
	 *            the layer that was shown
	 */
	public LayerShowEvent(Layer layer) {
		super(layer);
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