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

import org.geomajas.command.Command;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.command.dto.GetRenderedTileRequest;
import org.geomajas.command.dto.GetRenderedTileResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.strategy.RenderingStrategy;
import org.geomajas.rendering.strategy.RenderingStrategyFactory;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to retrieve a feature tile.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Transactional(readOnly = true)
public class GetRenderedTileCommand implements Command<GetRenderedTileRequest, GetRenderedTileResponse> {

	private final Logger log = LoggerFactory.getLogger(GetRenderedTileCommand.class);

	@Autowired
	private ApplicationService runtimeParameters;

	@Autowired
	private ApplicationInfo application;

	@Autowired
	private RenderingStrategyFactory renderingStrategyFactory;

	@Autowired
	private DtoConverterService converter;

	public GetRenderedTileResponse getEmptyCommandResponse() {
		return new GetRenderedTileResponse();
	}

	public void execute(GetRenderedTileRequest request, GetRenderedTileResponse response) throws Exception {
		log.debug("request start layer {}, crs {}", request.getLayerId(), request.getCrs());
		if (null == request.getLayerId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}
		VectorLayer vLayer = runtimeParameters.getVectorLayer(request.getLayerId());
		if (vLayer == null) {
			throw new GeomajasException(ExceptionCode.LAYER_NOT_FOUND, request.getLayerId());
		}
		RenderingStrategy strategy = renderingStrategyFactory.createRenderingStrategy(vLayer.getLayerInfo(), request);
		InternalTile tile = strategy.paint(request, application);
		response.setTile(converter.toDto(tile));
	}
}