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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.layer.tile.TileCode;
import org.geomajas.puregwt.client.map.MapRenderer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.LayerAddedEvent;
import org.geomajas.puregwt.client.map.event.LayerHideEvent;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.map.event.LayerShowEvent;
import org.geomajas.puregwt.client.map.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.map.event.MapResizedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent;
import org.geomajas.puregwt.client.map.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.layer.TilePresenter.STATUS;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.geomajas.puregwt.client.spatial.GeometryFactoryImpl;

/**
 * MapRenderer implementation that renders a single vector layer.
 * 
 * @author Pieter De Graef
 */
public class VectorLayerRenderer implements MapRenderer {

	private VectorLayer layer;

	private ViewPort viewPort;

	private EventBus eventBus;

	private HtmlContainer htmlContainer;

	private VectorContainer vectorContainer;

	private GeometryFactory factory;

	private Map<String, TilePresenter> tiles;

	private Bbox layerBounds;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public VectorLayerRenderer(VectorLayer layer, ViewPort viewPort) {
		this.layer = layer;
		this.viewPort = viewPort;

		factory = new GeometryFactoryImpl();
		layerBounds = factory.createBbox(layer.getLayerInfo().getMaxExtent());
		tiles = new HashMap<String, TilePresenter>();
	}

	// ------------------------------------------------------------------------
	// MapResizedHandler implementation:
	// ------------------------------------------------------------------------

	public void onMapResized(MapResizedEvent event) {
		// TODO implement me...
	}

	// ------------------------------------------------------------------------
	// MapCompositionHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerAdded(LayerAddedEvent event) {
		VectorLayer layer = (VectorLayer) event.getLayer();
		vectorContainer.setVisible(layer.getLayerInfo().isVisible());
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	public void render(Bbox bbox) {
		// Only fetch when inside the layer bounds:
		if (bbox.intersects(layerBounds)) {

			// Find needed tile codes:
			List<TileCode> tempCodes = calcCodesForBounds(bbox);
			for (TileCode tileCode : tempCodes) {

				TilePresenter tilePresenter = tiles.get(tileCode.toString());
				if (tilePresenter == null) {
					// New tile
					tilePresenter = addTile(tileCode);
					tilePresenter.render();
				} else if (tilePresenter.getSiblingStatus() == STATUS.EMPTY) {
					// Tile already exists, but the siblings have not yet been loaded:
					tilePresenter.renderSiblings();
				}
			}
		}
	}

	public TilePresenter addTile(TileCode tileCode) {
		TilePresenter tilePresenter = tiles.get(tileCode.toString());
		if (tilePresenter == null) {
			tilePresenter = new TilePresenter(this, tileCode.clone());
			tiles.put(tileCode.toString(), tilePresenter);
		}
		return tilePresenter;
	}

	public TilePresenter getTile(TileCode tileCode) {
		return tiles.get(tileCode.toString());
	}

	// ------------------------------------------------------------------------
	// LayerStyleChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerStyleChanged(LayerStyleChangedEvent event) {
		if (event.getLayer().getId().equals(layer.getId())) {
			// TODO implement me.
		}
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler Implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		if (event.getLayer().getId().equals(layer.getId())) {
			if (htmlContainer != null) {
				htmlContainer.setVisible(true);
			}
			if (vectorContainer != null) {
				vectorContainer.setVisible(true);
			}
			render(viewPort.getBounds());
		}
	}

	public void onHide(LayerHideEvent event) {
		if (event.getLayer().getId().equals(layer.getId())) {
			if (htmlContainer != null) {
				htmlContainer.setVisible(false);
			}
			if (vectorContainer != null) {
				vectorContainer.setVisible(false);
			}
		}
	}

	// ------------------------------------------------------------------------
	// MapRenderer Implementation:
	// ------------------------------------------------------------------------

	public void onViewPortChanged(ViewPortChangedEvent event) {
		clear();
		render(event.getViewPort().getBounds());
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		clear();
		render(event.getViewPort().getBounds());
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		render(event.getViewPort().getBounds());
	}

	public void onViewPortDragged(ViewPortDraggedEvent event) {
		render(event.getViewPort().getBounds());
	}

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		// Do nothing
	}

	public void setMapExentScaleAtFetch(double scale) {
		// TODO Not implemented...
	}

	public void clear() {
		for (TilePresenter tilePresenter : tiles.values()) {
			tilePresenter.cancel();
		}
		tiles.clear();
		if (htmlContainer != null) {
			htmlContainer.clear();
		}
		if (vectorContainer != null) {
			vectorContainer.clear();
		}
	}

	public void setHtmlContainer(HtmlContainer htmlContainer) {
		this.htmlContainer = htmlContainer;
	}

	public HtmlContainer getHtmlContainer() {
		return htmlContainer;
	}

	public void setVectorContainer(VectorContainer vectorContainer) {
		this.vectorContainer = vectorContainer;
	}

	public VectorContainer getVectorContainer() {
		return vectorContainer;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public ViewPort getViewPort() {
		return viewPort;
	}

	public VectorLayer getLayer() {
		return layer;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Saves the complete array of TileCode objects for the given bounds (and the current scale).
	 * 
	 * @param bounds
	 *            view bounds
	 * @return list of tiles in these bounds
	 */
	private List<TileCode> calcCodesForBounds(Bbox bounds) {
		int currentTileLevel = calculateTileLevel(bounds);

		// Calculate tile width and height for tileLevel=currentTileLevel
		double div = Math.pow(2, currentTileLevel); // tile level must be correct!
		double scale = viewPort.getScale();
		double tileWidth = Math.ceil((scale * layerBounds.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * layerBounds.getHeight()) / div) / scale;

		// For safety (to prevent division by 0):
		List<TileCode> codes = new ArrayList<TileCode>();
		if (tileWidth == 0 || tileHeight == 0) {
			return codes;
		}

		// Calculate bounds relative to extents:
		Bbox clippedBounds = bounds.intersection(layerBounds);
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
	 * @param bounds
	 *            view bounds
	 * @return best tile level for view bounds
	 */
	private int calculateTileLevel(Bbox bounds) {
		double baseX = layerBounds.getWidth();
		double baseY = layerBounds.getHeight();
		// choose the tile level so the area is between 256*256 and 512*512 pixels
		double baseArea = baseX * baseY;
		double scale = viewPort.getScale();
		double osmArea = 256 * 256 / (scale * scale);
		int tileLevel = (int) Math.floor(Math.log(baseArea / osmArea) / Math.log(4.0));
		if (tileLevel < 0) {
			tileLevel = 0;
		}
		return tileLevel;
	}
}