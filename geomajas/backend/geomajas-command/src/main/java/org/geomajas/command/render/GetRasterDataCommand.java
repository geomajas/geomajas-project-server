/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.command.render;

import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.GetRasterDataRequest;
import org.geomajas.command.dto.GetRasterDataResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GetRasterDataCommand
 * <p/>
 * <p>
 * Command that retrieves raster data. The result contains a list of raster images or tiles. Basic assumption is that
 * raster data is tiled in a pyramid structure based on to the layer's maximum extent and tile level. The optimum tile
 * level for the given scale is determined first. Once the tile level is known, it should be a piece of cake to
 * determine the indices. The client can cache as many tiles as it likes.
 * <p/>
 * </p>
 * 
 * @author Pieter De Graef, Jan De Moerloose
 */
@Component()
public class GetRasterDataCommand implements Command<GetRasterDataRequest, GetRasterDataResponse> {

	private final Logger log = LoggerFactory.getLogger(GetRasterDataCommand.class);

	@Autowired
	private ApplicationService runtimeParameters;

	@Autowired
	private DtoConverterService converterService;

	public GetRasterDataResponse getEmptyCommandResponse() {
		return new GetRasterDataResponse();
	}

	public void execute(GetRasterDataRequest request, GetRasterDataResponse response) throws Exception {
		log.debug("request start layer {}, crs {}", request.getLayerId(), request.getCrs());
		if (null == request.getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		RasterLayer rasterlayer = (RasterLayer) runtimeParameters.getLayer(request.getLayerId());
		log.debug("execute() : bbox {}", request.getBbox());
		List<RasterTile> images = rasterlayer.paint(request.getCrs(), converterService.toInternal(request.getBbox()),
				request.getScale());
		log.debug("execute() : returning {} images", images.size());
		response.setRasterData(images);
		if (images.size() > 0) {
			response.setNodeId(rasterlayer.getLayerInfo().getId() + "." + images.get(0).getCode().getTileLevel());
		}
	}

}
