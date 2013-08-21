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
package org.geomajas.plugin.wmsclient.printing.client;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.map.ZoomStrategy.ZoomOption;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.OpacitySupported;
import org.geomajas.plugin.printing.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.printing.server.dto.WmsClientLayerInfo;

/**
 * Builder for WMS layer.
 * 
 * @author Jan De Moerloose
 */
public class WmsLayerBuilder implements PrintableLayerBuilder {

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer, Bbox worldBounds, double scale) {
		WmsLayer wmsLayer = (WmsLayer) layer;

		// fromDto()
		WmsClientLayerInfo info = new WmsClientLayerInfo();
		info.setTiles(wmsLayer.getTiles(scale, worldBounds));
		info.setTileHeight(wmsLayer.getTileConfig().getTileHeight());
		info.setTileWidth(wmsLayer.getTileConfig().getTileWidth());
		// the actual scale may be different !
		info.setScale(mapPresenter.getViewPort().getZoomStrategy().checkScale(scale, ZoomOption.LEVEL_CLOSEST));

		info.setId(wmsLayer.getId());
		RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
		rasterInfo.setShowing(wmsLayer.isShowing());
		if (layer instanceof OpacitySupported) {
			rasterInfo.setCssStyle(((OpacitySupported) wmsLayer).getOpacity() + "");
		} else {
			rasterInfo.setCssStyle("1");
		}

		info.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo);
		return info;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof WmsLayer;
	}
}