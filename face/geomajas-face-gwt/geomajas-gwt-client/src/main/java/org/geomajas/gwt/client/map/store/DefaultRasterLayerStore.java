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
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.cache.tile.RasterTile;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Matrix;
import org.geomajas.layer.tile.TileCode;

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

	private double previousScale = -1;
	
	private Bbox tileBounds;

	private Deferred deferred;

	public DefaultRasterLayerStore(RasterLayer rasterLayer) {
		this.rasterLayer = rasterLayer;
	}

	public void applyAndSync(Bbox bounds, TileFunction<RasterTile> onDelete, TileFunction<RasterTile> onUpdate) {
		boolean scaleChanged = previousScale > 0 && !rasterLayer.getMapModel().getMapView().isPanning();
		if (scaleChanged || isDirty()) {
			if (callBack != null) {
				callBack.cancel();
			}
			for (RasterTile tile : tiles.values()) {
				onDelete.execute(tile);
			}
			tiles.clear();
			tileBounds = null;
			dirty = false;
		}
		previousScale = rasterLayer.getMapModel().getMapView().getCurrentScale();
		if (tileBounds == null || !tileBounds.contains(bounds)) {
			fetchAndUpdateTiles(bounds, onUpdate);
		} else {
			updateTiles(bounds, onUpdate);
		}
	}

	public RasterLayer getLayer() {
		return rasterLayer;
	}

	public void clear() {
		if (tiles.size() > 0 || deferred != null) {
			dirty = true;
			if (deferred != null) {
				deferred.cancel();
			}
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	public Collection<RasterTile> getTiles() {
		return tiles.values();
	}

	private void fetchAndUpdateTiles(Bbox bounds, final TileFunction<RasterTile> onUpdate) {
		// fetch a bigger area to avoid server requests while panning 
		tileBounds = bounds.scale(3);
		GetRasterTilesRequest request = new GetRasterTilesRequest();
		request.setBbox(new org.geomajas.geometry.Bbox(tileBounds.getX(), tileBounds.getY(), tileBounds.getWidth(),
				tileBounds.getHeight()));
		request.setCrs(getLayer().getMapModel().getCrs());
		request.setLayerId(getLayer().getServerLayerId());
		request.setScale(getLayer().getMapModel().getMapView().getCurrentScale());
		GwtCommand command = new GwtCommand("command.render.GetRasterTiles");
		command.setCommandRequest(request);
		callBack = new RasterCallBack(worldToPan(bounds), onUpdate);
		deferred = GwtCommandDispatcher.getInstance().execute(command, callBack);
	}
	
	private void updateTiles(Bbox bounds, final TileFunction<RasterTile> onUpdate) {
		Bbox panBounds = worldToPan(bounds);
		for (RasterTile tile : tiles.values()) {
			if (panBounds.intersects(tile.getBounds())) {
				onUpdate.execute(tile);
			}
		}
	}
	

	private Bbox worldToPan(Bbox bounds) {
		Matrix t = rasterLayer.getMapModel().getMapView().getWorldToPanTransformation();
		return bounds.transform(t);
	}

	private void addTiles(List<org.geomajas.layer.tile.RasterTile> images) {
		Matrix t = rasterLayer.getMapModel().getMapView().getWorldToPanTranslation();
		Bbox cacheBounds = null;
		for (org.geomajas.layer.tile.RasterTile image : images) {
			TileCode code = image.getCode().clone();
			if (!tiles.containsKey(code)) {
				Bbox panBounds = new Bbox(image.getBounds());
				panBounds.translate(Math.round(t.getDx()), Math.round(t.getDy()));
				if (cacheBounds == null) {
					cacheBounds = panBounds;
				} else {
					cacheBounds = cacheBounds.union(panBounds);
				}
				RasterTile tile = new RasterTile(code, panBounds, image.getUrl(), this);
				tiles.put(code, tile);
			}
		}
		deferred = null;
	}

	/**
	 * Raster layer callback for fetching tiles.
	 */
	private final class RasterCallBack implements CommandCallback {

		private boolean cancelled;

		private TileFunction<RasterTile> callback;
		
		private Bbox bounds;

		private RasterCallBack(Bbox bounds, TileFunction<RasterTile> callback) {
			this.callback = callback;
			this.bounds = bounds;
		}

		public void execute(CommandResponse response) {
			if (!cancelled) {
				GetRasterTilesResponse r = (GetRasterTilesResponse) response;
				addTiles(r.getRasterData());
				for (RasterTile tile : tiles.values()) {
					if (bounds.intersects(tile.getBounds())) {
						callback.execute(tile);
					}
				}
			}
		}

		public void cancel() {
			cancelled = true;
		}
	}
}
