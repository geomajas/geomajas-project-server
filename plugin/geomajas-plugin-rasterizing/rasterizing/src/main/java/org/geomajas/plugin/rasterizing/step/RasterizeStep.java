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

package org.geomajas.plugin.rasterizing.step;

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
 * @author Jan De Moerloose
 */
public class RasterizeStep extends AbstractRasterizingStep {

	private final Logger log = LoggerFactory.getLogger(RasterizeStep.class);

	@Autowired
	private PipelineService<GetTileContainer> pipelineService;

	public void execute(PipelineContext rasterContext, RasterizingContainer rasterizingContainer)
			throws GeomajasException {

		// calls the vector pipeline with the rebuild key
		// reuse context, no reason to create a new one

		String layerId = rasterContext.get(PipelineCode.LAYER_ID_KEY, String.class);
		GetTileContainer response = new GetTileContainer();
		pipelineService.execute(RasterizingPipelineCode.PIPELINE_GET_VECTOR_TILE_RASTERIZING, layerId, rasterContext,
				response);
		rasterContext.put(PipelineCode.BOUNDS_KEY, response.getTile().getBounds());
		RasterizingContainer rc = rasterContext.get(RasterizingPipelineCode.CONTAINER_KEY, RasterizingContainer.class );
		rasterizingContainer.setImage(rc.getImage());
		log.debug("getTile response InternalTile {}", response);
	}
}
