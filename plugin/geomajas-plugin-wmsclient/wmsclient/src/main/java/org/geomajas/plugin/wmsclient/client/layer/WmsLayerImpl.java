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

package org.geomajas.plugin.wmsclient.client.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.plugin.wmsclient.client.render.WmsScalesRenderer;
import org.geomajas.plugin.wmsclient.client.render.WmsScalesRendererFactory;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.plugin.wmsclient.client.service.WmsTileService;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.AbstractLayer;
import org.geomajas.puregwt.client.map.layer.LayerStylePresenter;
import org.geomajas.puregwt.client.map.render.MapScalesRenderer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Default implementation of a {@link WmsLayer}.
 * 
 * @author Pieter De Graef
 */
public class WmsLayerImpl extends AbstractLayer implements WmsLayer {

	protected final WmsLayerConfiguration wmsConfig;

	protected final WmsTileConfiguration tileConfig;

	@Inject
	protected WmsService wmsService;

	@Inject
	protected WmsTileService tileService;

	@Inject
	private WmsScalesRendererFactory rendererFactory;

	protected WmsScalesRenderer renderer;

	@Inject
	protected WmsLayerImpl(@Assisted String title, @Assisted WmsLayerConfiguration wmsConfig,
			@Assisted WmsTileConfiguration tileConfig) {
		super(wmsConfig.getLayers());

		this.wmsConfig = wmsConfig;
		this.tileConfig = tileConfig;
		this.title = title;
	}

	/** {@inheritDoc} */
	public List<LayerStylePresenter> getStylePresenters() {
		List<LayerStylePresenter> presenters = new ArrayList<LayerStylePresenter>();
		presenters.add(new WmsLayerStylePresenter(wmsService.getLegendGraphicUrl(wmsConfig)));
		return presenters;
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public WmsLayerConfiguration getConfig() {
		return wmsConfig;
	}

	/** {@inheritDoc} */
	public WmsTileConfiguration getTileConfig() {
		return tileConfig;
	}

	/**
	 * Returns the view port CRS. This layer should always have the same CRS as the map!
	 * 
	 * @return The layer CRS (=map CRS).
	 */
	public String getCrs() {
		return viewPort.getCrs();
	}

	/** {@inheritDoc} */
	public ViewPort getViewPort() {
		return viewPort;
	}

	// ------------------------------------------------------------------------
	// OpacitySupported implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void setOpacity(double opacity) {
		renderer.getHtmlContainer().setOpacity(opacity);
	}

	/** {@inheritDoc} */
	public double getOpacity() {
		return renderer.getHtmlContainer().getOpacity();
	}

	// ------------------------------------------------------------------------
	// HasMapScalesRenderer implementation:
	// ------------------------------------------------------------------------

	/**
	 * Get the specific renderer for this type of layer. This will return a scale-based renderer that used a
	 * {@link org.geomajas.plugin.wmsclient.client.render.WmsTiledScaleRendererImpl} for each resolution.
	 */
	public MapScalesRenderer getRenderer(HtmlContainer container) {
		if (renderer == null) {
			renderer = rendererFactory.create(this, container);
		}
		return renderer;
	}

	/** {@inheritDoc} */
	@Override
	public List<RasterTile> getTiles(double scale, Bbox worldBounds) {
		List<TileCode> codes = tileService.getTileCodesForBounds(getViewPort(), tileConfig, worldBounds, scale);
		List<RasterTile> tiles = new ArrayList<RasterTile>();
		if (!codes.isEmpty()) {
			double actualScale = viewPort.getZoomStrategy().getZoomStepScale(codes.get(0).getTileLevel());
			for (TileCode code : codes) {
				Bbox bounds = tileService.getWorldBoundsForTile(getViewPort(), tileConfig, code);
				RasterTile tile = new RasterTile(getScreenBounds(actualScale, bounds), code.toString());
				tile.setCode(code);
				tile.setUrl(wmsService.getMapUrl(getConfig(), getCrs(), bounds, tileConfig.getTileWidth(),
						tileConfig.getTileHeight()));
				tiles.add(tile);
			}
		}
		return tiles;
	}

	private Bbox getScreenBounds(double scale, Bbox worldBounds) {
		return new Bbox(Math.round(scale * worldBounds.getX()), -Math.round(scale * worldBounds.getMaxY()),
				Math.round(scale * worldBounds.getMaxX()) - Math.round(scale * worldBounds.getX()), Math.round(scale
						* worldBounds.getMaxY())
						- Math.round(scale * worldBounds.getY()));
	}
}