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
package org.geomajas.gwt.client.map.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetRasterDataRequest;
import org.geomajas.command.dto.GetRasterDataResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.cache.tile.RasterTile;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.layer.tile.TileCode;

import com.google.gwt.core.client.GWT;

/**
 * A raster layer store that keeps tiles in cache while panning, only clearing them when a non-panning move occurs, see
 * {@link org.geomajas.gwt.client.map.MapView}.
 * 
 * @author Jan De Moerloose
 */
public class DefaultRasterLayerStore implements RasterLayerStore {

	private RasterLayer rasterLayer;

	private Map<TileCode, RasterTile> tiles = new HashMap<TileCode, RasterTile>();

	private RasterCallBack callBack;

	private boolean dirty;

	public DefaultRasterLayerStore(RasterLayer rasterLayer) {
		this.rasterLayer = rasterLayer;
	}

	public void applyAndSync(Bbox bounds, TileFunction<RasterTile> onDelete, TileFunction<RasterTile> onUpdate) {
		if (!rasterLayer.getMapModel().getMapView().isPanning() || isDirty()) {
			if (callBack != null) {
				callBack.cancel();
			}
			for (RasterTile tile : tiles.values()) {
				onDelete.execute(tile);
			}
			tiles.clear();
			dirty = false;
		}
		fetchAndUpdateTiles(bounds, onUpdate);
	}

	public RasterLayer getLayer() {
		return rasterLayer;
	}

	public void clear() {
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public Collection<RasterTile> getTiles() {
		return tiles.values();
	}

	private void fetchAndUpdateTiles(Bbox bounds, final TileFunction<RasterTile> onUpdate) {
		GetRasterDataRequest request = new GetRasterDataRequest();
		request.setBbox(new org.geomajas.geometry.Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds
				.getHeight()));
		request.setCrs(getLayer().getMapModel().getCrs());
		request.setLayerId(getLayer().getId());
		request.setScale(getLayer().getMapModel().getMapView().getCurrentScale());
		GwtCommand command = new GwtCommand("command.render.GetRasterData");
		command.setCommandRequest(request);
		callBack = new RasterCallBack(onUpdate);
		GwtCommandDispatcher.getInstance().execute(command, callBack);
	}

	private void addTiles(List<org.geomajas.layer.tile.RasterTile> images) {
		for (org.geomajas.layer.tile.RasterTile image : images) {
			TileCode code = image.getCode().clone();
			if (!tiles.containsKey(code)) {
				RasterTile tile = new RasterTile(code, new Bbox(image.getBounds()), image.getUrl(), this);
				Matrix t = rasterLayer.getMapModel().getMapView().getWorldToPanTranslation();
				tile.getBounds().translate(Math.round(t.getDx()), Math.round(t.getDy()));
				tiles.put(code, tile);
			}
		}
	}

	/**
	 * ???
	 */
	private final class RasterCallBack implements CommandCallback {

		private boolean canceled;

		private TileFunction<RasterTile> callback;

		private RasterCallBack(TileFunction<RasterTile> callback) {
			this.callback = callback;
		}

		public void execute(CommandResponse response) {
			if (!canceled) {
				GetRasterDataResponse r = (GetRasterDataResponse) response;
				addTiles(r.getRasterData());
				for (RasterTile tile : tiles.values()) {
					callback.execute(tile);
				}
			} else {
				GWT.log("canceled ", null);
			}
		}

		public void cancel() {
			canceled = true;
		}
	}

}
