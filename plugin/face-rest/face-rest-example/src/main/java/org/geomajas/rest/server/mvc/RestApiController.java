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
import org.geomajas.command.dto.GeometryBufferRequest;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.UserMaximumExtentRequest;
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
	public CommandRequest getCommandResponse(@PathVariable String commandId) throws JsonProcessingException {
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
		}  else if (request instanceof SearchFeatureRequest) {
			return requestObjectsFactory.generateSearchFeatureRequest();
		} else {
			CommandRequest emtyRequest = new CommandRequest() {
				private String content = "Unknown request type in RestApiController.generateCommandRequest()";

				@Override
				public int hashCode() {
					return super.hashCode();
				}

				public String getContent() {
					return content;
				}

				public void setContent(String content) {
					this.content = content;
				}
			};

			return emtyRequest;
		}
	}

	@PostConstruct
	public void setUp() {
		requestObjectsFactory = new RequestObjectsFactory();
	}

}
