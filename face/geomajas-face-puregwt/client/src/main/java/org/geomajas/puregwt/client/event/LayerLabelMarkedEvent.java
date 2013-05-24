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
 * <p>
 * Event that is fired when the labels of a layer have been marked as visible or invisible. Note that when labels have
 * been marked as invisible at a moment when they where actually visible, than you can expect a
 * <code>LayerLabelHideEvent</code> shortly.
 * </p>
 * <p>
 * On the other hand marking labels as visible does not necessarily mean that they will become visible. For labels to
 * becomes visible, they must be invisible and their layer must be visible as well. Only if those requirements are met
 * will the labels truly become visible and can you expect a <code>LayerLabelShowEvent</code> to follow this event.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerLabelMarkedEvent extends BaseLayerEvent<LayerLabeledHandler> {

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer of which the labels where marked as visible/invisible
	 */
	public LayerLabelMarkedEvent(Layer layer) {
		super(layer);
	}

	@Override
	public Type<LayerLabeledHandler> getAssociatedType() {
		return LayerLabeledHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerLabeledHandler layerChangedHandler) {
		layerChangedHandler.onLabelMarked(this);
	}
}