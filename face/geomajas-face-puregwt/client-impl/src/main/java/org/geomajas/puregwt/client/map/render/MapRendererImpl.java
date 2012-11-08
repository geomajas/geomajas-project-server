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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.event.LayerAddedEvent;
import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.event.LayerRefreshedEvent;
import org.geomajas.puregwt.client.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerStyleChangedEvent;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.MapResizedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.HtmlGroup;
import org.geomajas.puregwt.client.gfx.HtmlObject;
import org.geomajas.puregwt.client.map.LayersModel;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.map.layer.VectorLayer;
import org.geomajas.puregwt.client.map.render.event.ScaleLevelRenderedEvent;
import org.geomajas.puregwt.client.map.render.event.ScaleLevelRenderedHandler;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Generic map renderer implementation. Has support for animated navigation through the {@link MapNavigationAnimation}.
 * 
 * @author Pieter De Graef
 */
public class MapRendererImpl implements MapRenderer {

	private final LayersModel layersModel;

	private final ViewPort viewPort;

	private final Map<Layer<?>, MapScalesRenderer> layerRenderers;

	private final HtmlContainer htmlContainer;

	private final MapNavigationAnimation animation;

	private boolean first = true; // No animation the first time the map is rendered.

	// Keeping track of the fetch delay:

	private final FetchTimer fetchTimer;

	private int fetchDelay = 100; // Delay for fetching scale levels. Results in less requests.

	// Keeping track of the navigation animation:

	private int animationMillis = 400;

	private double previousScale;

	private double currentScale;

	private Coordinate previousTranslation;

	private boolean navigationBusy;
	
	@Inject
	private MapScalesRendererFactory mapScalesRendererFactory;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------
	@Inject
	public MapRendererImpl(@Assisted LayersModel layersModel, @Assisted final ViewPort viewPort,
			@Assisted HtmlContainer htmlContainer) {
		this.layersModel = layersModel;
		this.viewPort = viewPort;
		this.htmlContainer = htmlContainer;

		this.layerRenderers = new HashMap<Layer<?>, MapScalesRenderer>();
		this.animation = new MapNavigationAnimation(new LinearNavigationFunction()) {

			protected void onComplete() {
				super.onComplete();
				onNavigationComplete();
			};
		};

		previousScale = viewPort.getScale();
		currentScale = viewPort.getScale();
		previousTranslation = new Coordinate(0, 0);
		fetchTimer = new FetchTimer();
	}

	// ------------------------------------------------------------------------
	// MapCompositionHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerAdded(LayerAddedEvent event) {
		Layer<?> layer = event.getLayer();

		HtmlGroup layerContainer = new HtmlGroup(htmlContainer.getWidth(), htmlContainer.getHeight());
		layerContainer.setVisible(layer.getLayerInfo().isVisible());
		htmlContainer.add(layerContainer);

		MapScalesRenderer layerRenderer = mapScalesRendererFactory.create(viewPort, layer, layerContainer);
		if (layerRenderers.size() == 0) {
			layerRenderer.addScaleLevelRenderedHandler(new ScaleLevelRenderedHandler() {

				public void onScaleLevelRendered(ScaleLevelRenderedEvent event) {
					onScaleRendered(event.getScale());
				}
			});
		}
		layerRenderers.put(layer, layerRenderer);
		if (viewPort.isInitialized()) {
			navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
		}
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
		Layer<?> layer = event.getLayer();
		if (layerRenderers.containsKey(layer)) {
			MapScalesRenderer layerRenderer = layerRenderers.get(layer);
			layerRenderer.cancel();
			layerRenderer.clear();
			layerRenderers.remove(layer);
			if (event.getIndex() < htmlContainer.getChildCount()) {
				HtmlObject layerContainer = htmlContainer.getChild(event.getIndex());				
				if (layerContainer != null) {
					htmlContainer.remove(layerContainer);
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// LayerOrderChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		if (event.getFromIndex() < htmlContainer.getChildCount()) {
			int toIndex = event.getToIndex() > htmlContainer.getChildCount() ? htmlContainer.getChildCount() : event
					.getToIndex();
			HtmlObject layerContainer = htmlContainer.getChild(event.getFromIndex());
			if (layerContainer != null) {
				htmlContainer.remove(layerContainer);
				htmlContainer.insert(layerContainer, toIndex);
			}
		}
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		Layer<?> layer = event.getLayer();
		if (layerRenderers.containsKey(layer)) {
			MapScalesRenderer layerRenderer = layerRenderers.get(layer);
			layerRenderer.ensureScale(viewPort.getScale(), viewPort.getBounds());
			layerRenderer.getHtmlContainer().setVisible(true);
		}
	}

	public void onHide(LayerHideEvent event) {
		Layer<?> layer = event.getLayer();
		if (layerRenderers.containsKey(layer)) {
			HtmlContainer layerContainer = layerRenderers.get(layer).getHtmlContainer();
			layerContainer.setVisible(false);
		}
	}

	public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
	}

	// ------------------------------------------------------------------------
	// LayerStyleChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerStyleChanged(LayerStyleChangedEvent event) {
		Layer<?> layer = event.getLayer();
		if (layer instanceof RasterLayer) {
			HtmlContainer layerContainer = layerRenderers.get(layer).getHtmlContainer();
			layerContainer.setOpacity(((RasterLayer) layer).getOpacity());
		} else if (layer instanceof VectorLayer) {
			// TODO implement me...
		}
	}

	// ------------------------------------------------------------------------
	// LayerRefreshedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerRefreshed(LayerRefreshedEvent event) {
		Layer<?> layer = event.getLayer();
		MapScalesRenderer renderer = layerRenderers.get(layer);
		if (renderer != null) {
			renderer.clear();
			navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
		}
	}

	// ------------------------------------------------------------------------
	// MapResizedHandler implementation:
	// ------------------------------------------------------------------------

	public void onMapResized(MapResizedEvent event) {
		navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
	}

	// ------------------------------------------------------------------------
	// ViewPortChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onViewPortChanged(ViewPortChangedEvent event) {
		if (viewPort.getScale() > 0) {
			if (first) {
				navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
			} else {
				navigateTo(viewPort.getBounds(), viewPort.getScale(), animationMillis);
			}
		}
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		if (viewPort.getScale() > 0) {
			navigateTo(viewPort.getBounds(), viewPort.getScale(), animationMillis);
		}
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		if (viewPort.getScale() > 0 && !animation.isRunning()) {
			navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
		}
	}

	// ------------------------------------------------------------------------
	// MapRenderer public methods:
	// ------------------------------------------------------------------------

	public void setMapExtentScaleAtFetch(double scale) {
	}

	public int getAnimationMillis() {
		return animationMillis;
	}

	public void setAnimationMillis(int animationMillis) {
		this.animationMillis = animationMillis;
	}

	public int getNrAnimatedLayers() {
		return animation.getNrAnimatedLayers();
	}

	public void setNrAnimatedLayers(int nrAnimatedLayers) {
		animation.setNrAnimatedLayers(nrAnimatedLayers);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected void onNavigationComplete() {
		navigationBusy = false;

		// Keep track of the
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		previousTranslation = new Coordinate(translation.getDx(), translation.getDy());

		// Bring the current scale to the font and make it visible (for all layers):
		for (int i = 0; i < layersModel.getLayerCount(); i++) {
			Layer<?> layer = layersModel.getLayer(i);
			MapScalesRenderer presenter = layerRenderers.get(layer);
			if (presenter != null) {
				presenter.bringScaleToFront(currentScale);
				presenter.setScaleVisibility(currentScale, true);
				presenter.applyScaleTranslation(currentScale, previousTranslation);
			}
		}

		onScaleRendered(currentScale);
	}

	// Make the previous scale invisible. Can only be done if the animation has completed.
	protected void onScaleRendered(double scale) {
		if (scale == currentScale && scale != previousScale && !navigationBusy) {
			// Make the previous scale invisible:
			for (int i = 0; i < layersModel.getLayerCount(); i++) {
				Layer<?> layer = layersModel.getLayer(i);
				MapScalesRenderer presenter = layerRenderers.get(layer);
				if (presenter != null) {
					presenter.setScaleVisibility(previousScale, false);
				}
			}
		}
	}

	private void navigateTo(Bbox bounds, double scale, int millis) {
		navigationBusy = true;
		int delay = fetchDelay >= millis ? 0 : fetchDelay;

		// Calculate the map translation for the requested scale:
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate transCoord = new Coordinate(translation.getDx(), translation.getDy());

		// Install a navigation animation:
		if (animation.isRunning()) {
			currentScale = scale;

			// Ensure the scale level after a certain delay:
			ensureScale(scale, bounds, delay);

			// Extend the current animation:
			animation.extend(currentScale / previousScale, transCoord, millis);
		} else {
			// Keep track of navigation details:
			previousScale = currentScale;
			currentScale = scale;

			// Ensure the scale level after a certain delay:
			ensureScale(scale, bounds, delay);

			// Create an ordered list of presenters for the animation.
			List<MapScalesRenderer> presenters = new ArrayList<MapScalesRenderer>();
			for (int i = 0; i < layersModel.getLayerCount(); i++) {
				Layer<?> layer = layersModel.getLayer(i);
				MapScalesRenderer presenter = layerRenderers.get(layer);
				if (presenter != null) {
					presenters.add(presenter);
				}
			}

			// Start a new animation:
			animation.start(presenters, 1, currentScale / previousScale, previousTranslation, transCoord, millis);
		}
	}

	private void ensureScale(double scale, Bbox bounds, int delay) {
		fetchTimer.cancel();
		fetchTimer.setTargetLocation(scale, bounds);
		fetchTimer.schedule(delay);
	}

	/**
	 * Timer that keeps track of fetch delays to fetch requested scales.
	 * 
	 * @author Pieter De Graef
	 */
	private class FetchTimer extends Timer {

		private double scale;

		private Bbox bounds;

		public void schedule(int delayMillis) {
			if (delayMillis == 0) {
				run();
			} else {
				super.schedule(delayMillis);
			}
		}

		public void run() {
			// Ensure the scale at the requested location:
			List<MapScalesRenderer> presenters = new ArrayList<MapScalesRenderer>();
			for (int i = 0; i < layersModel.getLayerCount(); i++) {
				Layer<?> layer = layersModel.getLayer(i);
				MapScalesRenderer presenter = layerRenderers.get(layer);
				if (presenter != null) {
					presenter.ensureScale(scale, bounds);
					if (first) {
						// First time: don't make it invisible. We're not using the animation either.
						presenter.setScaleVisibility(scale, true);
						first = false;
					} else {
						presenter.setScaleVisibility(scale, false);
					}
					presenters.add(presenter); // Create an ordered list of presenters for the animation.
				}
			}
		}

		public void setTargetLocation(double scale, Bbox bounds) {
			this.scale = scale;
			this.bounds = bounds;
		}
	}
}