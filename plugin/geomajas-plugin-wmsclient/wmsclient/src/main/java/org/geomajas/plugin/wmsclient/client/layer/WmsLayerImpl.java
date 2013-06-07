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

import org.geomajas.configuration.FontStyleInfo;
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
		
		presenters.add(new WmsLayerStylePresenter(getLegendImageUrl()));

		return presenters;
	}
	
	
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
	public boolean hasSupportForDynamicLegend() {
		return false;
	}
	
	@Override
	public String getLegendImageUrl() {
		
		return getLegendImageUrl(0, 0, null, null);
	}

	@Override
	// Note that the bounds cannot be specified here
	public String getLegendImageUrl(int iconWidth, int iconHeight, FontStyleInfo fontStyle, String imageFormat) {
		int widthOrig = wmsConfig.getLegendWidth();
		int heightOrig = wmsConfig.getLegendHeight();
		if (iconWidth > 0) {
			getConfig().setLegendWidth(iconWidth); //TODO: better to clone getConfig() result
		}
		if (iconHeight > 0) {
			getConfig().setLegendHeight(iconHeight);  //TODO: better to clone getConfig() result
		}
		String url = wmsService.getLegendGraphicUrl(getConfig(), fontStyle, imageFormat);

		getConfig().setLegendWidth(widthOrig);
		getConfig().setLegendHeight(heightOrig);

		return url;
	}

	@Override
	public String getLegendImageUrl(int iconWidth, int iconHeight,
			FontStyleInfo fontStyle, String imageFormat, Bbox bounds) {
		// bounds are ignored here, override this method to implement dynamic legend !
		return getLegendImageUrl(iconWidth, iconHeight, fontStyle, imageFormat);
	}
	
	private Bbox getScreenBounds(double scale, Bbox worldBounds) {
		return new Bbox(Math.round(scale * worldBounds.getX()), -Math.round(scale * worldBounds.getMaxY()),
				Math.round(scale * worldBounds.getMaxX()) - Math.round(scale * worldBounds.getX()), Math.round(scale
						* worldBounds.getMaxY())
						- Math.round(scale * worldBounds.getY()));
	}



}