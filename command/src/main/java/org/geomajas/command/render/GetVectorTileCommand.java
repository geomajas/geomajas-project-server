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

package org.geomajas.command.render;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayerService;
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
 * @since 1.6.0
 */
@Api
@Component()
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetVectorTileCommand implements Command<GetVectorTileRequest, GetVectorTileResponse> {

	private final Logger log = LoggerFactory.getLogger(GetVectorTileCommand.class);

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private VectorLayerService layerService;

	@Override
	public GetVectorTileResponse getEmptyCommandResponse() {
		return new GetVectorTileResponse();
	}

	@Override
	public void execute(GetVectorTileRequest request, GetVectorTileResponse response) throws Exception {
		String layerId = request.getLayerId();
		log.debug("request start layer {}, crs {}", layerId, request.getCrs());
		if (null == layerId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "layer");
		}
		if (null == request.getCrs()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "crs");
		}

		response.setTile(converter.toDto(layerService.getTile(request)));
	}
}