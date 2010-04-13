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

package com.my.program.command.mysuper;

import com.my.program.command.dto.MySuperDoItRequest;
import com.my.program.command.dto.MySuperDoItResponse;
import org.geomajas.command.Command;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Simple example command.
 *
 * @author Joachim Van der Auwera
 */
// @extract-start ExampleCommand, Example command template
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
