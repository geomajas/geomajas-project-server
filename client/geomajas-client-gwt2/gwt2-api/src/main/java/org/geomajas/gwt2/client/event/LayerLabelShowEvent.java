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
package org.geomajas.gwt2.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt2.client.map.layer.Layer;

/**
 * Event that is fired when the labels of a layer have become visible.
 *
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerLabelShowEvent extends BaseLayerEvent<LayerLabeledHandler> {

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer of which the labels where shown
	 */
	public LayerLabelShowEvent(Layer layer) {
		super(layer);
	}

	@Override
	public Type<LayerLabeledHandler> getAssociatedType() {
		return LayerLabeledHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerLabeledHandler layerChangedHandler) {
		layerChangedHandler.onLabelShow(this);
	}
}