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
package org.geomajas.smartgwt.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.style.PictureStyle;
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.cache.tile.RasterTile;
import org.geomajas.smartgwt.client.map.cache.tile.TileFunction;
import org.geomajas.smartgwt.client.map.event.LayerStyleChangeEvent;
import org.geomajas.smartgwt.client.map.store.DefaultRasterLayerStore;
import org.geomajas.smartgwt.client.map.store.RasterLayerStore;
import org.geomajas.smartgwt.client.spatial.Bbox;

/**
 * <p>
 * The client side representation of a raster layer.
 * </p>
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api
public class RasterLayer extends AbstractLayer<ClientRasterLayerInfo> {

	private RasterLayerStore store;

	/**
	 * The only constructor! Set the MapModel and the layer info.
	 * 
	 * @param mapModel
	 *            The model of layers and features behind a map. This layer will be a part of this model.
	 */
	public RasterLayer(MapModel mapModel, ClientRasterLayerInfo layerInfo) {
		super(mapModel, layerInfo);
		store = new DefaultRasterLayerStore(this);
	}

	@Override
	public void accept(final PainterVisitor visitor, final Object group, final Bbox bounds, boolean recursive) {
		visitor.visit(this, group);

		// When visible, take care of fetching through an applyAndSync:
		if (recursive && isShowing()) {
			TileFunction<RasterTile> onDelete = new TileFunction<RasterTile>() {

				public void execute(RasterTile tile) {
					visitor.remove(tile, group);
				}
			};
			TileFunction<RasterTile> onUpdate = new TileFunction<RasterTile>() {

				// Updating a tile, re-rendering it:
				public void execute(RasterTile tile) {
					tile.accept(visitor, group, bounds, true);
				}
			};
			store.applyAndSync(bounds, onDelete, onUpdate);
		}
	}

	public RasterLayerStore getStore() {
		return store;
	}

	/**
	 * Apply a new opacity on the entire raster layer.
	 * 
	 * @param opacity
	 *            The new opacity value. Must be a value between 0 and 1, where 0 means invisible and 1 is totally
	 *            visible.
	 * @since 1.8.0
	 */
	public void setOpacity(double opacity) {
		getLayerInfo().setStyle(Double.toString(opacity));
		for (RasterTile tile : store.getTiles()) {
			tile.setStyle(new PictureStyle(opacity));
		}
		handlerManager.fireEvent(new LayerStyleChangeEvent(this));
	}

	@Override
	public void updateStyle() {
		handlerManager.fireEvent(new LayerStyleChangeEvent(this));
	}
}
