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
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step which does the actual rasterizing. This is done by invoking the get getTile() call on the vector layer. The
 * {@link RasterTileStep} there will actually fill the {@link RasterizingContainer} object.
 * 
 * @author Joachim Van der Auwera
 */
public class RasterizeStep extends AbstractRasterizingStep {

	private final Logger log = LoggerFactory.getLogger(RasterizeStep.class);

	@Autowired
	private PipelineService<GetTileContainer> pipelineService;

	public void execute(PipelineContext rasterContext, RasterizingContainer rasterizingContainer)
			throws GeomajasException {
		// calls the vector pipeline with the rebuild key
		// Q: do we need a new context or can we pass our own raster context instead ?
		PipelineContext context = pipelineService.createContext();
		context.put(RasterizingPipelineCode.IMAGE_ID_KEY, rasterContext.get(RasterizingPipelineCode.IMAGE_ID_KEY));
		context.put(PipelineCode.LAYER_ID_KEY, rasterContext.get(PipelineCode.LAYER_ID_KEY));
		context.put(PipelineCode.LAYER_KEY, rasterContext.get(PipelineCode.LAYER_KEY));
		
		String layerId = context.get(PipelineCode.LAYER_ID_KEY, String.class);
		GetTileContainer response = new GetTileContainer();
		pipelineService.execute(RasterizingPipelineCode.PIPELINE_GET_VECTOR_TILE_RASTERIZING, layerId, context,
				response);
		rasterContext.put(PipelineCode.BOUNDS_KEY, response.getTile().getBounds());
		rasterContext.put(PipelineCode.TILE_METADATA_KEY, context.get(PipelineCode.TILE_METADATA_KEY));
		RasterizingContainer rc = context.get(RasterizingPipelineCode.CONTAINER_KEY, RasterizingContainer.class );
		rasterizingContainer.setImage(rc.getImage());
		log.debug("getTile response InternalTile {}", response);
	}
}
