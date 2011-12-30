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
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.HtmlGroup;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;
import org.geomajas.puregwt.client.map.layer.VectorLayer;
import org.geomajas.puregwt.client.map.render.event.ScaleLevelRenderedEvent;
import org.geomajas.puregwt.client.map.render.event.ScaleLevelRenderedHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

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
public class LayerScalesRenderer implements MapScalesRenderer {

	private static final int SCALE_CACHE_SIZE = 2; // Let's keep the last 3 scales.

	private final EventBus eventBus;

	private final ViewPort viewPort;

	private final Layer<?> layer;

	private final HtmlContainer htmlContainer;

	private final Map<Double, TiledScaleRenderer> tiledScaleRenderers; // A renderer per scale.

	private final List<Double> scales; // Keeps track of the lastly visited scales.

	private double visibleScale;

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
	public LayerScalesRenderer(ViewPort viewPort, Layer<?> layer, HtmlContainer htmlContainer) {
		this.viewPort = viewPort;
		this.layer = layer;
		this.htmlContainer = htmlContainer;
		tiledScaleRenderers = new HashMap<Double, TiledScaleRenderer>();
		scales = new ArrayList<Double>(SCALE_CACHE_SIZE + 2);
		eventBus = new SimpleEventBus();

		visibleScale = viewPort.getScale();
	}

	// ------------------------------------------------------------------------
	// MapScalesRenderer implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public HandlerRegistration addScaleLevelRenderedHandler(ScaleLevelRenderedHandler handler) {
		return eventBus.addHandler(ScaleLevelRenderedHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HtmlContainer getHtmlContainer() {
		return htmlContainer;
	}

	/** {@inheritDoc} */
	public void ensureScale(double scale, Bbox bounds) {
		// Get or create the presenter, then turn it invisible and fetch the tiles.
		TiledScaleRenderer presenter = getOrCreate(scale);
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

	/** {@inheritDoc} */
	public void setScaleVisibility(double scale, boolean visible) {
		TiledScaleRenderer scalePresenter = tiledScaleRenderers.get(scale);
		if (scalePresenter != null) {
			GWT.log("Setting scale visibility: " + scale + ", " + visible);
			if (visible) {
				visibleScale = scale;
				scalePresenter.getHtmlContainer().zoomToLocation(1, 0, 0);
				scalePresenter.getHtmlContainer().setVisible(true);
			} else if (scale != visibleScale) {
				scalePresenter.getHtmlContainer().setVisible(false);
			}
		}
	}

	/** {@inheritDoc} */
	public void applyScaleTranslation(double scale, Coordinate translation) {
		TiledScaleRenderer scalePresenter = tiledScaleRenderers.get(scale);
		if (scalePresenter != null) {
			GWT.log("Translate scale: " + scale + ", " + translation.toString());
			scalePresenter.getHtmlContainer().setLeft((int) Math.round(translation.getX()));
			scalePresenter.getHtmlContainer().setTop((int) Math.round(translation.getY()));
		}
	}

	/** Does nothing. */
	public void cancel() {
	}

	/** {@inheritDoc} */
	public TiledScaleRenderer getVisibleScale() {
		return tiledScaleRenderers.get(visibleScale);
	}

	/** {@inheritDoc} */
	public TiledScaleRenderer getScale(double scale) {
		return tiledScaleRenderers.get(scale);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private TiledScaleRenderer getOrCreate(double scale) {
		if (tiledScaleRenderers.containsKey(scale)) {
			return tiledScaleRenderers.get(scale);
		}

		GWT.log("Creating new scale (default invisible), " + scale);
		final HtmlContainer container = new HtmlGroup();
		container.setVisible(false);
		htmlContainer.insert(container, 0);

		TiledScaleRenderer scalePresenter = null;
		if (layer instanceof RasterLayer) {
			scalePresenter = new RasterLayerScaleRenderer(viewPort.getCrs(), (RasterLayer) layer, container, scale) {

				public void onTilesReceived(HtmlContainer container, double scale) {
				}

				public void onTilesRendered(HtmlContainer container, double scale) {
					eventBus.fireEvent(new ScaleLevelRenderedEvent(scale));
				}
			};
		} else {
			scalePresenter = new VectorLayerScaleRenderer(viewPort, (VectorLayer) layer, container, scale) {

				public void onTilesReceived(HtmlContainer container, double scale) {
				}

				public void onTilesRendered(HtmlContainer container, double scale) {
					eventBus.fireEvent(new ScaleLevelRenderedEvent(scale));
				}
			};
		}
		tiledScaleRenderers.put(scale, scalePresenter);
		return scalePresenter;
	}

	private boolean removeScaleLevel(Double scale) {
		if (scale != visibleScale) {
			GWT.log("Remove scale: " + scale);
			// Remove the presenter:
			TiledScaleRenderer removedPresenter = tiledScaleRenderers.get(scale);
			if (removedPresenter == null) {
				return false;
			}
			removedPresenter.cancel();
			// TODO let the presenter have it's own destroy() method??
			htmlContainer.remove(removedPresenter.getHtmlContainer());
			tiledScaleRenderers.remove(scale);

			// Remove the scale:
			return scales.remove(scale);
		}
		return false;
	}
}