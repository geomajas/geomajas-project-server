/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command.render;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetRasterTilesResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Command that retrieves raster data. The result contains a list of raster tiles. Basic assumption is that
 * raster data is tiled in a pyramid structure based on to the layer's maximum extent and tile level. The optimum tile
 * level for the given scale is determined first. Once the tile level is known, it should be a piece of cake to
 * determine the indices. The client can cache as many tiles as it likes.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
@Component()
public class GetRasterTilesCommand implements Command<GetRasterTilesRequest, GetRasterTilesResponse> {

	private final Logger log = LoggerFactory.getLogger(GetRasterTilesCommand.class);

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private RasterLayerService layerService;

	@Autowired
	private GeoService geoService;

	/** {@inheritDoc} */
	public GetRasterTilesResponse getEmptyCommandResponse() {
		return new GetRasterTilesResponse();
	}

	/** {@inheritDoc} */
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
		List<RasterTile> images = layerService.getTiles(layerId, geoService.getCrs2(request.getCrs()),
				converterService.toInternal(request.getBbox()),	request.getScale());
		log.debug("execute() : returning {} images", images.size());

		response.setRasterData(images);
		if (images.size() > 0) {
			response.setNodeId(layerId + "." + images.get(0).getCode().getTileLevel());
		}
	}

}
