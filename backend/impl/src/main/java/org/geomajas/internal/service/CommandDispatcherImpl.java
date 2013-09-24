/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.service;

import java.util.List;
import java.util.Locale;

import org.geomajas.command.Command;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.ExceptionDto;
import org.geomajas.global.GeomajasException;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.security.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The <code>CommandDispatcher</code> is the main command execution center. It accepts command from the client and
 * executes them on the server.
 * 
 * @author Joachim Van der Auwera
 */
@Component()
public final class CommandDispatcherImpl implements CommandDispatcher {

	private static final long serialVersionUID = -1334372707950671271L;
	private static final String MSG_START = " execute ";

	private final Logger log = LoggerFactory.getLogger(CommandDispatcherImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

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
	@SuppressWarnings("unchecked")
	public CommandResponse execute(String commandName, CommandRequest request, String userToken, String locale) {
		String id = Long.toString(++commandCount); // NOTE indicative, this is not (fully) thread safe or cluster aware
		log.info(id + MSG_START + commandName + " for user token " + userToken + " in locale " + locale);
		if (log.isTraceEnabled()) {
			log.trace(id + MSG_START + commandName + " request " + request);
		}
		long begin = System.currentTimeMillis();
		CommandResponse response;

		String previousToken = securityContext.getToken();
		// for null token we always need to *try* as otherwise login would never be possible
		boolean tokenIdentical = null != userToken && userToken.equals(previousToken);
		try {
			if (!tokenIdentical) {
				// need to change security context
				if (!securityManager.createSecurityContext(userToken)) {
					// not authorized
					response = new CommandResponse();
					response.setId(id);

					GeomajasSecurityException credentialsException =
							new GeomajasSecurityException(ExceptionCode.CREDENTIALS_MISSING_OR_INVALID, commandName,
									securityContext.getUserId());

					Locale localeObject = null;
					if (null != locale) {
						localeObject = new Locale(locale);
					}

					String msg = getErrorMessage(credentialsException, localeObject);
					if (log.isDebugEnabled()) {
						log.debug(id + MSG_START + commandName + ", " + msg, credentialsException);
					}

					response.getErrorMessages().add(msg);
					response.getExceptions().add(toDto(credentialsException, localeObject, msg));
					response.setExecutionTime(System.currentTimeMillis() - begin);
				}
			}

			// check access rights for the command
			if (securityContext.isCommandAuthorized(commandName)) {

				Command command = null;
				try {
					command = applicationContext.getBean(commandName, Command.class);
				} catch (BeansException be) {
					log.debug(id + MSG_START + commandName + ", could not create command bean", be);
				}
				if (null != command) {
					response = command.getEmptyCommandResponse();
					response.setId(id);
					try {
						command.execute(request, response);
					} catch (Throwable throwable) { //NOPMD
						log.debug(id + MSG_START + commandName + ", error executing command", throwable);
						response.getErrors().add(throwable);
					}
				} else {
					response = new CommandResponse();
					response.setId(id);
					response.getErrors().add(new GeomajasException(ExceptionCode.COMMAND_NOT_FOUND, commandName));
				}

			} else {
				// not authorized
				response = new CommandResponse();
				response.setId(id);
				response.getErrors().add(
						new GeomajasSecurityException(ExceptionCode.COMMAND_ACCESS_DENIED, commandName, securityContext
								.getUserId()));
			}

			// Now process the errors for display on the client:
			List<Throwable> errors = response.getErrors();
			if (null != errors && !errors.isEmpty()) {
				Locale localeObject = null;
				if (null != locale) {
					localeObject = new Locale(locale);
				}
				for (Throwable t : errors) {
					String msg = getErrorMessage(t, localeObject);
					if (log.isDebugEnabled()) {
						log.debug(id + MSG_START + commandName + ", " + msg, t);
					}
					// For each exception, make sure the entire exception is sent to the client:
					response.getErrorMessages().add(msg);
					response.getExceptions().add(toDto(t, localeObject, msg));
				}
			}

			long executionTime = System.currentTimeMillis() - begin;
			response.setExecutionTime(executionTime);
			if (log.isDebugEnabled()) {
				log.debug(id + MSG_START + commandName + " done in " + executionTime + "ms");
			}
			if (log.isTraceEnabled()) {
				log.trace(id + MSG_START + commandName + " response " + response);
			}
			return response;
		} finally {
			if (!tokenIdentical) {
				// clear security context
				securityManager.clearSecurityContext();
			}
		}
	}

	@Override
	public long getCommandCount() {
		return commandCount;
	}

	private ExceptionDto toDto(final Throwable throwable, final Locale locale) {
		return toDto(throwable, locale, null);
	}

	private ExceptionDto toDto(final Throwable throwable, final Locale locale, final String msg) {
		if (null == throwable) {
			return null;
		}
		String message = msg;
		if (null == message) {
			message = getErrorMessage(throwable, locale);
		}
		ExceptionDto dto = new ExceptionDto(throwable.getClass().getName(), message, throwable.getStackTrace());
		if (throwable instanceof GeomajasException) {
			dto.setExceptionCode(((GeomajasException) throwable).getExceptionCode());
		}
		dto.setCause(toDto(throwable.getCause(), locale));
		return dto;
	}

	/**
	 * Get localized error message for a {@link Throwable}.
	 *
	 * @param throwable throwable to get message for
	 * @param locale locale to use for message
	 * @return exception message
	 */
	private String getErrorMessage(final Throwable throwable, final Locale locale) {
		String message;
		if (!(throwable instanceof GeomajasException)) {
			message = throwable.getMessage();
		} else {
			message = ((GeomajasException) throwable).getMessage(locale);
		}
		if (null == message || 0 == message.length()) {
			message = throwable.getClass().getName();
		}
		return message;
	}
}