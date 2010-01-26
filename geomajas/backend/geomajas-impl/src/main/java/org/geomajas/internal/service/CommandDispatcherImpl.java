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
package org.geomajas.internal.service;

import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The <code>CommandDispatcher</code> is the main command execution center. It
 * accepts command from the client and executes them on the server.
 *
 * @author Joachim Van der Auwera
 */
@Component()
public final class CommandDispatcherImpl implements CommandDispatcher {

	private static final long serialVersionUID = -1334372707950671271L;
	private final Logger log = LoggerFactory
			.getLogger(CommandDispatcherImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ApplicationInfo application;

	private long commandCount;

	/**
	 * General command execution function.
	 *
	 * @param commandName
	 *            name of command to execute
	 * @param request
	 *            {@link CommandRequest} object for the command (if any)
	 * @param userToken
	 *            token to identify user
	 * @param locale
	 *            which should be used for the error messages in the response
	 * @return {@link CommandResponse} command response
	 */
	public CommandResponse execute(String commandName, CommandRequest request,
			String userToken, String locale) {
		String id = Long.toString(++commandCount); // @todo this is not thread
		// safe or cluster aware
		log.info("{} execute command {} for user token {}", new Object[] {id, commandName, userToken});
		long begin = System.currentTimeMillis();
		CommandResponse response;

		// @todo security check, verify validity of userToken and check access
		// rights for the command
		Command command = null;
		try {
			command = applicationContext.getBean(commandName, Command.class);
		} catch (BeansException be) {
			log.error("could not create command bean for {}",
					new Object[] {commandName}, be);
		}
		if (null != command) {
			response = command.getEmptyCommandResponse();
			response.setId(id);
			// @todo the interceptors still need to be handled here. For proper
			// (exception) handling, this should be treated as an interceptor
			// chain instead of a list
			try {
				command.execute(request, response);
			} catch (Throwable throwable) {
				response.getErrors().add(throwable);
			}
		} else {
			response = new CommandResponse();
			response.setId(id);
			response.getErrors().add(
					new GeomajasException(ExceptionCode.COMMAND_NOT_FOUND,
							commandName));
		}

		// now process the errors for display on the client
		List<Throwable> errors = response.getErrors();
		if (null != errors && !errors.isEmpty()) {
			for (Throwable t : errors) {
				if (!(t instanceof GeomajasException)) {
					log.error("unexpected " + t.getMessage(), t);
				}
				// @todo need to use the properly translated version in case of
				// GeomajasException (and assure it contains cause)
				response.getErrorMessages().add(t.getMessage());
			}
		}
		response.setExecutionTime(System.currentTimeMillis() - begin);
		return response;
	}

}