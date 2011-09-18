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
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.util.Log;
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
import org.geomajas.puregwt.client.map.gfx.HtmlGroup;
import org.geomajas.puregwt.client.map.gfx.HtmlImageImpl;
import org.geomajas.puregwt.client.map.gfx.HtmlObject;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.geomajas.puregwt.client.service.BooleanCallback;
import org.geomajas.puregwt.client.spatial.Bbox;

/**
 * <p>
 * MapRenderer implementation that specifically works on a single raster layer.
 * </p>
 * Note that this renderer expects the first event to be a zooming type of event, otherwise a NullPointerException will
 * occur.
 * 
 * @author Pieter De Graef
 */
public class SmartRasterLayerRenderer implements MapRenderer {

	private ViewPort viewPort;

	private RasterLayer rasterLayer;

	/** The container that should render all images. */
	private HtmlContainer htmlContainer;

	private double mapExtentScaleAtFetch = 2;

	private Map<TileCode, RasterTile> tiles = new HashMap<TileCode, RasterTile>();

	private Deferred deferred;

	private Bbox currentTileBounds;

	private GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();

	// Set of parameters stored for zooming consecutively:

	private double beginScale;

	private Coordinate beginOrigin;

	private boolean renderDelayed;

	// Set of parameters that keep track of rendering status:

	private int nrLoadingTiles;

	private boolean busyRendering;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	protected SmartRasterLayerRenderer(ViewPort viewPort, RasterLayer rasterLayer) {
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
		beginScale = viewPort.getScale();
		beginOrigin = viewPort.getPosition();
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		if (event.getLayer().getId().equals(rasterLayer.getId())) {
			fetchTiles(viewPort.getBounds(), true);
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
	// MapCompositionHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerAdded(LayerAddedEvent event) {
		RasterLayer layer = (RasterLayer) event.getLayer();
		htmlContainer.setVisible(layer.getLayerInfo().isVisible());
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
	}

	// ------------------------------------------------------------------------
	// MapRenderer implementation:
	// ------------------------------------------------------------------------

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		// Does nothing...
	}

	public void onViewPortChanged(ViewPortChangedEvent event) {
		fetchTiles(event.getViewPort().getBounds(), true);
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		fetchTiles(event.getViewPort().getBounds(), true);
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		if (currentTileBounds == null || !currentTileBounds.contains(event.getViewPort().getBounds())) {
			beginOrigin = viewPort.getPosition();
			fetchTiles(event.getViewPort().getBounds(), false);
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

	public void setMapExtentScaleAtFetch(double mapExentScaleAtFetch) {
		if (mapExentScaleAtFetch >= 1 && mapExentScaleAtFetch < 10) {
			this.mapExtentScaleAtFetch = mapExentScaleAtFetch;
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
	// Getters and setters:
	// ------------------------------------------------------------------------

	public boolean isRenderDelayed() {
		return renderDelayed;
	}

	public void setRenderDelayed(boolean renderDelayed) {
		this.renderDelayed = renderDelayed;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Fetch tiles and make sure they are rendered when the response returns.
	 *
	 * @param bounds bounds to fetch tiles for
	 * @param zooming is the user zooming?
	 */
	private void fetchTiles(final Bbox bounds, final boolean zooming) {
		// Are we still busy loading a previous batch? Than clean that up and create a new temporary rendering.
		if (busyRendering && zooming) {
			cleanupTempRendering();
		}
		busyRendering = true;

		// While we're waiting for the response to return and all images to load, fake zoom the current tiles:
		if (zooming) {
			fakeZoom(viewPort.getScale());
		}

		// Scale the bounds to fetch tiles for:
		currentTileBounds = bounds.scale(mapExtentScaleAtFetch);

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
				addTiles(response.getRasterData(), zooming);
			}
		});
	}

	/**
	 * Add tiles to the list and render them on the map.
	 *
	 * @param rasterTiles tiles to add/render
	 * @param zooming are we zooming
	 */
	private void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles, boolean zooming) {
		// Go over all tiles we got back from the server:
		nrLoadingTiles = 0;
		for (RasterTile tile : rasterTiles) {
			TileCode code = tile.getCode().clone();

			// Add only new tiles to the list:
			if (!tiles.containsKey(code)) {
				nrLoadingTiles++;

				// Give the tile the correct location, keeping panning in mind:
				tile.getBounds().setX(tile.getBounds().getX());
				tile.getBounds().setY(tile.getBounds().getY());

				// Add the tile to the list and render it:
				tiles.put(code, tile);
				HtmlImageImpl image = new HtmlImageImpl(tile.getUrl(), (int) Math.round(tile.getBounds().getWidth()),
						(int) Math.round(tile.getBounds().getHeight()), (int) Math.round(tile.getBounds().getY()),
						(int) Math.round(tile.getBounds().getX()), new ImageCounter(zooming));
				image.setOpacity(rasterLayer.getOpacity());
				if (zooming) {
					getTopContainer().add(image);
				} else {
					getBottomContainer().add(image);
				}
			}
		}
		deferred = null;
	}

	private void cleanupTempRendering() {
		while (htmlContainer.getChildCount() > 1) {
			htmlContainer.remove(getTopContainer());
		}
		tiles.clear();
		if (deferred != null) {
			deferred.cancel();
		}
	}

	private void resetRendering() {
		busyRendering = false;
		getTopContainer().setVisible(true);
		beginScale = viewPort.getScale();
		beginOrigin = viewPort.getPosition();
		if (htmlContainer.getChildCount() > 1) {
			htmlContainer.remove(getBottomContainer());
		} else {
			Log.logWarn("WARN: Trying to remove bottom container when there is no top container...");
		}
	}

	private void fakeZoom(double scale) {
		HtmlContainer container = getBottomContainer();
		if (container == null || beginOrigin == null) {
			getTopContainer();
			return;
		}

		// Zoom in:
		container.zoomToLocation(scale / beginScale, 0, 0);
	}

	private HtmlContainer getTopContainer() {
		if (htmlContainer.getChildCount() < 2) {
			HtmlContainer tempContainer = new HtmlGroup();
			if (renderDelayed) {
				tempContainer.setVisible(false);
			}
			htmlContainer.add(tempContainer);
			return tempContainer;
		}
		return (HtmlContainer) htmlContainer.getChild(1);
	}

	private HtmlContainer getBottomContainer() {
		if (htmlContainer.getChildCount() == 0) {
			return null;
		}
		return (HtmlContainer) htmlContainer.getChild(0);
	}

	/**
	 * Call-back that counts the total amount of images that have been loaded. When all images are loaded, it cleans up
	 * older renderings if there are any. That depends on whether or not we're zooming.
	 * 
	 * @author Pieter De Graef
	 */
	public class ImageCounter implements BooleanCallback {

		private boolean zooming;

		public ImageCounter(boolean zooming) {
			this.zooming = zooming;
		}

		public void execute(Boolean value) {
			nrLoadingTiles--;
			if (nrLoadingTiles == 0) {
				if (zooming) {
					resetRendering();
				} else {
					busyRendering = false;
					getBottomContainer().setVisible(true);
				}
			}
		}
	}
}