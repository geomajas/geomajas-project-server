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
package org.geomajas.plugin.rasterizing.step;

import java.awt.Rectangle;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.LayerFactoryService;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.pipeline.PipelineContext;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.map.MapViewport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step which adds all the layers to the map context and prepares the context for rendering.
 * 
 * @author Jan De Moerloose
 */
public class AddLayersStep extends AbstractRasterizingStep {

	@Autowired
	private LayerFactoryService layerFactoryService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	public void execute(PipelineContext context, RasterizingContainer response) throws GeomajasException {
		ClientMapInfo clientMapInfo = context.get(RasterizingPipelineCode.CLIENT_MAP_INFO_KEY, ClientMapInfo.class);
		MapContext mapContext = context.get(RasterizingPipelineCode.MAP_CONTEXT_KEY, MapContext.class);
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) clientMapInfo
				.getWidgetInfo(MapRasterizingInfo.WIDGET_KEY);
		mapContext.getUserData().put(LayerFactory.USERDATA_RASTERIZING_INFO, mapRasterizingInfo);
		Crs mapCrs = geoService.getCrs2(clientMapInfo.getCrs());
		ReferencedEnvelope mapArea = new ReferencedEnvelope(
				converterService.toInternal(mapRasterizingInfo.getBounds()), mapCrs);
		Rectangle paintArea = new Rectangle((int) (mapRasterizingInfo.getScale() * mapArea.getWidth()),
				(int) (mapRasterizingInfo.getScale() * mapArea.getHeight()));
		MapViewport viewPort = mapContext.getViewport();
		viewPort.setBounds(mapArea);
		viewPort.setCoordinateReferenceSystem(mapCrs);
		viewPort.setScreenArea(paintArea);
		// add the configured layers
		for (ClientLayerInfo clientLayerInfo : clientMapInfo.getLayers()) {
			boolean showing = (Boolean) layerFactoryService.getLayerUserData(mapContext, clientLayerInfo).get(
					LayerFactory.USERDATA_KEY_SHOWING);
			if (showing) {
				Layer layer = layerFactoryService.createLayer(mapContext, clientLayerInfo);
				mapContext.addLayer(layer);
			}
		}
		// add the extra layers
		for (ClientLayerInfo clientLayerInfo : mapRasterizingInfo.getExtraLayers()) {
			boolean showing = (Boolean) layerFactoryService.getLayerUserData(mapContext, clientLayerInfo).get(
					LayerFactory.USERDATA_KEY_SHOWING);
			if (showing) {
				Layer layer = layerFactoryService.createLayer(mapContext, clientLayerInfo);
				mapContext.addLayer(layer);
			}
		}
	}

}
