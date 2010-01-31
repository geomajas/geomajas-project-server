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
package org.geomajas.extension.command.configuration;

import org.geomajas.command.Command;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.configuration.ClientApplicationInfo;
import org.geomajas.configuration.MapInfo;
import org.geomajas.extension.command.dto.EmptyCommandRequest;
import org.geomajas.extension.command.dto.GetConfigurationResponse;
import org.geomajas.layer.LayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This command fetches, and returns the initial application configuration for the user.
 * This is typically the first command to be executed.
 *
 * @author Pieter De Graef
 */
@Component()
public class GetConfigurationCommand implements Command<EmptyCommandRequest, GetConfigurationResponse> {

	@Autowired
	private ApplicationInfo application;

	@Autowired
	private GetMapConfigurationCommand mapConfigurationCommand;

	public GetConfigurationResponse getEmptyCommandResponse() {
		return new GetConfigurationResponse();
	}

	public void execute(EmptyCommandRequest request, GetConfigurationResponse response) throws Exception {
		// @todo security, data should be filtered
		ClientApplicationInfo cai = new ClientApplicationInfo();
		cai.setId(application.getId());
		cai.setName(application.getName());
		cai.setMaps(getMaps());
		cai.setScreenDpi(application.getScreenDpi());
		response.setApplication(cai);
	}

	private List<MapInfo> getMaps() throws LayerException {
		List<MapInfo> maps = new ArrayList<MapInfo>();
		for (MapInfo map : application.getMaps()) {
			maps.add(mapConfigurationCommand.getClientMapInfo(map));
		}
		return maps;
	}
}
