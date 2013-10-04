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
 * <p>
 * Called when a layer has been marked as visible or invisible. When a layer has been marked as invisible, expect a
 * <code>LayerHideEvent</code> very soon.
 * </p>
 * <p>
 * But, when a layer has been marked as visible, that does not necessarily mean it will become visible. There are more
 * requirements that have to be met in order for a layer to become visible: the map's scale must be between the minimum
 * and maximum allowed scales for the layer. If that requirement has been met as well, expect a
 * <code>LayerShowEvent</code> shortly.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LayerVisibilityMarkedEvent extends BaseLayerEvent<LayerVisibilityHandler> {

	/**
	 * Create an event for the specified layer.
	 * 
	 * @param layer the layer of which the visibility mark has changed
	 */
	public LayerVisibilityMarkedEvent(Layer layer) {
		super(layer);
	}

	@Override
	public Type<LayerVisibilityHandler> getAssociatedType() {
		return LayerVisibilityHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerVisibilityHandler layerVisibleHandler) {
		layerVisibleHandler.onVisibilityMarked(this);
	}
}