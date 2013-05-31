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
package org.geomajas.plugin.wmsclient.printing.server.layer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.layer.RasterDirectLayer;
import org.geomajas.plugin.wmsclient.printing.server.dto.WmsClientLayerInfo;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.springframework.stereotype.Component;

/**
 * This factory creates a GeoTools layer that is capable of rendering WMS layers.
 * 
 * @author Jan De Moerloose
 */
@Component
public class WmsLayerFactory implements LayerFactory {

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof WmsClientLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof WmsClientLayerInfo)) {
			throw new IllegalArgumentException(
					"WmsLayerFactory.createLayer() should only be called using WmsClientLayerInfo");
		}
		WmsClientLayerInfo rasterInfo = (WmsClientLayerInfo) clientLayerInfo;
		RasterLayerRasterizingInfo extraInfo = (RasterLayerRasterizingInfo) rasterInfo
				.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY);
		List<RasterTile> tiles = rasterInfo.getTiles();
		RasterDirectLayer rasterLayer = new RasterDirectLayer(tiles, rasterInfo.getTileHeight(),
				rasterInfo.getTileWidth(), rasterInfo.getScale(), extraInfo.getCssStyle());
		rasterLayer.setTitle(clientLayerInfo.getLabel());
		rasterLayer.getUserData().put(USERDATA_KEY_LAYER_ID, rasterInfo.getId());
		rasterLayer.getUserData().put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		rasterLayer.getUserData().put("selectionInfo", rasterInfo.getSelectionInfo());
		return rasterLayer;
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		RasterLayerRasterizingInfo extraInfo = (RasterLayerRasterizingInfo) clientLayerInfo
				.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY);
		userData.put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		return userData;
	}
}