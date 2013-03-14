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
package org.geomajas.plugin.printing.client.widget;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterServerLayer;

/**
 * {@link PrintableLayerBuilder} for {@link RasterServerLayer} instances.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterServerLayerBuilder implements PrintableLayerBuilder {

	@Override
	public ClientLayerInfo build(MapPresenter mapPresenter, Layer layer) {
		RasterServerLayer rasterServerLayer = (RasterServerLayer) layer;
		ClientRasterLayerInfo layerInfo = (ClientRasterLayerInfo) rasterServerLayer.getLayerInfo();
		RasterLayerRasterizingInfo rasterInfo = new RasterLayerRasterizingInfo();
		rasterInfo.setShowing(rasterServerLayer.isShowing());
		rasterInfo.setCssStyle(rasterServerLayer.getOpacity() + "");
		layerInfo.getWidgetInfo().put(RasterLayerRasterizingInfo.WIDGET_KEY, rasterInfo);
		return layerInfo;
	}

	@Override
	public boolean supports(Layer layer) {
		return layer instanceof RasterServerLayer;
	}

}
