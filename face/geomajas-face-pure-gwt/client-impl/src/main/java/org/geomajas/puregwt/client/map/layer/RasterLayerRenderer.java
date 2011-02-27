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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.puregwt.client.command.Command;
import org.geomajas.puregwt.client.command.CommandCallback;
import org.geomajas.puregwt.client.command.CommandService;
import org.geomajas.puregwt.client.command.Deferred;
import org.geomajas.puregwt.client.map.MapRenderer;
import org.geomajas.puregwt.client.map.ViewPortImpl;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.HtmlImage;
import org.geomajas.puregwt.client.spatial.Bbox;

/**
 * <p>
 * MapRenderer implementation that specifically works on a single raster layer.
 * </p>
 * Pretty experimental for now...
 * 
 * @author Pieter De Graef
 */
public class RasterLayerRenderer implements MapRenderer {

	private HtmlContainer htmlContainer;

	private RasterLayer rasterLayer;

	private double mapExentScaleAtFetch = 2;

	private Map<TileCode, RasterTile> tiles = new HashMap<TileCode, RasterTile>();

	private Deferred deferred;

	private CommandService commandService = new CommandService();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected RasterLayerRenderer(RasterLayer rasterLayer) {
		this.rasterLayer = rasterLayer;
	}

	// ------------------------------------------------------------------------
	// MapRenderer implementation:
	// ------------------------------------------------------------------------

	public void onViewPortChanged(ViewPortChangedEvent event) {
		clear();
		fetchTiles(event.getViewPort().getBounds());
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		clear();
		fetchTiles(event.getViewPort().getBounds());
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		fetchTiles(event.getViewPort().getBounds());
	}

	public void onViewPortDragged(ViewPortDraggedEvent event) {
		// Do some smart fetching...
	}

	public void clear() {
		htmlContainer.clear();
		if (tiles.size() > 0) {
			tiles.clear();
		}
		if (deferred != null) {
			deferred.cancel();
		}
	}

	public void redraw() {
		clear();
		onViewPortChanged(new ViewPortChangedEvent(rasterLayer.getMapModel().getViewPort()));
	}

	public void setMapExentScaleAtFetch(double mapExentScaleAtFetch) {
		if (mapExentScaleAtFetch >= 1 && mapExentScaleAtFetch < 10) {
			this.mapExentScaleAtFetch = mapExentScaleAtFetch;
		} else {
			throw new IllegalArgumentException("The 'setMapExentScaleAtFetch' method on the MapRender allows"
					+ " only values between 1 and 10.");
		}
	}

	public void setHtmlContainer(HtmlContainer htmlContainer) {
		this.htmlContainer = htmlContainer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void fetchTiles(final Bbox bounds) {
		// Scale the bounds to fetch tiles for:
		Bbox tileBounds = bounds.scale(mapExentScaleAtFetch);

		// Create the command:
		GetRasterTilesRequest request = new GetRasterTilesRequest();
		request.setBbox(new org.geomajas.geometry.Bbox(tileBounds.getX(), tileBounds.getY(), tileBounds.getWidth(),
				tileBounds.getHeight()));
		request.setCrs(rasterLayer.getMapModel().getEpsg());
		request.setLayerId(rasterLayer.getServerLayerId());
		request.setScale(rasterLayer.getMapModel().getViewPort().getScale());
		Command command = new Command("command.render.GetRasterTiles");
		command.setCommandRequest(request);

		// Execute the fetch, and render on success:
		deferred = commandService.execute(command, new CommandCallback() {

			public void onSuccess(CommandResponse response) {
				GetRasterTilesResponse r = (GetRasterTilesResponse) response;
				addTiles(r.getRasterData());
				for (RasterTile tile : tiles.values()) {
					htmlContainer.add(new HtmlImage(tile.getUrl(), (int) Math.round(tile.getBounds().getWidth()),
							(int) Math.round(tile.getBounds().getHeight()), (int) Math.round(tile.getBounds().getY()),
							(int) Math.round(tile.getBounds().getX())));
				}
			}

			public void onFailure(Throwable error) {
			}
		});
	}

	private void addTiles(List<org.geomajas.layer.tile.RasterTile> images) {
		ViewPortImpl viewPort = (ViewPortImpl) rasterLayer.getMapModel().getViewPort();
		Coordinate delta = viewPort.getWorldToPanTranslation();

		// Go over all tiles we got back from the server. Add only new tiles.
		for (RasterTile image : images) {
			TileCode code = image.getCode().clone();
			if (!tiles.containsKey(code)) {
				image.getBounds().setX(image.getBounds().getX() + delta.getX());
				image.getBounds().setY(image.getBounds().getY() + delta.getY());
				tiles.put(code, image);
			}
		}
		deferred = null;
	}
}