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
import java.util.Collections;
import java.util.List;

import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.wmsclient.client.render.WmsScalesRenderer;
import org.geomajas.plugin.wmsclient.client.render.WmsScalesRendererFactory;
import org.geomajas.plugin.wmsclient.client.render.WmsTiledScaleRenderer;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
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
	public List<RasterTile> getCurrentTiles() {
		if (renderer != null) {
			WmsTiledScaleRenderer scaleRenderer = (WmsTiledScaleRenderer) renderer.getScale(viewPort.getScale());
			return scaleRenderer.getTiles(viewPort.getBounds());
		} else {
			return Collections.<RasterTile>emptyList();
		}
	}
}