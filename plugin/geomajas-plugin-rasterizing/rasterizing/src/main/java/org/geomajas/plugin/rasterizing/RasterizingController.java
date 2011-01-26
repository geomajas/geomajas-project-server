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

import javax.servlet.http.HttpServletResponse;

import org.geomajas.service.ConfigurationService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller which serves the actual rasterized images.
 *
 * @author Joachim Van der Auwera
 */
@Controller("/rasterizing/**")
public class RasterizingController {

	private final Logger log = LoggerFactory.getLogger(RasterizingController.class);

	@Autowired
	private PipelineService<RasterizingContainer> pipelineService;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/rasterizing/{layerId}/{key}.png", method = RequestMethod.GET)
	public void getWms(@PathVariable String layerId, @PathVariable String key,
			HttpServletResponse response) throws Exception {

		try {
			PipelineContext context = pipelineService.createContext();
			context.put(RasterizingPipelineCode.IMAGE_ID_KEY, key);
			context.put(PipelineCode.LAYER_ID_KEY, layerId);
			context.put(PipelineCode.LAYER_KEY, configurationService.getVectorLayer(layerId));
			RasterizingContainer rasterizeContainer = new RasterizingContainer();
			pipelineService.execute(RasterizingPipelineCode.PIPELINE_RASTERIZING, layerId, context, rasterizeContainer);

			// Prepare the response:
			response.setContentType("image/png");
			response.getOutputStream().write(rasterizeContainer.getImage());
		} catch (Exception e) {
			log.error("Could not rasterize image " + key, e);
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
	}

}
