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
import org.geomajas.command.CommandResponse;
import org.geomajas.rest.server.command.CommandUtils;
import org.geomajas.rest.server.json.dto.CommandDescribeDto;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Spring controller for json Geomajas command requests.
 * 
 * @author Dosi Bingov
 * 
 */
@Controller("/rest/**")
public class JsonController extends RestController {

	private final Logger log = LoggerFactory.getLogger(JsonController.class);

	@Autowired
	protected CommandDispatcher commandDispatcher;

	@Autowired
	private Map<String, Command> commands;

	@Autowired
	protected SecurityContext securityContext;

	private static final String COMMAND_URI = "/command/";

	@Autowired
	private ApplicationContext applicationContext;

	//generic rest command method
	@RequestMapping(value = COMMAND_URI + "{commandId}", method = RequestMethod.POST)
	@ResponseBody
	public CommandResponse execute(@PathVariable String commandId, @RequestBody HashMap commandRequest,
			HttpServletRequest request, @RequestParam("token") String token) throws JsonProcessingException {
		return getJsonResponse("command.configuration.Get", commandRequest, token, request);
	}

	//generic command describe method
	@RequestMapping(value = COMMAND_URI + "describe/{commandId}", method = RequestMethod.GET)
	@ResponseBody
	public CommandDescribeDto getCommandDescription(@PathVariable String commandId) throws JsonProcessingException {
		Command command = (Command) applicationContext.getBean(commandId);
		return new CommandDescribeDto(CommandUtils.createCommandRequest(command),
				CommandUtils.createCommandResponse(command));
	}

	//generic configuration get command without security
	@RequestMapping(value = COMMAND_URI + "configuration/{applicationId}", method = RequestMethod.GET)
	@ResponseBody
	public CommandResponse getConfiguration(HttpServletRequest request, @PathVariable String applicationId)
			throws JsonProcessingException {
		HashMap<String, String> jsonRequest = new HashMap<String, String>();
		jsonRequest.put("applicationId", applicationId);

		return getJsonResponse("command.configuration.Get", jsonRequest, null, request);
	}

	//get all available commands
	@RequestMapping(value = COMMAND_URI + "list", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> listAllCommands() throws JsonProcessingException {
		return commands.keySet();
	}

	private CommandResponse getJsonResponse(String commandId,
			HashMap<String, String> jsonRequest, String token, HttpServletRequest request) {
		String locale = request.getLocale().getLanguage();

		CommandResponse commandResponse = null;

		try {
			Command command = (Command) applicationContext.getBean(commandId);
			CommandRequest requestObject = CommandUtils.createCommandRequest(command);
			//throws InvocationTargetException
			org.apache.commons.beanutils.BeanUtils.populate(requestObject, jsonRequest);
			commandResponse = commandDispatcher.execute(commandId, requestObject, token, locale);

		} catch (IllegalStateException e) {
			e.printStackTrace();
			commandResponse = new CommandResponse();
			commandResponse.getErrorMessages().add(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			commandResponse = new CommandResponse();
			commandResponse.getErrorMessages().add(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			commandResponse = new CommandResponse();
			commandResponse.getErrorMessages().add(e.getMessage());
		} finally {
			return commandResponse;
		}
	}

}
