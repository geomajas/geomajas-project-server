/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.rasterizing;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.mvc.RasterizingController;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that creates an image of the map and the legend, adds it to the cache and returns an URL for both images.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api
@Component
public class RasterizeMapCommand implements Command<RasterizeMapRequest, RasterizeMapResponse> {

	private static final int MAP_BUFFER_SIZE = 1024 * 10;

	private static final int MAP_LEGEND_SIZE = 1024;

	@Autowired
	private ImageService imageService;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	public void execute(RasterizeMapRequest request, RasterizeMapResponse response) throws Exception {
		ClientMapInfo mapInfo = request.getClientMapInfo();
		if (null == mapInfo) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "clientMapInfo");
		}
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) mapInfo
				.getWidgetInfo(MapRasterizingInfo.WIDGET_KEY);
		if (mapRasterizingInfo == null) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapRasterizingInfo");
		}
		for (ClientLayerInfo clientLayer : mapInfo.getLayers()) {
			if (clientLayer instanceof ClientVectorLayerInfo) {
				if (null == clientLayer.getWidgetInfo(VectorLayerRasterizingInfo.WIDGET_KEY)) {
					throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "vectorLayerRasterizingInfo ("
							+ clientLayer.getId() + ")");
				}
			} else if (clientLayer instanceof ClientRasterLayerInfo) {
				if (null == clientLayer.getWidgetInfo(RasterLayerRasterizingInfo.WIDGET_KEY)) {
					throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "rasterLayerRasterizingInfo ("
							+ clientLayer.getId() + ")");
				}
			}
		}
		ByteArrayOutputStream mapStream = new ByteArrayOutputStream(MAP_BUFFER_SIZE);
		imageService.writeMap(mapStream, request.getClientMapInfo());
		String mapKey = putInCache(mapStream.toByteArray(), mapRasterizingInfo.getBounds());
		ByteArrayOutputStream legendStream = new ByteArrayOutputStream(MAP_LEGEND_SIZE);
		imageService.writeLegend(legendStream, request.getClientMapInfo());
		String legendKey = putInCache(legendStream.toByteArray(), mapRasterizingInfo.getBounds());
		// remove last '/' from dispatcher URL
		String baseUrl = dispatcherUrlService.getDispatcherUrl().substring(0,
				dispatcherUrlService.getDispatcherUrl().length() - 1);
		response.setMapKey(mapKey);
		response.setMapUrl(baseUrl + RasterizingController.IMAGE_MAPPING + mapKey + ".png");
		response.setLegendKey(legendKey);
		response.setLegendUrl(baseUrl + RasterizingController.IMAGE_MAPPING + legendKey + ".png");
	}

	private String putInCache(byte[] byteArray, Bbox bounds) {
		RasterizingContainer container = new RasterizingContainer();
		container.setImage(byteArray);
		String key = UUID.randomUUID().toString();
		cacheManagerService.put(null, CacheCategory.RASTER, key, container, dtoConverterService.toInternal(bounds));
		return key;
	}

	public RasterizeMapResponse getEmptyCommandResponse() {
		return new RasterizeMapResponse();
	}

}