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

package org.geomajas.plugin.wmsclient.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.client.service.WmsTileService;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.HtmlImage;
import org.geomajas.puregwt.client.gfx.HtmlImageFactory;
import org.geomajas.puregwt.client.map.layer.OpacitySupported;
import org.geomajas.puregwt.client.map.render.event.ScaleLevelRenderedEvent;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Default implementation of a {@link WmsTiledScaleRenderer}.
 * 
 * @author Pieter De Graef
 */
public class WmsTiledScaleRendererImpl implements WmsTiledScaleRenderer {

	private final HtmlContainer container;

	private final WmsLayer layer;

	private final double scale;

	private final Map<String, RasterTile> tiles;

	@Inject
	private WmsService wmsService;

	@Inject
	private WmsTileService tileService;

	@Inject
	private HtmlImageFactory htmlImageFactory;

	@Inject
	private EventBus eventBus;

	private int nrLoadingTiles;

	@Inject
	public WmsTiledScaleRendererImpl(@Assisted HtmlContainer container, @Assisted WmsLayer layer, 
			@Assisted double scale) {
		this.container = container;
		this.layer = layer;
		this.scale = scale;

		this.tiles = new HashMap<String, RasterTile>();
	}

	public double getScale() {
		return scale;
	}

	public HtmlContainer getHtmlContainer() {
		return container;
	}

	public void render(Bbox bounds) {
		List<TileCode> tilesForBounds = tileService.getTileCodesForBounds(layer.getViewPort(), layer.getTileConfig(),
				bounds, scale);
		for (TileCode tileCode : tilesForBounds) {
			if (!tiles.containsKey(tileCode.toString())) {
				RasterTile tile = createTile(tileCode);

				// Add the tile to the list and render it:
				tiles.put(tileCode.toString(), tile);
				nrLoadingTiles++;
				renderTile(tile, new ImageCounter());
			}
		}
	}

	@Override
	public List<RasterTile> getTiles(Bbox worldBounds) {
		List<RasterTile> result = new ArrayList<RasterTile>();
		for (RasterTile rasterTile : tiles.values()) {
			Bbox screenBounds = getScreenBounds(worldBounds);
			if (BboxService.intersects(screenBounds, rasterTile.getBounds())) {
				result.add(rasterTile);
			}
		}
		return result;
	}

	public void cancel() {
		nrLoadingTiles = 0;
	}

	public boolean isRendered() {
		return nrLoadingTiles == 0;
	}

	public void onTilesRendered(HtmlContainer container, double scale) {
		eventBus.fireEventFromSource(new ScaleLevelRenderedEvent(scale), this);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected void renderTile(RasterTile tile, Callback<String, String> callback) {
		HtmlImage image = htmlImageFactory.create(tile.getUrl(), tile.getBounds(), callback);
		if (layer instanceof OpacitySupported) {
			image.setOpacity(((OpacitySupported) layer).getOpacity());
		}
		container.add(image);
	}

	private RasterTile createTile(TileCode tileCode) {
		Bbox worldBounds = tileService.getWorldBoundsForTile(layer.getViewPort(), layer.getTileConfig(), tileCode);
		RasterTile tile = new RasterTile(getScreenBounds(worldBounds), tileCode.toString());
		tile.setCode(tileCode);
		tile.setUrl(wmsService.getMapUrl(layer.getConfig(), layer.getCrs(), worldBounds, layer.getTileConfig()
				.getTileWidth(), layer.getTileConfig().getTileHeight()));
		return tile;
	}

	private Bbox getScreenBounds(Bbox worldBox) {
		return new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale * worldBox.getMaxY()), Math.round(scale
				* worldBox.getMaxX())
				- Math.round(scale * worldBox.getX()), Math.round(scale * worldBox.getMaxY())
				- Math.round(scale * worldBox.getY()));
	}

	/**
	 * Counts the number of images that are still inbound. If all images are effectively rendered, we call
	 * {@link #onTilesRendered}.
	 * 
	 * @author Pieter De Graef
	 */
	private class ImageCounter implements Callback<String, String> {

		// In case of failure, we can't just sit and wait. Instead we immediately consider the scale level rendered.
		public void onFailure(String reason) {
			onTilesRendered(container, scale);
		}

		public void onSuccess(String result) {
			if (nrLoadingTiles > 0) { // A cancel may have reset the number of loading tiles.
				nrLoadingTiles--;
				if (nrLoadingTiles == 0) {
					onTilesRendered(container, scale);
				}
			}
		}
	}
}