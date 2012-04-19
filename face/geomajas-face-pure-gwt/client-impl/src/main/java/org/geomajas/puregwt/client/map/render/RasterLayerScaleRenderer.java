/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.HtmlImage;
import org.geomajas.puregwt.client.gfx.HtmlImageFactory;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.map.render.event.ScaleLevelRenderedEvent;
import org.geomajas.puregwt.client.service.CommandService;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

/**
 * <p>
 * Presenter for a certain fixed scale level of a {@link RasterLayer}. It retrieves, stores and displays raster tiles
 * for a single raster layer at a single scale level.
 * </p>
 * <p>
 * Rendering new or extra tiles is a 2-step process: first the correct list of tiles is fetched, then their images are
 * retrieved. Both steps end in a call to an abstract method. For fetching tiles, this is "onTilesReceived"
 * </p>
 * 
 * @author Pieter De Graef
 */
public class RasterLayerScaleRenderer implements TiledScaleRenderer {

	private CommandService commandService;

	private HtmlImageFactory htmlImageFactory;

	private EventBus eventBus;

	private final String crs;

	private final RasterLayer rasterLayer;

	private final HtmlContainer container;

	private final double scale;

	private final Map<TileCode, RasterTile> tiles;

	// Settings and status variables:

	private double mapExtentScaleAtFetch = 2;

	private Deferred deferred;

	private Bbox currentTileBounds;

	private int nrLoadingTiles;

	private boolean renderingImages;

	private Object eventSource;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	@Inject
	public RasterLayerScaleRenderer(EventBus eventBus, CommandService commandService,
			HtmlImageFactory htmlImageFactory, @Assisted Object eventSource, @Assisted String crs,
			@Assisted RasterLayer rasterLayer, @Assisted HtmlContainer container, @Assisted double scale) {
		this.eventBus = eventBus;
		this.commandService = commandService;
		this.htmlImageFactory = htmlImageFactory;
		this.eventSource = eventSource;
		this.crs = crs;
		this.rasterLayer = rasterLayer;
		this.container = container;
		this.scale = scale;
		tiles = new HashMap<TileCode, RasterTile>();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onTilesRendered(HtmlContainer container, double scale) {
		eventBus.fireEventFromSource(new ScaleLevelRenderedEvent(scale), eventSource);
	}

	/** {@inheritDoc} */
	public void cancel() {
		if (deferred != null) {
			deferred.cancel();
			deferred = null;
			currentTileBounds = null;
		}
	}

	/** {@inheritDoc} */
	public double getScale() {
		return scale;
	}

	/** {@inheritDoc} */
	public void render(final Bbox bounds) {
		// First we check whether or not the requested bounds is already rendered:
		if (currentTileBounds != null && BboxService.contains(currentTileBounds, bounds)) {
			onTilesRendered(container, scale);
			return; // Bounds already rendered, nothing to do here.
		}

		// Scale the bounds to fetch tiles for (we want a bigger area than the map bounds):
		currentTileBounds = BboxService.scale(bounds, mapExtentScaleAtFetch);

		// Create the command:
		GetRasterTilesRequest request = new GetRasterTilesRequest();
		request.setBbox(new org.geomajas.geometry.Bbox(currentTileBounds.getX(), currentTileBounds.getY(),
				currentTileBounds.getWidth(), currentTileBounds.getHeight()));
		request.setCrs(crs);
		request.setLayerId(rasterLayer.getServerLayerId());
		request.setScale(scale);
		GwtCommand command = new GwtCommand(GetRasterTilesRequest.COMMAND);
		command.setCommandRequest(request);

		// Execute the fetch, and render on success:
		deferred = commandService.execute(command, new AbstractCommandCallback<GetRasterTilesResponse>() {

			public void execute(GetRasterTilesResponse response) {
				addTiles(response.getRasterData());
			}
		});
	}

	public boolean isRendered() {
		return nrLoadingTiles > 0;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public boolean isFetchingTiles() {
		return deferred != null;
	}

	public boolean isRenderingImages() {
		return renderingImages;
	}

	public HtmlContainer getHtmlContainer() {
		return container;
	}

	public double getMapExtentScaleAtFetch() {
		return mapExtentScaleAtFetch;
	}

	public void setMapExtentScaleAtFetch(double mapExtentScaleAtFetch) {
		this.mapExtentScaleAtFetch = mapExtentScaleAtFetch;
	}

	// ------------------------------------------------------------------------
	// Private methods and classes:
	// ------------------------------------------------------------------------

	protected void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles) {
		nrLoadingTiles = 0;
		for (RasterTile tile : rasterTiles) {
			TileCode code = tile.getCode().clone();

			// Add only new tiles to the list:
			if (!tiles.containsKey(code)) {
				nrLoadingTiles++;
				// Add the tile to the list and render it:
				tiles.put(code, tile);
				renderTile(tile, new ImageCounter());
			}
		}
		deferred = null;
		renderingImages = true;
	}

	protected void renderTile(RasterTile tile, Callback<String, String> callback) {
		HtmlImage image = htmlImageFactory.create(tile.getUrl(), tile.getBounds(), callback);
		image.setOpacity(rasterLayer.getOpacity());
		container.add(image);
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
			nrLoadingTiles--;
			if (nrLoadingTiles == 0) {
				onTilesRendered(container, scale);
			}
		}
	}
}