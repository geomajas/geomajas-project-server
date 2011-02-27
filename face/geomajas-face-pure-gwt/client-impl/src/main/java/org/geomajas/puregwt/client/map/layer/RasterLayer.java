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
package org.geomajas.puregwt.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.puregwt.client.map.MapModel;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.layer.OpacitySupported;

/**
 * <p>
 * The client side representation of a raster layer.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
public class RasterLayer extends AbstractLayer<ClientRasterLayerInfo> implements OpacitySupported {

	//private RasterLayerStore store;
	
	private RasterLayerRenderer renderer;

	/**
	 * The only constructor! Set the MapModel and the layer info.
	 * 
	 * @param mapModel
	 *            The model of layers and features behind a map. This layer will be a part of this model.
	 */
	public RasterLayer(MapModel mapModel, ClientRasterLayerInfo layerInfo) {
		super(mapModel, layerInfo);
		//store = new DefaultRasterLayerStore(this);
		renderer = new RasterLayerRenderer(this);
	}

	/**
	 * Apply a new opacity on the entire raster layer.
	 * 
	 * @param opacity
	 *            The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *            visible.
	 */
	public void setOpacity(double opacity) {
		getLayerInfo().setStyle(opacity + "");
//		for (RasterTile tile : store.getTiles()) {
//			tile.setStyle(new PictureStyle(opacity));
//		}
		mapModel.getEventBus().fireEvent(new LayerStyleChangedEvent(this));
	}

	
	public RasterLayerRenderer getRenderer() {
		return renderer;
	}
}
