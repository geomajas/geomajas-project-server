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
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.global.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.GeomajasSecurityException;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.RasterLayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that retrieves raster data. The result contains a list of raster tiles. Basic assumption is that
 * raster data is tiled in a pyramid structure based on to the layer's maximum extent and tile level. The optimum tile
 * level for the given scale is determined first. Once the tile level is known, it should be a piece of cake to
 * determine the indexes. The client can cache as many tiles as it likes.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
@Api
@Component()
public class GetRasterTilesCommand implements Command<GetRasterTilesRequest, GetRasterTilesResponse> {

	private final Logger log = LoggerFactory.getLogger(GetRasterTilesCommand.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private RasterLayerService layerService;

	public GetRasterTilesResponse getEmptyCommandResponse() {
		return new GetRasterTilesResponse();
	}

	public void execute(GetRasterTilesRequest request, GetRasterTilesResponse response) throws Exception {
		log.debug("request start layer {}, crs {}", request.getLayerId(), request.getCrs());
		String layerId = request.getLayerId();
		if (null == layerId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		if (!securityContext.isLayerVisible(layerId)) {
			throw new GeomajasSecurityException(ExceptionCode.LAYER_NOT_VISIBLE, layerId, securityContext.getUserId());
		}

		log.debug("execute() : bbox {}", request.getBbox());
		List<RasterTile> images = layerService.getTiles(layerId, configurationService.getCrs(request.getCrs()),
				converterService.toInternal(request.getBbox()),	request.getScale());
		log.debug("execute() : returning {} images", images.size());

		response.setRasterData(images);
		if (images.size() > 0) {
			response.setNodeId(layerId + "." + images.get(0).getCode().getTileLevel());
		}
	}

}
