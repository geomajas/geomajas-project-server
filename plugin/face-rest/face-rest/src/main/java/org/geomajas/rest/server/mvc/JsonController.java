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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GeometryAreaRequest;
import org.geomajas.command.dto.GeometryBufferRequest;
import org.geomajas.command.dto.GeometryConvexHullRequest;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GeometrySplitRequest;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.RefreshConfigurationRequest;
import org.geomajas.command.dto.RegisterNamedStyleInfoRequest;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.plugin.staticsecurity.command.dto.LoginResponse;
import org.geomajas.plugin.staticsecurity.command.staticsecurity.LoginCommand;
import org.geomajas.rest.server.json.mixin.ResponseMixin;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Spring controller for json Geomajas command requests.
 * 
 * @author Dosi Bingov
 * 
 */
@Controller("/json/**")
public class JsonController {

	private final Logger log = LoggerFactory.getLogger(JsonController.class);

	@Autowired
	protected CommandDispatcher commandDispatcher;

	@Autowired
	protected SecurityContext securityContext;

	@Autowired
	private LoginCommand loginCommand;


	@Autowired
	private ApplicationContext applicationContext;


	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> printGet(@RequestParam("cmd") String cmd) throws GeomajasException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String json = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			json = ow.writeValueAsString(new GetConfigurationRequest());
		} catch (JsonProcessingException e) {
			e.printStackTrace();

		}

		return new ResponseEntity<String>(cmd, headers, HttpStatus.OK);
	}


	//Configuration rest
	@RequestMapping(value = "/configuration", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse configuration(@RequestBody GetConfigurationRequest configurationRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GetConfigurationRequest.COMMAND, configurationRequest, token, request);
	}

	@RequestMapping(value = "/configuration/map", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse mapConfiguration(@RequestBody GetMapConfigurationRequest configurationRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GetMapConfigurationRequest.COMMAND, configurationRequest, token, request);
	}

	@RequestMapping(value = "/configuration/refresh", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse refreshConfiguration(@RequestBody RefreshConfigurationRequest configurationRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(RefreshConfigurationRequest.COMMAND, configurationRequest, token, request);
	}

	@RequestMapping(value = "/configuration/maximumextend", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse maximumExtendConfiguration(@RequestBody UserMaximumExtentRequest configurationRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(UserMaximumExtentRequest.COMMAND, configurationRequest, token, request);
	}

	@RequestMapping(value = "/geometry/area", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse geometryArea(@RequestBody GeometryAreaRequest geometryAreaRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GeometryAreaRequest.COMMAND, geometryAreaRequest, token, request);
	}

	//Geometry rest
	@RequestMapping(value = "/geometry/buffer", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse geometryBuffer(@RequestBody GeometryBufferRequest geometryBufferRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GeometryBufferRequest.COMMAND, geometryBufferRequest, token, request);
	}

	@RequestMapping(value = "/geometry/convexhull", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse geometryConvexHull(@RequestBody GeometryConvexHullRequest geometryConvexHullRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GeometryConvexHullRequest.COMMAND, geometryConvexHullRequest, token, request);
	}

	@RequestMapping(value = "/geometry/merge", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse geometryMerge(@RequestBody GeometryMergeRequest geometryMergeRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GeometryMergeRequest.COMMAND, geometryMergeRequest, token, request);
	}

	@RequestMapping(value = "/geometry/split", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse geometrySplit(@RequestBody GeometrySplitRequest geometrySplitRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GeometrySplitRequest.COMMAND, geometrySplitRequest, token, request);
	}

	@RequestMapping(value = "/geometry/transform", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse geometryTransform(@RequestBody TransformGeometryRequest transformGeometryRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(TransformGeometryRequest.COMMAND, transformGeometryRequest, token, request);
	}

	//Render rest
	@RequestMapping(value = "/render/rastertiles", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse renderRasterTiles(@RequestBody GetRasterTilesRequest rasterTilesRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GetRasterTilesRequest.COMMAND, rasterTilesRequest, token, request);
	}

	@RequestMapping(value = "/render/vectortiles", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse renderVectorTiles(@RequestBody GetVectorTileRequest vectorTileRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(GetVectorTileRequest.COMMAND, vectorTileRequest, token, request);
	}

	@RequestMapping(value = "/render/namedstyle", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse renderNamedStyle(@RequestBody RegisterNamedStyleInfoRequest namedStyleInfoRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse(RegisterNamedStyleInfoRequest.COMMAND, namedStyleInfoRequest, token, request);
	}


	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse login(@RequestBody LoginRequest loginRequest) throws JsonProcessingException {
		LoginResponse response = loginCommand.getEmptyCommandResponse();

		try {
			loginCommand.execute(loginRequest, response);
		} catch (Exception e) {
					response.getErrorMessages().add(e.getMessage());
			return response;
		}
		return response;
	}

	/**
	 * Main logic that produces json string of a command response.
	 *
	 * @param commandRequest
	 * @param request
	 * @param commandName package of the command that need to be resolved
	 * @return
	 * @throws JsonProcessingException
	 */
	private ResponseEntity<String> getJsonEntity(String commandName,
			CommandRequest commandRequest, HttpServletRequest request)
			throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		//set type of response
		headers.setContentType(MediaType.APPLICATION_JSON);

		String jsonResponse = "";


	   ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// abstract info classes
	/*	objectMapper.addMixInAnnotations(AbstractAttributeInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(ClientLayerInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(ClientUserDataInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(ClientWidgetInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(ConstraintInfo.class, TypeInfoMixin.class);
		objectMapper.addMixInAnnotations(LayerExtraInfo.class, TypeInfoMixin.class);*/
		objectMapper.addMixInAnnotations(CommandResponse.class, ResponseMixin.class);

		try {
			String locale = request.getLocale().getLanguage();

			CommandResponse response =
					commandDispatcher.execute(commandName, commandRequest,
							securityContext.getToken(), locale);

			jsonResponse = objectMapper.writeValueAsString(response);
		} catch (Exception e) {
			//if exception accurs
			CommandResponse commandResponse = new CommandResponse();
			commandResponse.getErrorMessages().add(e.getMessage());
			jsonResponse = objectMapper.writeValueAsString(commandResponse);
		}

		return new ResponseEntity<String>(jsonResponse, headers, HttpStatus.OK);
	}

	private CommandResponse getJsonResponse(String commandName,
			CommandRequest commandRequest, String token, HttpServletRequest request) {
			String locale = request.getLocale().getLanguage();

			CommandResponse response =
					commandDispatcher.execute(commandName, commandRequest, token, locale);

			return response;
	}

}
