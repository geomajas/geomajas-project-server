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
@Api
public class LayerShownEvent extends GwtEvent<LayerChangedHandler> {

	private Layer<?> layer;

	private boolean scaleChange;

	public LayerShownEvent(Layer<?> layer) {
		this(layer, false);
	}

	/**
	 * @since 1.9.0
	 */
	public LayerShownEvent(Layer<?> layer, boolean scaleChange) {
		this.layer = layer;
		this.scaleChange = scaleChange;
	}

	public Layer<?> getLayer() {
		return layer;
	}

	/**
	 * True if this event was fired because the layer came into view or went out
	 * of view by a scale change.
	 * 
	 * @return if a scalechange fired this event
	 * @since 1.9.0
	 */
	public boolean isScaleChange() {
		return scaleChange;
	}

	@Override
	public Type<LayerChangedHandler> getAssociatedType() {
		return LayerChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerChangedHandler layerChangedHandler) {
		layerChangedHandler.onVisibleChange(this);
	}
}