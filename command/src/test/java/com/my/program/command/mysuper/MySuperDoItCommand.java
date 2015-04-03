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

// @extract-start ExampleCommand, Example command template
package com.my.program.command.mysuper;

import com.my.program.command.dto.MySuperDoItRequest;
import com.my.program.command.dto.MySuperDoItResponse;
import org.geomajas.annotation.Api;
import org.geomajas.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Simple example command.
 *
 * @author Joachim Van der Auwera
 */
@Api
@Component()
public class MySuperDoItCommand implements Command<MySuperDoItRequest, MySuperDoItResponse> {

	private final Logger log = LoggerFactory.getLogger(MySuperDoItCommand.class);

	public MySuperDoItResponse getEmptyCommandResponse() {
		return new MySuperDoItResponse();
	}

	public void execute(MySuperDoItRequest request, MySuperDoItResponse response) throws Exception {
		log.debug("called");
		// ..... perform the actual command
	}

}
// @extract-end
