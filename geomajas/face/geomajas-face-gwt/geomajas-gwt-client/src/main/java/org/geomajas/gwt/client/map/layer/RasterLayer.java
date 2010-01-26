/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.cache.tile.RasterTile;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.store.DefaultRasterLayerStore;
import org.geomajas.gwt.client.map.store.RasterLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p> The client side representation of a raster layer. </p>
 *
 * @author Jan De Moerloose
 */
public class RasterLayer extends AbstractLayer<RasterLayerInfo> {

	private RasterLayerStore store;

	/**
	 * The only constructor! Set the MapModel and the layer info.
	 *
	 * @param mapModel The model of layers and features behind a map. This layer will be a part of this model.
	 */
	public RasterLayer(MapModel mapModel, RasterLayerInfo layerInfo) {
		super(mapModel, layerInfo);
		store = new DefaultRasterLayerStore(this);
	}

	@Override
	public void accept(final PainterVisitor visitor, final Bbox bounds, boolean recursive) {
		visitor.visit(this);
		// When visible, take care of fetching through an applyAndSync:
		if (recursive && isShowing()) {
			TileFunction<RasterTile> onDelete = new TileFunction<RasterTile>() {
				public void execute(RasterTile tile) {
					visitor.remove(tile);
				}
			};
			TileFunction<RasterTile> onUpdate = new TileFunction<RasterTile>() {
				// Updating a tile, is simply re-rendering it:
				public void execute(RasterTile tile) {
					tile.accept(visitor, bounds, true);
				}
			};
			store.applyAndSync(bounds, onDelete, onUpdate);
		}
	}

}
