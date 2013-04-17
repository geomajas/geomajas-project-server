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

package org.geomajas.plugin.wmsclient.client.render;

import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.HtmlGroup;
import org.geomajas.puregwt.client.map.render.LayerScalesRendererImpl;
import org.geomajas.puregwt.client.map.render.TiledScaleRenderer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Renderer definition for this type of Layer. Used scale-based rendering through {@link WmsTiledScaleRendererImpl}.
 * 
 * @author Pieter De Graef
 */
public class WmsScalesRendererImpl extends LayerScalesRendererImpl implements WmsScalesRenderer {

	@Inject
	private WmsTiledScaleRendererFactory scaleRendererFactory;

	@Inject
	public WmsScalesRendererImpl(@Assisted WmsLayer layer, @Assisted HtmlContainer htmlContainer) {
		super(layer.getViewPort(), layer, htmlContainer);
	}

	/** Return renderers of the type {@link WmsTiledScaleRendererImpl}. */
	protected TiledScaleRenderer getOrCreate(double scale) {
		if (tiledScaleRenderers.containsKey(scale)) {
			return tiledScaleRenderers.get(scale);
		}

		final HtmlContainer container = new HtmlGroup();
		htmlContainer.insert(container, 0);

		TiledScaleRenderer scalePresenter = scaleRendererFactory.create(container, (WmsLayer) layer, scale);
		tiledScaleRenderers.put(scale, scalePresenter);
		return scalePresenter;
	}
}