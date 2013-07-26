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

package org.geomajas.gwt.client.map.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.gwt.client.event.ScaleLevelRenderedEvent;
import org.geomajas.gwt.client.gfx.HtmlContainer;
import org.geomajas.gwt.client.map.layer.VectorServerLayer;
import org.geomajas.gwt.client.service.CommandService;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Tiled scale presenter for a vector layer. It displays a single tile level for a single ector layer.
 * 
 * @author Pieter De Graef
 */
public class VectorLayerScaleRenderer implements LayerScaleRenderer {

	private final String crs;

	private final VectorServerLayer vectorLayer;

	private final HtmlContainer htmlContainer;

	private final Bbox layerBounds;

	private final double scale;

	private final Map<String, VectorTilePresenter> tiles;

	private double mapExtentScaleAtFetch = 2;

	private Deferred deferred;

	// private boolean renderingImages;
	private int nrLoadingTiles;

	private CommandService commandService;

	private EventBus eventBus;

	private Object eventSource;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------
	@Inject
	public VectorLayerScaleRenderer(EventBus eventBus, CommandService commandService, @Assisted Object eventSource,
			@Assisted String crs, @Assisted VectorServerLayer vectorLayer, @Assisted HtmlContainer htmlContainer,
			@Assisted double scale) {
		this.eventBus = eventBus;
		this.commandService = commandService;
		this.eventSource = eventSource;
		this.crs = crs;
		this.vectorLayer = vectorLayer;
		this.htmlContainer = htmlContainer;
		this.scale = scale;

		layerBounds = vectorLayer.getLayerInfo().getMaxExtent();
		tiles = new HashMap<String, VectorTilePresenter>();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public void onScaleRendered(HtmlContainer container, double scale) {
		eventBus.fireEventFromSource(new ScaleLevelRenderedEvent(scale), eventSource);
	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public void cancel() {
		// Perhaps we where busy fetching the correct tiles?
		if (deferred != null) {
			deferred.cancel();
			deferred = null;
		}

		// Perhaps we where busy rendering the tiles?
	}

	@Override
	public void render(Bbox bbox) {
		// Only fetch when inside the layer bounds:
		if (BboxService.intersects(bbox, layerBounds) && vectorLayer.isShowing()) {

			// Find needed tile codes:
			List<TileCode> tempCodes = calcCodesForBounds(bbox);
			for (TileCode tileCode : tempCodes) {

				VectorTilePresenter tilePresenter = tiles.get(tileCode.toString());
				if (tilePresenter == null) {
					// New tile
					tilePresenter = addTile(tileCode);
					tilePresenter.render();
				} else if (tilePresenter.getSiblingStatus() == VectorTilePresenter.STATUS.EMPTY) {
					// Tile already exists, but the siblings have not yet been loaded:
					tilePresenter.renderSiblings();
				}
			}
		}
	}

	public VectorTilePresenter addTile(TileCode tileCode) {
		VectorTilePresenter tilePresenter = tiles.get(tileCode.toString());
		if (tilePresenter == null) {
			tilePresenter = new VectorTilePresenter(commandService, this, tileCode.clone(), scale, crs,
					new TileLoadCallback());
			nrLoadingTiles++;
			tiles.put(tileCode.toString(), tilePresenter);
		}
		return tilePresenter;
	}

	public VectorTilePresenter getTile(TileCode tileCode) {
		return tiles.get(tileCode.toString());
	}

	/**
	 * Always returns true...
	 */
	public boolean isRendered() {
		return nrLoadingTiles == 0;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public boolean isFetchingTiles() {
		return deferred != null;
	}

	public boolean isRenderingImages() {
		return nrLoadingTiles > 0;
	}

	public HtmlContainer getHtmlContainer() {
		return htmlContainer;
	}

	public double getMapExtentScaleAtFetch() {
		return mapExtentScaleAtFetch;
	}

	public void setMapExtentScaleAtFetch(double mapExtentScaleAtFetch) {
		this.mapExtentScaleAtFetch = mapExtentScaleAtFetch;
	}

	public VectorServerLayer getLayer() {
		return vectorLayer;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Saves the complete array of TileCode objects for the given bounds (and the current scale).
	 * 
	 * @param bounds view bounds
	 * @return list of tiles in these bounds
	 */
	private List<TileCode> calcCodesForBounds(Bbox bounds) {
		int currentTileLevel = calculateTileLevel(bounds);

		// Calculate tile width and height for tileLevel=currentTileLevel
		double div = Math.pow(2, currentTileLevel); // tile level must be correct!
		double tileWidth = Math.ceil((scale * layerBounds.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * layerBounds.getHeight()) / div) / scale;

		// For safety (to prevent division by 0):
		List<TileCode> codes = new ArrayList<TileCode>();
		if (tileWidth == 0 || tileHeight == 0) {
			return codes;
		}

		// Calculate bounds relative to extents:
		Bbox clippedBounds = BboxService.intersection(bounds, layerBounds);
		if (clippedBounds == null) {
			// TODO throw error? If this is null, then the server configuration is incorrect.
			return codes;
		}
		double relativeBoundX = Math.abs(clippedBounds.getX() - layerBounds.getX());
		double relativeBoundY = Math.abs(clippedBounds.getY() - layerBounds.getY());
		int currentMinX = (int) Math.floor(relativeBoundX / tileWidth);
		int currentMinY = (int) Math.floor(relativeBoundY / tileHeight);
		int currentMaxX = (int) Math.ceil((relativeBoundX + clippedBounds.getWidth()) / tileWidth) - 1;
		int currentMaxY = (int) Math.ceil((relativeBoundY + clippedBounds.getHeight()) / tileHeight) - 1;

		// Now fill the list with the correct codes:
		for (int x = currentMinX; x <= currentMaxX; x++) {
			for (int y = currentMinY; y <= currentMaxY; y++) {
				codes.add(new TileCode(currentTileLevel, x, y));
			}
		}
		return codes;
	}

	/**
	 * Calculate the best tile level to use for a certain view-bounds.
	 * 
	 * @param bounds view bounds
	 * @return best tile level for view bounds
	 */
	private int calculateTileLevel(Bbox bounds) {
		double baseX = layerBounds.getWidth();
		double baseY = layerBounds.getHeight();
		// choose the tile level so the area is between 256*256 and 512*512 pixels
		double baseArea = baseX * baseY;
		double osmArea = 256 * 256 / (scale * scale);
		int tileLevel = (int) Math.floor(Math.log(baseArea / osmArea) / Math.log(4.0));
		if (tileLevel < 0) {
			tileLevel = 0;
		}
		return tileLevel;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Callback that keeps track of the number of tiles still underway.
	 * 
	 * @author Pieter De Graef
	 */
	private class TileLoadCallback implements Callback<String, String> {

		public void onFailure(String reason) {
			onLoadingDone();
		}

		public void onSuccess(String result) {
			onLoadingDone();
		}

		private void onLoadingDone() {
			nrLoadingTiles--;
			if (nrLoadingTiles == 0) {
				onScaleRendered(htmlContainer, scale);
			}
		}
	}
}