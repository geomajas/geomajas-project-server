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
import org.geomajas.plugin.wmsclient.client.layer.config.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.client.layer.config.WmsTileConfiguration;
import org.geomajas.plugin.wmsclient.client.render.WmsLayerRenderer;
import org.geomajas.plugin.wmsclient.client.render.WmsLayerRendererFactory;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.plugin.wmsclient.client.service.WmsTileService;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.layer.AbstractLayer;
import org.geomajas.puregwt.client.map.layer.LayerStylePresenter;
import org.geomajas.puregwt.client.map.render.LayerRenderer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Default implementation of a {@link WmsLayer}.
 * 
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsLayerImpl extends AbstractLayer implements WmsLayer {

	public static final String DEFAULT_LEGEND_FONT_FAMILY = "Arial";

	public static final int DEFAULT_LEGEND_FONT_SIZE = 13;

	protected final WmsLayerConfiguration wmsConfig;

	protected final WmsTileConfiguration tileConfig;

	@Inject
	protected WmsService wmsService;

	@Inject
	protected WmsTileService tileService;

	@Inject
	private WmsLayerRendererFactory rendererFactory;

	protected WmsLayerRenderer renderer;

	@Inject
	protected WmsLayerImpl(@Assisted String title, @Assisted WmsLayerConfiguration wmsConfig,
			@Assisted WmsTileConfiguration tileConfig) {
		super(wmsConfig.getLayers());

		this.wmsConfig = wmsConfig;
		this.tileConfig = tileConfig;
		this.title = title;
	}

	@Override
	public List<LayerStylePresenter> getStylePresenters() {
		List<LayerStylePresenter> presenters = new ArrayList<LayerStylePresenter>();
		presenters.add(new WmsLayerStylePresenter(wmsService.getLegendGraphicUrl(wmsConfig)));
		//TODO check if not better to use:
//		presenters.add(new WmsLayerStylePresenter(wmsService.getLegendGraphicUrl(wmsConfig, 
//				DEFAULT_LEGEND_FONT_FAMILY, DEFAULT_LEGEND_FONT_SIZE)));
		return presenters;
	}
	
	
//
//	TODO Old AnB: 
//	@Override
//	public List<LayerStylePresenter> getStylePresenters() {
//		return getStylePresenters(null, null);
//	}
//	
//	@Override
//	public List<LayerStylePresenter> getStylePresenters(String fontFamily, Integer fontSize) {
//		List<LayerStylePresenter> presenters = new ArrayList<LayerStylePresenter>();
//		presenters.add(new WmsLayerStylePresenter(wmsService.getLegendGraphicUrl(wmsConfig, 
//				FONT_FAMILY_LEGEND_LABELS, FONT_SIZE_LEGEND_LABELS)));
//		return presenters;
//	}
	

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	@Override
	public WmsLayerConfiguration getConfig() {
		return wmsConfig;
	}

	@Override
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

	@Override
	public ViewPort getViewPort() {
		return viewPort;
	}

	// ------------------------------------------------------------------------
	// OpacitySupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setOpacity(double opacity) {
		renderer.getHtmlContainer().setOpacity(opacity);
	}

	@Override
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
	public LayerRenderer getRenderer(HtmlContainer container) {
		if (renderer == null) {
			renderer = rendererFactory.create(this, container);
		}
		return renderer;
	}

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

	// ------------------------------------------------------------------------
	// LegendUrlSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public String getLegendImageUrl() {
		
		return getLegendImageUrl(wmsConfig.getLegendWidth(), wmsConfig.getLegendHeight(), DEFAULT_LEGEND_FONT_FAMILY,
				DEFAULT_LEGEND_FONT_SIZE);
	}

	@Override
	public String getLegendImageUrl(int width, int height, String fontFamily, int fontSize) {
		int widthOrig = wmsConfig.getLegendWidth();
		int heightOrig = wmsConfig.getLegendHeight();
		if (width > 0) {
			getConfig().setLegendWidth(width);
		}
		if (height > 0) {
			getConfig().setLegendHeight(height);
		}
		String url = wmsService.getLegendGraphicUrl(getConfig(), fontFamily, fontSize);

		getConfig().setLegendWidth(widthOrig);
		getConfig().setLegendHeight(heightOrig);

		return url;
	}

	private Bbox getScreenBounds(double scale, Bbox worldBounds) {
		return new Bbox(Math.round(scale * worldBounds.getX()), -Math.round(scale * worldBounds.getMaxY()),
				Math.round(scale * worldBounds.getMaxX()) - Math.round(scale * worldBounds.getX()), Math.round(scale
						* worldBounds.getMaxY())
						- Math.round(scale * worldBounds.getY()));
	}
}