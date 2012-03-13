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

import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.web.bindery.event.shared.Event;

/**
 * Base event for events that are layer-bound.
 * 
 * @author Jan De Moerloose
 * 
 * @param <H>
 */
public abstract class BaseLayerEvent<H> extends Event<H> {

	private Layer<?> layer;

	protected BaseLayerEvent(Layer<?> layer) {
		this.layer = layer;
	}

	/**
	 * Get the layer for which this event occurred.
	 * 
	 * @return the layer
	 */
	public Layer<?> getLayer() {
		return layer;
	}

}
