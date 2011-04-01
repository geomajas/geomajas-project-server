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
package org.geomajas.gwt.client.map.event;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.map.layer.Layer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports <code>Layer</code> changes.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public class LayerFilteredEvent extends GwtEvent<LayerChangedHandler> {

	private Layer<?> layer;

	public LayerFilteredEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Layer<?> getLayer() {
		return layer;
	}

	@Override
	public Type<LayerChangedHandler> getAssociatedType() {
		return LayerChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerChangedHandler layerChangedHandler) {
		layerChangedHandler.onFilterChange(this);
	}
}