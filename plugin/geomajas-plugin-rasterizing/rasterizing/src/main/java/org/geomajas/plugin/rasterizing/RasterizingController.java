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

package org.geomajas.plugin.rasterizing;

import org.geomajas.service.ConfigurationService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;

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

	@RequestMapping(value = "/rasterizing/**", method = RequestMethod.GET)
	public void getWms(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Search for the WMS layer:
		String layer = parseLayer(request);
		String key = parseKey(request);

		try {
			PipelineContext context = pipelineService.createContext();
			context.put(RasterizingPipelineCode.IMAGE_ID_KEY, key);
			context.put(PipelineCode.LAYER_ID_KEY, layer);
			context.put(PipelineCode.LAYER_KEY, configurationService.getVectorLayer(layer));
			RasterizingContainer rasterizeContainer = new RasterizingContainer();
			pipelineService.execute(RasterizingPipelineCode.PIPELINE_RASTERIZING, layer, context, rasterizeContainer);

			// Prepare the response:
			response.setContentType("image/png");
			response.getOutputStream().write(rasterizeContainer.getImage());
		} catch (Exception e) {
			log.error("Could not rasterize image " + key, e);
			response.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
	}

	/**
	 * Get the raster image cache key out of the request URL.
	 *
	 * @param request servlet request
	 * @return cache key
	 */
	private String parseKey(HttpServletRequest request) {
		StringTokenizer tokenizer = new StringTokenizer(request.getRequestURI(), "/.");
		String token = "";
		if (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
		}
		if (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
		}
		return token;
	}

	/**
	 * Get the layer id out of the request URL.
	 *
	 * @param request servlet request
	 * @return cache key
	 */
	private String parseLayer(HttpServletRequest request) {
		StringTokenizer tokenizer = new StringTokenizer(request.getRequestURI(), "/.");
		String token = "";
		if (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
		}
		return token;
	}


}
