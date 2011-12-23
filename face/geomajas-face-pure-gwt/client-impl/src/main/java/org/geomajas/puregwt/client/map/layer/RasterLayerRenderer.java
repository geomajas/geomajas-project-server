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

import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.puregwt.client.map.MapRenderer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.event.LayerAddedEvent;
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.map.event.LayerShowEvent;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.map.event.MapResizedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.HtmlImageImpl;
import org.geomajas.puregwt.client.map.gfx.HtmlObject;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;

/**
 * <p>
 * MapRenderer implementation that specifically works on a single raster layer.
 * </p>
 * Pretty experimental for now...
 * 
 * @author Pieter De Graef
 */
public class RasterLayerRenderer implements MapRenderer {

	private ViewPort viewPort;

	private RasterLayer rasterLayer;

	/** The container that should render all images. */
	private HtmlContainer htmlContainer;

	private double mapExtentScaleAtFetch = 2;

	private Map<TileCode, RasterTile> tiles = new HashMap<TileCode, RasterTile>();

	private Deferred deferred;

	private Bbox currentTileBounds;

	private GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected RasterLayerRenderer(ViewPort viewPort, RasterLayer rasterLayer) {
		this.viewPort = viewPort;
		this.rasterLayer = rasterLayer;
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
	// MapResizedHandler implementation:
	// ------------------------------------------------------------------------

	public void onMapResized(MapResizedEvent event) {
		fetchTiles(viewPort.getBounds());
	}

	// ------------------------------------------------------------------------
	// MapCompositionHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerAdded(LayerAddedEvent event) {
		RasterLayer layer = (RasterLayer) event.getLayer();
		htmlContainer.setVisible(layer.getLayerInfo().isVisible());
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		if (event.getLayer().getId().equals(rasterLayer.getId())) {
			fetchTiles(viewPort.getBounds());
			htmlContainer.setVisible(true);
		}
	}

	public void onHide(LayerHideEvent event) {
		if (event.getLayer().getId().equals(rasterLayer.getId())) {
			htmlContainer.setVisible(false);
		}
	}

	public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
	}

	// ------------------------------------------------------------------------
	// MapRenderer implementation:
	// ------------------------------------------------------------------------

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		// Does nothing...
	}

	public void onViewPortChanged(ViewPortChangedEvent event) {
		clear();
		fetchTiles(event.getViewPort().getBounds());
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		clear();
		fetchTiles(event.getViewPort().getBounds());
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		if (currentTileBounds == null || !BboxService.contains(currentTileBounds, event.getViewPort().getBounds())) {
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

	public void setMapExtentScaleAtFetch(double mapExtentScaleAtFetch) {
		if (mapExtentScaleAtFetch >= 1 && mapExtentScaleAtFetch < 10) {
			this.mapExtentScaleAtFetch = mapExtentScaleAtFetch;
		} else {
			throw new IllegalArgumentException("The 'setMapExtentScaleAtFetch' method on the MapRender allows"
					+ " only values between 1 and 10.");
		}
	}

	public void setHtmlContainer(HtmlContainer htmlContainer) {
		this.htmlContainer = htmlContainer;
	}

	public void setVectorContainer(VectorContainer vectorContainer) {
		// This renderer doesn't support vector rendering.
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Fetch tiles and make sure they are rendered when the response returns.
	 * 
	 * @param bounds
	 *            bounds to fetch tiles for
	 */
	private void fetchTiles(final Bbox bounds) {
		// Scale the bounds to fetch tiles for:
		currentTileBounds = BboxService.scale(bounds, mapExtentScaleAtFetch);

		// Create the command:
		GetRasterTilesRequest request = new GetRasterTilesRequest();
		request.setBbox(new org.geomajas.geometry.Bbox(currentTileBounds.getX(), currentTileBounds.getY(),
				currentTileBounds.getWidth(), currentTileBounds.getHeight()));
		request.setCrs(viewPort.getCrs());
		request.setLayerId(rasterLayer.getServerLayerId());
		request.setScale(viewPort.getScale());
		GwtCommand command = new GwtCommand(GetRasterTilesRequest.COMMAND);
		command.setCommandRequest(request);

		// Execute the fetch, and render on success:
		deferred = dispatcher.execute(command, new AbstractCommandCallback<GetRasterTilesResponse>() {

			public void execute(GetRasterTilesResponse response) {
				addTiles((response).getRasterData());
			}
		});
	}

	/**
	 * Add tiles to the list and render them on the map.
	 * 
	 * @param rasterTiles
	 *            tiles to add/render
	 */
	private void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles) {
		// Go over all tiles we got back from the server:
		for (RasterTile tile : rasterTiles) {
			TileCode code = tile.getCode().clone();

			// Add only new tiles to the list:
			if (!tiles.containsKey(code)) {
				// Give the tile the correct location, keeping panning in mind:
				tile.getBounds().setX(tile.getBounds().getX());
				tile.getBounds().setY(tile.getBounds().getY());

				// Add the tile to the list and render it:
				tiles.put(code, tile);
				HtmlImageImpl image = new HtmlImageImpl(tile.getUrl(), (int) Math.round(tile.getBounds().getWidth()),
						(int) Math.round(tile.getBounds().getHeight()), (int) Math.round(tile.getBounds().getY()),
						(int) Math.round(tile.getBounds().getX()));
				image.setOpacity(rasterLayer.getOpacity());
				htmlContainer.add(image);
			}
		}
		deferred = null;
	}
}