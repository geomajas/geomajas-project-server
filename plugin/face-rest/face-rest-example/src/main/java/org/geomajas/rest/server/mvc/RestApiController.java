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
import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.dto.GeometryAreaRequest;
import org.geomajas.command.dto.GeometryBufferRequest;
import org.geomajas.command.dto.GeometryConvexHullRequest;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.SearchAttributesRequest;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.TransformGeometryRequest;
import org.geomajas.command.dto.UserMaximumExtentRequest;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.staticsecurity.command.dto.LoginRequest;
import org.geomajas.rest.server.command.CommandUtils;
import org.geomajas.rest.server.factory.RequestObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	/**
	 * UnknownCommandRequest type.
	 *
	 * @author Dosi Bingov
	 */
	class UnknownCommandRequest implements CommandRequest {
		private String message = "Unknown request type in RestApiController.generateCommandRequest()";

		public UnknownCommandRequest() {
		}

		public UnknownCommandRequest(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	private final Logger log = LoggerFactory.getLogger(RestApiController.class);

	public static final String REQUEST_OBJECT_URI = "generaterequest";

	@Autowired
	protected CommandDispatcher commandDispatcher;

	private RequestObjectsFactory requestObjectsFactory;

	@Autowired
	private ApplicationContext applicationContext;

	//generic command describe method
	@RequestMapping(value = REQUEST_OBJECT_URI + "/{commandId}", method = RequestMethod.GET)
	@ResponseBody
	public CommandRequest getCommandRequestExample(@PathVariable String commandId) throws JsonProcessingException {
		Command command = (Command) applicationContext.getBean(commandId);
		return generateCommandRequest(command);
	}

	/**
	 * We need same stub data for your json request ojects therefore gather from factory.
	 *
	 * @param command
	 * @return
	 */
	private CommandRequest generateCommandRequest(Command command) {
		CommandRequest request = CommandUtils.createCommandRequest(command);

		try {
			//TODO finish this nasty check
			if (request instanceof GetConfigurationRequest) {
				return requestObjectsFactory.generateConfigurationRequest();
			} else if (request instanceof GetMapConfigurationRequest) {
				return requestObjectsFactory.generateMapConfigurationRequest();
			} else if (request instanceof GeometryBufferRequest) {
				return requestObjectsFactory.generateGeometryBufferRequest();
			} else if (request instanceof UserMaximumExtentRequest) {
				return requestObjectsFactory.generateUserMaximumExtentRequest();
			} else if (request instanceof LoginRequest) {
				return requestObjectsFactory.generateLoginRequest();
			} else if (request instanceof GeometryMergeRequest) {
				return requestObjectsFactory.generateGeometryMergeRequest();
			} else if (request instanceof SearchFeatureRequest) {
				return requestObjectsFactory.generateSearchFeatureRequest();
			} else if (request instanceof GeometryConvexHullRequest) {
				return requestObjectsFactory.generateGeometryConvexHullRequest();
			} else if (request instanceof TransformGeometryRequest) {
				return requestObjectsFactory.generateTransformGeometryRequest();
			} else if (request instanceof GeometryAreaRequest) {
				return requestObjectsFactory.generateGeometryAreaRequest();
			} else if (request instanceof SearchAttributesRequest) {
				return requestObjectsFactory.generateSearchAttributesRequest();
			} else if (request instanceof SearchByLocationRequest) {
				return requestObjectsFactory.generateSearchByLocationService();
			} else {
				return new UnknownCommandRequest();
			}
		} catch (GeomajasException e) {
			return new UnknownCommandRequest(e.getMessage());
		}
	}

	@PostConstruct
	public void setUp() {
		requestObjectsFactory = new RequestObjectsFactory();
	}

}
