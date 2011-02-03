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
package org.geomajas.plugin.rasterizing;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Pipeline step which creates the image URL of the raster.
 * F
 * @author Jan De Moerloose
 *
 */
public class RasterUrlStep implements PipelineStep<GetTileContainer> {

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext context, GetTileContainer response) throws GeomajasException {
		InternalTile tile = response.getTile();
		tile.setContentType(VectorTile.VectorTileContentType.URL_CONTENT);
		String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
		String cacheKey = context.get(RasterizingPipelineCode.IMAGE_ID_KEY, String.class);
		StringBuilder url = new StringBuilder(200);
		url.append(dispatcherUrlService.getDispatcherUrl());
		url.append("/rasterizing/");
		url.append(layerId);
		url.append("/");
		url.append(cacheKey);
		url.append(".png");
		tile.setFeatureContent(url.toString());
	}

}
