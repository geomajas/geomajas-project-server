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

package org.geomajas.puregwt.client.map.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Matrix;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.map.LayersModel;
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
import org.geomajas.puregwt.client.map.gfx.HtmlObject;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.map.layer.VectorLayer;

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

	// Keeping track of the navigation animation:

	private int animationMillis = 400;

	private double previousScale;

	private double currentScale;

	private Coordinate previousTranslation;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public MapRendererImpl(LayersModel layersModel, final ViewPort viewPort, HtmlContainer htmlContainer) {
		this.layersModel = layersModel;
		this.viewPort = viewPort;
		this.htmlContainer = htmlContainer;

		this.layerRenderers = new HashMap<Layer<?>, MapScalesRenderer>();
		this.animation = new MapNavigationAnimation(new LinearNavigationFunction()) {

			protected void onComplete() {
				super.onComplete();
				Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
				previousTranslation = new Coordinate(translation.getDx(), translation.getDy());

				for (MapScalesRenderer presenter : layerPresenters) {
					presenter.setScaleVisibility(previousScale, false);
					presenter.setScaleVisibility(currentScale, true);
					presenter.applyScaleTranslation(currentScale, previousTranslation);
				}
			};
		};

		previousScale = viewPort.getScale();
		currentScale = viewPort.getScale();
		previousTranslation = new Coordinate(0, 0);
	}

	// ------------------------------------------------------------------------
	// LayerOrderChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		if (event.getFromIndex() < htmlContainer.getChildCount()) {
			// Source is rasterized layer:
			int toIndex = event.getToIndex() > htmlContainer.getChildCount() ? htmlContainer.getChildCount() : event
					.getToIndex();
			HtmlObject layerContainer = htmlContainer.getChild(event.getFromIndex());
			if (layerContainer != null) {
				htmlContainer.remove(layerContainer);
				htmlContainer.insert(layerContainer, toIndex);
			}
			// } else {
			// // Source is vector layer:
			// int fromIndex = event.getFromIndex() - htmlContainer.getChildCount();
			// int toIndex = event.getToIndex() - htmlContainer.getChildCount();
			// VectorObject layerContainer = vectorContainer.getVectorObject(fromIndex);
			// if (layerContainer != null) {
			// vectorContainer.remove(layerContainer);
			// vectorContainer.insert(layerContainer, toIndex);
			// }
		}
	}

	// ------------------------------------------------------------------------
	// LayerVisibleHandler implementation:
	// ------------------------------------------------------------------------

	public void onShow(LayerShowEvent event) {
		Layer<?> layer = event.getLayer();
		HtmlContainer layerContainer = getOrCreateHtmlContainer(layer);
		layerContainer.setVisible(true);
		navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
	}

	public void onHide(LayerHideEvent event) {
		Layer<?> layer = event.getLayer();
		HtmlContainer layerContainer = getOrCreateHtmlContainer(layer);
		layerContainer.setVisible(false);
		if (layer instanceof VectorLayer) {
			// TODO make vector container invisible...
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
			for (int i = 0; i < htmlContainer.getChildCount(); i++) {
				HtmlObject htmlObject = htmlContainer.getChild(i);
				htmlObject.setOpacity(((RasterLayer) layer).getOpacity());
			}
		} else if (layer instanceof VectorLayer) {
			// TODO implement me...
		}
	}

	// ------------------------------------------------------------------------
	// MapResizedHandler implementation:
	// ------------------------------------------------------------------------

	public void onMapResized(MapResizedEvent event) {
		navigateTo(viewPort.getBounds(), viewPort.getScale(), 0);
	}

	// ------------------------------------------------------------------------
	// MapCompositionHandler implementation:
	// ------------------------------------------------------------------------

	public void onLayerAdded(LayerAddedEvent event) {
		Layer<?> layer = event.getLayer();
		HtmlContainer layerContainer = getOrCreateHtmlContainer(layer);
		layerContainer.setVisible(layer.getLayerInfo().isVisible());
		MapScalesRenderer layerPresenter = new MapScalesRendererImpl(viewPort, layer, layerContainer);
		layerRenderers.put(layer, layerPresenter);
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
		Layer<?> layer = event.getLayer();

		// if (layerContainers.containsKey(layer)) {
		// layerContainers.remove(layer);
		// }

		if (layerRenderers.containsKey(layer)) {
			// TODO onDestroy method??
			layerRenderers.remove(layer);
		}
	}

	// ------------------------------------------------------------------------
	// ViewPortChangedHandler implementation:
	// ------------------------------------------------------------------------

	public void onViewPortChanged(ViewPortChangedEvent event) {
		if (viewPort.getScale() > 0) {
			navigateTo(viewPort.getBounds(), viewPort.getScale(), animationMillis);
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

	private HtmlContainer getOrCreateHtmlContainer(Layer<?> layer) {
		if (layerRenderers.containsKey(layer)) {
			return layerRenderers.get(layer).getHtmlContainer();
		}
		HtmlGroup layerContainer = new HtmlGroup(htmlContainer.getWidth(), htmlContainer.getHeight());
		htmlContainer.add(layerContainer);
		return layerContainer;
	}

	private void navigateTo(Bbox bounds, double scale, int millis) {
		Matrix translation = viewPort.getTranslationMatrix(RenderSpace.WORLD, RenderSpace.SCREEN);
		Coordinate transCoord = new Coordinate(translation.getDx(), translation.getDy());

		// Install a navigation animation:
		if (animation.isRunning()) {
			currentScale = scale;

			animation.extend(currentScale / previousScale, transCoord, millis);
		} else {
			// Keep track of navigation details:
			previousScale = currentScale;
			currentScale = scale;

			// Let every layer prepare for navigation:
			List<MapScalesRenderer> presenters = new ArrayList<MapScalesRenderer>();
			for (int i = 0; i < layersModel.getLayerCount(); i++) {
				Layer<?> layer = layersModel.getLayer(i);
				MapScalesRenderer presenter = layerRenderers.get(layer);
				if (presenter != null) {
					// presenter.prepareNavigation(previousPosition, previousScale);
					// TODO find a better way to get the bounds from scale+position:
					presenter.ensureScale(currentScale, bounds);
					presenters.add(presenter); // Create an ordered list of presenters for the animation.
				}
			}

			// Start a new animation:
			animation.start(presenters, 1, currentScale / previousScale, previousTranslation, transCoord, millis);
		}
	}
}