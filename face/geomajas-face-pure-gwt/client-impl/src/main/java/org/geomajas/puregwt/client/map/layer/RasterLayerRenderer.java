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
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerShowEvent;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedHandler;
import org.geomajas.puregwt.client.map.event.LayerVisibleHandler;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.HtmlImage;
import org.geomajas.puregwt.client.map.gfx.HtmlObject;
import org.geomajas.puregwt.client.spatial.Bbox;

/**
 * <p>
 * MapRenderer implementation that specifically works on a single raster layer.
 * </p>
 * Pretty experimental for now...
 * 
 * @author Pieter De Graef
 */
public class RasterLayerRenderer implements MapRenderer, LayerStyleChangedHandler, LayerVisibleHandler {

	/** The container that should render all images. */
	private HtmlContainer htmlContainer;

	private RasterLayer rasterLayer;

	private double mapExentScaleAtFetch = 2;

	private Map<TileCode, RasterTile> tiles = new HashMap<TileCode, RasterTile>();

	private Deferred deferred;

	private Bbox currentTileBounds;

	// TODO inject this with GIN
	private CommandService commandService = new CommandService();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected RasterLayerRenderer(RasterLayer rasterLayer) {
		this.rasterLayer = rasterLayer;
		rasterLayer.getMapModel().getEventBus().addHandler(LayerStyleChangedHandler.TYPE, this);
		rasterLayer.getMapModel().getEventBus().addHandler(LayerVisibleHandler.TYPE, this);
	}

	// ------------------------------------------------------------------------
	// LayerStyleChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerStyleChanged(LayerStyleChangedEvent event) {
		if (event.getLayer().getId().equals(rasterLayer.getId())) {
			for (int i = 0; i < htmlContainer.getChildCount(); i++) {
				HtmlObject htmlObject = htmlContainer.getChild(i);
				htmlObject.setOpacity(rasterLayer.getOpacity());
			}
		}
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		if (event.getLayer().getId().equals(rasterLayer.getId())) {
			htmlContainer.show();
		}
	}

	public void onHide(LayerHideEvent event) {
		if (event.getLayer().getId().equals(rasterLayer.getId())) {
			htmlContainer.hide();
		}
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
		if (currentTileBounds == null || !currentTileBounds.contains(event.getViewPort().getBounds())) {
			fetchTiles(event.getViewPort().getBounds());
		}
	}

	public void onViewPortDragged(ViewPortDraggedEvent event) {
		if (currentTileBounds == null || !currentTileBounds.contains(event.getViewPort().getBounds())) {
			fetchTiles(event.getViewPort().getBounds());
		}
	}

	public void clear() {
		currentTileBounds = null;
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

	/** Fetch tiles and make sure they are rendered when the response returns. */
	private void fetchTiles(final Bbox bounds) {
		// Scale the bounds to fetch tiles for:
		currentTileBounds = bounds.scale(mapExentScaleAtFetch);

		// Create the command:
		GetRasterTilesRequest request = new GetRasterTilesRequest();
		request.setBbox(new org.geomajas.geometry.Bbox(currentTileBounds.getX(), currentTileBounds.getY(),
				currentTileBounds.getWidth(), currentTileBounds.getHeight()));
		request.setCrs(rasterLayer.getMapModel().getEpsg());
		request.setLayerId(rasterLayer.getServerLayerId());
		request.setScale(rasterLayer.getMapModel().getViewPort().getScale());
		Command command = new Command("command.render.GetRasterTiles");
		command.setCommandRequest(request);

		// Execute the fetch, and render on success:
		deferred = commandService.execute(command, new CommandCallback() {

			public void onSuccess(CommandResponse response) {
				if (response instanceof GetRasterTilesResponse) {
					addTiles(((GetRasterTilesResponse) response).getRasterData());
				}
			}

			public void onFailure(Throwable error) {
			}
		});
	}

	/** Add tiles to the list and render them on the map. */
	private void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles) {
		ViewPortImpl viewPort = (ViewPortImpl) rasterLayer.getMapModel().getViewPort();
		Coordinate delta = viewPort.getWorldToPanTranslation();

		// Go over all tiles we got back from the server:
		for (RasterTile tile : rasterTiles) {
			TileCode code = tile.getCode().clone();

			// Add only new tiles to the list:
			if (!tiles.containsKey(code)) {
				// Give the tile the correct location, keeping panning in mind:
				tile.getBounds().setX(tile.getBounds().getX() + delta.getX());
				tile.getBounds().setY(tile.getBounds().getY() + delta.getY());

				// Add the tile to the list and render it:
				tiles.put(code, tile);
				HtmlImage image = new HtmlImage(tile.getUrl(), (int) Math.round(tile.getBounds().getWidth()),
						(int) Math.round(tile.getBounds().getHeight()), (int) Math.round(tile.getBounds().getY()),
						(int) Math.round(tile.getBounds().getX()));
				image.setOpacity(rasterLayer.getOpacity());
				htmlContainer.add(image);
			}
		}
		deferred = null;
	}
}