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

package org.geomajas.puregwt.client.map.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.puregwt.client.event.ScaleLevelRenderedHandler;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.HtmlGroup;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterServerLayer;
import org.geomajas.puregwt.client.map.layer.VectorServerLayer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * <p>
 * MapScalesRenderer implementation that keeps a fixed number of scales in cache. This strategy trades memory for speed.
 * The more scale levels are kept in memory, the less tiles need to be fetched.
 * </p>
 * <p>
 * This implementation keeps the scales for a single layer, not the entire map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LayerScalesRendererImpl implements LayerRenderer {

	private static final int SCALE_CACHE_SIZE = 3; // Let's keep the last 3 scales.

	@Inject
	protected EventBus eventBus;

	protected final ViewPort viewPort;

	protected final Layer layer;

	protected final HtmlContainer htmlContainer;

	protected final Map<Double, LayerScaleRenderer> tiledScaleRenderers; // A renderer per scale.

	protected final List<Double> scales; // Keeps track of the lastly visited scales.

	protected double visibleScale;
	
	@Inject
	protected TiledScaleRendererFactory rasterRendererFactory;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new instance with the following parameters.
	 * 
	 * @param viewPort
	 *            The viewPort onto which this renderer applies.
	 * @param layer
	 *            The layer to be rendered.
	 * @param htmlContainer
	 *            The container wherein to render all scales.
	 */
	@Inject
	public LayerScalesRendererImpl(@Assisted ViewPort viewPort, @Assisted Layer layer,
			@Assisted HtmlContainer htmlContainer) {
		this.viewPort = viewPort;
		this.layer = layer;
		this.htmlContainer = htmlContainer;
		tiledScaleRenderers = new HashMap<Double, LayerScaleRenderer>();
		scales = new ArrayList<Double>(SCALE_CACHE_SIZE + 2);

		visibleScale = viewPort.getScale();
	}

	// ------------------------------------------------------------------------
	// MapScalesRenderer implementation:
	// ------------------------------------------------------------------------

	@Override
	public HandlerRegistration addScaleLevelRenderedHandler(ScaleLevelRenderedHandler handler) {
		return eventBus.addHandlerToSource(ScaleLevelRenderedHandler.TYPE, this, handler);
	}

	@Override
	public HtmlContainer getHtmlContainer() {
		return htmlContainer;
	}

	@Override
	public void ensureScale(double scale, Bbox bounds) {
		cancel(); // TODO should we do this??

		// Get or create the presenter, then turn it invisible and fetch the tiles.
		LayerScaleRenderer presenter = getOrCreate(scale);
		if (scale != visibleScale) {
			presenter.getHtmlContainer().setVisible(false);
		}
		presenter.render(bounds);

		// Rearrange the scales:
		if (scales.contains(scale)) {
			scales.remove(scale);
		}
		scales.add(scale);

		// If we have too many scales, remove the last one to be used:
		if (scales.size() > SCALE_CACHE_SIZE) {
			if (scales.get(0) != visibleScale) {
				removeScaleLevel(scales.get(0));
			} else if (scales.get(1) != scale) {
				removeScaleLevel(scales.get(1));
			}
		}
	}

	@Override
	public void bringScaleToFront(double scale) {
		LayerScaleRenderer scalePresenter = tiledScaleRenderers.get(scale);
		if (scalePresenter != null) {
			LayerScaleRenderer renderer = tiledScaleRenderers.get(scale);
			htmlContainer.bringToFront(renderer.getHtmlContainer());
		}
	}

	@Override
	public void setScaleVisibility(double scale, boolean visible) {
		LayerScaleRenderer scalePresenter = tiledScaleRenderers.get(scale);
		if (scalePresenter != null) {
			if (visible) {
				visibleScale = scale;
				if (Dom.isTransformationSupported()) {
					scalePresenter.getHtmlContainer().applyScale(1, 0, 0);
				}
				scalePresenter.getHtmlContainer().setVisible(true);
			} else if (scale != visibleScale) {
				scalePresenter.getHtmlContainer().setVisible(false);
			}
		}
	}

	@Override
	public void applyScaleTranslation(double scale, Coordinate translation) {
		LayerScaleRenderer scalePresenter = tiledScaleRenderers.get(scale);
		if (scalePresenter != null) {
			scalePresenter.getHtmlContainer().setLeft((int) Math.round(translation.getX()));
			scalePresenter.getHtmlContainer().setTop((int) Math.round(translation.getY()));
		}
	}

	/** Delegates the cancel to each scale level. */
	public void cancel() {
		for (LayerScaleRenderer scaleRenderer : tiledScaleRenderers.values()) {
			scaleRenderer.cancel();
		}
	}

	@Override
	public LayerScaleRenderer getVisibleScale() {
		return tiledScaleRenderers.get(visibleScale);
	}

	@Override
	public LayerScaleRenderer getScale(double scale) {
		return tiledScaleRenderers.get(scale);
	}

	@Override
	public void clear() {
		while (tiledScaleRenderers.size() > 0) {
			Double scale = tiledScaleRenderers.keySet().iterator().next();
			LayerScaleRenderer removedPresenter = tiledScaleRenderers.get(scale);
			removedPresenter.cancel();
			htmlContainer.remove(removedPresenter.getHtmlContainer());
			tiledScaleRenderers.remove(scale);
		}
		scales.clear();
	}

	@Override
	public Layer getLayer() {
		return layer;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	protected LayerScaleRenderer getOrCreate(double scale) {
		if (tiledScaleRenderers.containsKey(scale)) {
			return tiledScaleRenderers.get(scale);
		}

		final HtmlContainer container = new HtmlGroup();
		//container.getElement().setId("scale-" + scale);
		htmlContainer.insert(container, 0);

		LayerScaleRenderer scalePresenter = null;
		if (layer instanceof RasterServerLayer) {
			scalePresenter = rasterRendererFactory.create(this, viewPort.getCrs(), (RasterServerLayer) layer, container,
					scale);
		} else if (layer instanceof VectorServerLayer) {
			scalePresenter = rasterRendererFactory.create(this, viewPort.getCrs(), (VectorServerLayer) layer, container,
					scale);
		}
		if (scalePresenter != null) {
			tiledScaleRenderers.put(scale, scalePresenter);
		}
		return scalePresenter;
	}

	private boolean removeScaleLevel(Double scale) {
		if (scale != visibleScale) {
			// Remove the presenter:
			LayerScaleRenderer removedPresenter = tiledScaleRenderers.get(scale);
			if (removedPresenter == null) {
				return false;
			}
			removedPresenter.cancel();
			htmlContainer.remove(removedPresenter.getHtmlContainer());
			tiledScaleRenderers.remove(scale);

			// Remove the scale:
			return scales.remove(scale);
		}
		return false;
	}
}