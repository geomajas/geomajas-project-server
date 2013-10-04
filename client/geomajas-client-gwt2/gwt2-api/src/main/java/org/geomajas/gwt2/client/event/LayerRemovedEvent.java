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
 * Event that is fired when a layer is removed from the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerRemovedEvent extends BaseLayerEvent<MapCompositionHandler> {
	
	private int index;

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer which has been removed
	 * @param index the index of the layer before removal
	 */
	public LayerRemovedEvent(Layer layer, int index) {
		super(layer);
		this.index = index;
	}

	@Override
	public Type<MapCompositionHandler> getAssociatedType() {
		return MapCompositionHandler.TYPE;
	}
	
	/**
	 * Get the index of the layer.
	 * 
	 * @return index of the layer before removal.
	 */
	public int getIndex() {
		return index;
	}

	/** @todo javadoc unknown. */
	protected void dispatch(MapCompositionHandler mapCompositionHandler) {
		mapCompositionHandler.onLayerRemoved(this);
	}
}