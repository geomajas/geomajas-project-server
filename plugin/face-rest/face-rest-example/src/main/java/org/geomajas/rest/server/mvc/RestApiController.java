/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.rest.server.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.global.GeomajasException;
import org.geomajas.rest.server.factory.RequestObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

/**
 * Spring controller for json command requests.
 *
 * @author Dosi Bingov
 */
@Controller("/restapi/**")
public class RestApiController {

	private final Logger log = LoggerFactory.getLogger(RestApiController.class);

	public static final String REQUEST_OBJECT_URI = "generaterequest";

	@Autowired
	protected CommandDispatcher commandDispatcher;

	private RequestObjectsFactory requestObjectsFactory;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getAll() throws GeomajasException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String json = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			json = ow.writeValueAsString(new GetConfigurationRequest());
		} catch (JsonProcessingException e) {
			e.printStackTrace();

		}

		return new ResponseEntity<String>("", headers, HttpStatus.OK);
	}

	//Configuration rest
	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/configuration", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest configuration() throws JsonProcessingException {
		return requestObjectsFactory.generateConfigurationRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/configuration/map", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest mapConfiguration() throws JsonProcessingException {
		return requestObjectsFactory.generateMapConfigurationRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/configuration/refresh", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest configurationRefresh() throws JsonProcessingException {
		return requestObjectsFactory.generateRefreshConfigurationRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/configuration/maximumextend", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest maximumExtendConfiguration() throws JsonProcessingException {
		return requestObjectsFactory.generateUserMaximumExtentRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/geometry/area", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest geometryArea() throws JsonProcessingException {
		return requestObjectsFactory.generateGeometryAreaRequest();
	}

	//Geometry rest
	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/geometry/buffer", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest geometryBuffer() throws JsonProcessingException {
		return requestObjectsFactory.generateGeometryBufferRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/geometry/convexhull", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest geometryConvexHull() throws JsonProcessingException {
		return requestObjectsFactory.generateGeometryConvexHullRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/geometry/merge", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest geometryMerge() throws JsonProcessingException {
		return requestObjectsFactory.generateGeometryMergeRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/geometry/split", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest geometrySplit() throws JsonProcessingException {
		return requestObjectsFactory.generateGeometrySplitRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/geometry/transform", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest geometryTransform() throws JsonProcessingException {
		return requestObjectsFactory.generateTransformGeometryRequest();
	}

	//Render rest
	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/render/rastertiles", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest renderRasterTiles() throws JsonProcessingException {
		return requestObjectsFactory.generateGetRasterTilesRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/render/vectortiles", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest renderVectorTiles() throws JsonProcessingException {
		return requestObjectsFactory.generateGetVectorTileRequest();
	}

	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/render/namedstyle", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest renderNamedStyle() throws JsonProcessingException {
		return requestObjectsFactory.generateNamedStyleInfoRequest();
	}

	//Login rest
	@RequestMapping(value = "/" + REQUEST_OBJECT_URI + "/login", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest login() throws JsonProcessingException {
		return requestObjectsFactory.generateLoginRequest();
	}

	@PostConstruct
	public void setUp() {
		requestObjectsFactory = new RequestObjectsFactory();
	}

}
