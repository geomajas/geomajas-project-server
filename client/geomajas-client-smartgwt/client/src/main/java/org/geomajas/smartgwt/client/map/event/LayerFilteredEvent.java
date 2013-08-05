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
 * Event that reports <code>VectorLayer</code> filter changes.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
public class LayerFilteredEvent extends GwtEvent<LayerFilteredHandler> {

	private Layer<?> layer;

	/**
	 * Constructor.
	 *
	 * @param layer layer which is filtered
	 */
	public LayerFilteredEvent(Layer<?> layer) {
		this.layer = layer;
	}

	/**
	 * Get layer.
	 *
	 * @return layer
	 */
	public Layer<?> getLayer() {
		return layer;
	}

	@Override
	public Type<LayerFilteredHandler> getAssociatedType() {
		return LayerFilteredHandler.TYPE;
	}

	@Override

	protected void dispatch(LayerFilteredHandler layerFilteredHandler) {
		layerFilteredHandler.onFilterChange(this);
	}
}