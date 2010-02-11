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
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

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

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private SecurityManager securityManager;

	private long commandCount;

	/**
	 * General command execution function.
	 * <p/>
	 * The security context is built for the authentication token. The security context is cleared again at the end of
	 * processing if the security context was changed.
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
		String id = Long.toString(++commandCount); // NOTE this is not thread safe
		// safe or cluster aware
		log.info("{} execute command {} for user token {}", new Object[] {id, commandName, userToken});
		long begin = System.currentTimeMillis();
		CommandResponse response;

		String previousToken = securityContext.getToken();
		boolean tokenIdentical;
		if (null == userToken) {
			tokenIdentical = false; // always need to *try* as otherwise login would never be possible
		} else {
			tokenIdentical = userToken.equals(previousToken);
		}
		try {
			if (!tokenIdentical) {
				// need to change security context
				securityManager.createSecurityContext(userToken);
			}

			// check access rights for the command
			System.out.println("security check on " + commandName + " for " + securityContext.getUserId());
			if (securityContext.isCommandAuthorized(commandName)) {

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
					try {
						command.execute(request, response);
					} catch (Throwable throwable) {
						response.getErrors().add(throwable);
					}
				} else {
					response = new CommandResponse();
					response.setId(id);
					response.getErrors().add(new GeomajasException(ExceptionCode.COMMAND_NOT_FOUND, commandName));
				}

			} else {
				// not authorized
				System.out.println("not authorized");
				response = new CommandResponse();
				response.setId(id);
				response.getErrors().add(new GeomajasException(ExceptionCode.COMMAND_ACCESS_DENIED,
						commandName, securityContext.getUserId()));
			}

			// now process the errors for display on the client
			List<Throwable> errors = response.getErrors();
			Locale localeObject = null;
			if (null != errors && !errors.isEmpty()) {
				for (Throwable t : errors) {
					if (!(t instanceof GeomajasException)) {
						log.error("unexpected " + t.getMessage(), t); // unexpected so log also
						String msg = t.getMessage();
						if (null == msg) {
							msg = t.getClass().getName();
						}
						response.getErrorMessages().add(msg);
					} else {
						if (null == localeObject && null != locale) {
							localeObject = new Locale(locale);
						}
						response.getErrorMessages().add(((GeomajasException) t).getMessage(localeObject));
					}
				}
			}
			response.setExecutionTime(System.currentTimeMillis() - begin);
			return response;
		} finally {
			if (!tokenIdentical) {
				// clear security context to previous
				securityManager.clearSecurityContext();
			}
		}
	}

}