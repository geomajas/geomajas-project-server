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
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This command fetches, and returns the initial application configuration for a specific MapWidget.
 * 
 * @author Pieter De Graef
 */
@Component()
public class GetMapConfigurationCommand implements Command<GetMapConfigurationRequest, GetMapConfigurationResponse> {

	@Autowired
	private ApplicationContext context;

	public GetMapConfigurationResponse getEmptyCommandResponse() {
		return new GetMapConfigurationResponse();
	}

	public void execute(GetMapConfigurationRequest request, GetMapConfigurationResponse response) throws Exception {
		// @todo security, data should be filtered
		ClientApplicationInfo client = context.getBean(request.getApplicationId(), ClientApplicationInfo.class);
		for (ClientMapInfo map : client.getMaps()) {
			if (request.getMapId().equals(map.getId())) {
				// clone it before modifying !
				ClientMapInfo clone = (ClientMapInfo) SerializationUtils.clone(map);
				// @todo security, data should be filtered
				// ((ClientVectorLayerInfo)map.getLayers().get(0)).setCreatable(true);
				response.setMapInfo(clone);
			}
		}
	}

}
