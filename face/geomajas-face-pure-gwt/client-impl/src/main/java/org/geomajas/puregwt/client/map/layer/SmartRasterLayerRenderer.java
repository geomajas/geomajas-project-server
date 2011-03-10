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
import org.geomajas.puregwt.client.map.AbstractMapRenderer;
import org.geomajas.puregwt.client.map.MapModel;
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedEvent;
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
import org.geomajas.puregwt.client.service.BooleanCallback;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.Matrix;

import com.google.gwt.user.client.DOM;

/**
 * <p>
 * MapRenderer implementation that specifically works on a single raster layer.
 * </p>
 * Pretty experimental for now...
 * 
 * @author Pieter De Graef
 */
public class SmartRasterLayerRenderer extends AbstractMapRenderer implements LayerStyleChangedHandler,
		LayerVisibleHandler {

	/** The container that should render all images. */
	private HtmlContainer htmlContainer;

	private RasterLayer rasterLayer;

	private double mapExentScaleAtFetch = 2;

	private Map<TileCode, RasterTile> tiles = new HashMap<TileCode, RasterTile>();

	private Deferred deferred;

	private Bbox currentTileBounds;

	private CommandService commandService = new CommandService();

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

	protected SmartRasterLayerRenderer(MapModel mapModel, RasterLayer rasterLayer) {
		super(mapModel);
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
			fetchTiles(event.getViewPort().getBounds(), false);
		}
	}

	public void onViewPortDragged(ViewPortDraggedEvent event) {
		if (currentTileBounds == null || !currentTileBounds.contains(event.getViewPort().getBounds())) {
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

	/** Fetch tiles and make sure they are rendered when the response returns. */
	private void fetchTiles(final Bbox bounds, final boolean zooming) {
		// Are we still busy loading a previous batch? Than clean that up and create a new temporary rendering.
		if (busyRendering && zooming) {
			cleanupTempRendering();
		}
		busyRendering = true;

		// While we're waiting for the response to return and all images to load, fake zoom the current tiles:
		if (zooming) {
			fakeZoom(rasterLayer.getMapModel().getViewPort().getScale(), bounds);
		}

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
					addTiles(((GetRasterTilesResponse) response).getRasterData(), zooming);
				}
			}

			public void onFailure(Throwable error) {
			}
		});
	}

	/** Add tiles to the list and render them on the map. */
	private void addTiles(List<org.geomajas.layer.tile.RasterTile> rasterTiles, boolean zooming) {
		Matrix delta = getWorldToPanTranslation();

		// Go over all tiles we got back from the server:
		nrLoadingTiles = 0;
		for (RasterTile tile : rasterTiles) {
			TileCode code = tile.getCode().clone();

			// Add only new tiles to the list:
			if (!tiles.containsKey(code)) {
				nrLoadingTiles++;

				// Give the tile the correct location, keeping panning in mind:
				tile.getBounds().setX(tile.getBounds().getX() + delta.getDx());
				tile.getBounds().setY(tile.getBounds().getY() + delta.getDy());

				// Add the tile to the list and render it:
				tiles.put(code, tile);
				HtmlImage image = new HtmlImage(tile.getUrl(), (int) Math.round(tile.getBounds().getWidth()),
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
		beginScale = rasterLayer.getMapModel().getViewPort().getScale();
		beginOrigin = rasterLayer.getMapModel().getViewPort().getBounds().getCenterPoint();
		htmlContainer.remove(getBottomContainer());
	}

	private void fakeZoom(double scale, Bbox bounds) {
		HtmlContainer container = getBottomContainer();
		if (container == null) {
			return;
		}

		// Get the old center in pan space, we need this to know how far the center has moved:
		Coordinate beginPanOrigin = worldToPan(beginOrigin);

		// Compensate with zooming factor from the fake zoom (CSS3) - where the default value is 1.
		double zoomDiff = (scale / beginScale) - 1;

		// Coordinate transformed = calcTransformOrigin(scale, beginScale, beginOrigin);
		int deltaX = (int) (-beginPanOrigin.getX() / zoomDiff);
		int deltaY = (int) (-beginPanOrigin.getY() / zoomDiff);

		DOM.setStyleAttribute(container.getElement(), "MozTransform", "scale(" + (scale / beginScale) + ")");
		DOM.setStyleAttribute(container.getElement(), "MozTransformOrigin", deltaX + "px " + deltaY + "px");
		DOM.setStyleAttribute(container.getElement(), "WebkitTransform", "scale(" + (scale / beginScale) + ")");
		DOM.setStyleAttribute(container.getElement(), "WebkitTransformOrigin", deltaX + "px " + deltaY + "px");
		
		// TODO what about Internet Explorer?
	}

	private HtmlContainer getTopContainer() {
		if (htmlContainer.getChildCount() < 2) {
			HtmlContainer tempContainer = new HtmlContainer();
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