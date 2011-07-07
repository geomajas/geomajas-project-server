/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.rasterizing;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.geomajas.annotations.Api;
import org.geomajas.command.Command;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapRequest;
import org.geomajas.plugin.rasterizing.command.dto.RasterizeMapResponse;
import org.geomajas.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api
@Component
public class RasterizeMapCommand implements Command<RasterizeMapRequest, RasterizeMapResponse> {

	@Autowired
	private ImageService imageService;

	@Autowired
	private CacheManagerService cacheManagerService;

	@Autowired
	private DtoConverterService dtoConverterService;

	public void execute(RasterizeMapRequest request, RasterizeMapResponse response) throws Exception {
		MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) request.getClientMapInfo().getWidgetInfo(
				MapRasterizingInfo.WIDGET_KEY);
		ByteArrayOutputStream mapStream = new ByteArrayOutputStream(1024 * 10);
		imageService.writeMap(mapStream, request.getClientMapInfo());
		String mapKey = putInCache(mapStream.toByteArray(), mapRasterizingInfo.getBounds());
		ByteArrayOutputStream legendStream = new ByteArrayOutputStream(1024);
		imageService.writeLegend(legendStream, request.getClientMapInfo());
		String legendKey = putInCache(legendStream.toByteArray(), mapRasterizingInfo.getBounds());
		response.setMapKey(mapKey);
		response.setLegendKey(legendKey);
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