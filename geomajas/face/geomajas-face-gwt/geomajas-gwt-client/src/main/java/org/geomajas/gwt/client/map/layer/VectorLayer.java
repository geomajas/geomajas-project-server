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

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.cache.TileCache;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p>
 * The client side representation of a vector layer.
 * </p>
 *
 * @author Pieter De Graef
 */
public class VectorLayer extends AbstractLayer<ClientVectorLayerInfo> {

	/** Storage of features in this layer. */
	private TileCache cache;

	private String filter;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The only constructor! Set the MapModel and the metadata.
	 *
	 * @param mapModel
	 *            The model of layers and features behind a map. This layer will be a part of this model.
	 */
	public VectorLayer(MapModel mapModel, ClientVectorLayerInfo layerInfo) {
		super(mapModel, layerInfo);
		Bbox maxExtent = new Bbox(layerInfo.getMaxExtent().getX(), layerInfo.getMaxExtent().getY(), layerInfo
				.getMaxExtent().getWidth(), layerInfo.getMaxExtent().getHeight());
		cache = new TileCache(this, maxExtent, layerInfo.getMaxTileLevel());
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Return this layer's unique ID.
	 *
	 * @return
	 */
	public void accept(final PainterVisitor visitor, final Bbox bounds, boolean recursive) {
		// Draw layer-specific stuff (see VectorLayerPainter)
		visitor.visit(this);

		// When visible, take care of fetching through an queryAndSync:
		if (recursive && isShowing()) {
			TileFunction<VectorTile> onDelete = new TileFunction<VectorTile>() {

				// When deleting a tile, delete selected features in it first:
				public void execute(VectorTile tile) {
					for (Feature feature : tile.getFeatures()) {
						if (feature != null && feature.isSelected()) {
							visitor.remove(feature);
						}
					}
					visitor.remove(tile);
				}
			};
			TileFunction<VectorTile> onUpdate = new TileFunction<VectorTile>() {

				// Updating a tile, is simply re-rendering it:
				public void execute(VectorTile tile) {
					tile.accept(visitor, bounds, true);
				}
			};
			cache.queryAndSync(bounds, filter, onDelete, onUpdate);
		}
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Return whether the feature with given id is selected.
	 *
	 * @return true when the feature with given ide is selected
	 */
	public boolean isFeatureSelected(String id) {
		return mapModel.isFeatureSelected(id);
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public VectorLayerStore getFeatureStore() {
		return cache;
	}
}
