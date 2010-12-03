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

package org.geomajas.example.gwt.server.samples;

import org.geomajas.command.Command;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.example.gwt.client.samples.base.GetResourcesResponse;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Command that generates an exception with stack trace.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class GetExceptionCommand implements Command<EmptyCommandRequest, CommandResponse> {

	public void execute(EmptyCommandRequest request, CommandResponse response) throws Exception {
		throw new IllegalArgumentException("Oops. I guess the server encountered some 'unexpected' exception.");
	}

	public CommandResponse getEmptyCommandResponse() {
		return new GetResourcesResponse();
	}
}
