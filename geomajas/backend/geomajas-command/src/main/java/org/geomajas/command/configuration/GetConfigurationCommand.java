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
package org.geomajas.command.configuration;

import org.apache.commons.lang.SerializationUtils;
import org.geomajas.command.Command;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This command fetches, and returns the initial application configuration for the user. This is typically the first
 * command to be executed.
 * 
 * @author Pieter De Graef
 */
@Component()
public class GetConfigurationCommand implements Command<GetConfigurationRequest, GetConfigurationResponse> {

	@Autowired
	private ApplicationContext context;

	public GetConfigurationResponse getEmptyCommandResponse() {
		return new GetConfigurationResponse();
	}

	public void execute(GetConfigurationRequest request, GetConfigurationResponse response) throws Exception {
		ClientApplicationInfo client = context.getBean(request.getApplicationId(), ClientApplicationInfo.class);
		// clone it before modifying
		ClientApplicationInfo clone = (ClientApplicationInfo) SerializationUtils.clone(client);
		// @todo security, data should be filtered
		// ((ClientVectorLayerInfo)clone.getMaps().get(0).getLayers().get(0)).setCreatable(true);
		response.setApplication(clone);
	}

}
