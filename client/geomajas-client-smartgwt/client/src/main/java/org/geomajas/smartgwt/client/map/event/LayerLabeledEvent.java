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
 * Event that reports <code>Layer</code> changes.
 *
 * @author Frank Wynants
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerLabeledEvent extends GwtEvent<LayerChangedHandler> {

	private Layer<?> layer;

	/**
	 * Contstructor.
	 *
	 * @param layer layer which is now labeled
	 */
	public LayerLabeledEvent(Layer<?> layer) {
		this.layer = layer;
	}

	/**
	 * Get layer which is not labeled.
	 *
	 * @return labelled layer
	 */
	public Layer<?> getLayer() {
		return layer;
	}

	@Override
	public Type<LayerChangedHandler> getAssociatedType() {
		return LayerChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerChangedHandler layerChangedHandler) {
		layerChangedHandler.onLabelChange(this);
	}
}