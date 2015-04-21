/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.general;

import org.geomajas.command.CommandHasRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.LogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Command which does server side logging.
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
@Component()
public class LogCommand implements CommandHasRequest<LogRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(LogCommand.class);

	@Override
	public LogRequest getEmptyCommandRequest() {
		return new LogRequest();
	}

	@Override
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}

	@Override
	public void execute(LogRequest request, CommandResponse response) throws Exception {
		switch (request.getLevel()) {
			case LogRequest.LEVEL_DEBUG:
				log.debug(request.getStatement());
				break;
			case LogRequest.LEVEL_INFO:
				log.info(request.getStatement());
				break;
			case LogRequest.LEVEL_WARN:
				log.warn(request.getStatement());
				break;
			case LogRequest.LEVEL_ERROR:
				log.error(request.getStatement());
				break;
			default:
				throw new IllegalArgumentException("Unknown log level " + request.getLevel());
		}
	}
}