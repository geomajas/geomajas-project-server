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
 * Event that is fired when new layers are added to the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerAddedEvent extends GwtEvent<MapCompositionHandler> {

	private Layer<?> layer;

	public LayerAddedEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Type<MapCompositionHandler> getAssociatedType() {
		return MapCompositionHandler.TYPE;
	}

	protected void dispatch(MapCompositionHandler mapCompositionHandler) {
		mapCompositionHandler.onLayerAdded(this);
	}

	public Layer<?> getLayer() {
		return layer;
	}
}