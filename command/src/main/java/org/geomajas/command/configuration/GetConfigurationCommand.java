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
package org.geomajas.command.configuration;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.ServerSideOnlyInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
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

	@Autowired
	private GetMapConfigurationCommand mapConfigurationCommand;

	@Override
	public GetConfigurationResponse getEmptyCommandResponse() {
		return new GetConfigurationResponse();
	}

	@Override
	public void execute(GetConfigurationRequest request, GetConfigurationResponse response) throws Exception {
		if (null == request.getApplicationId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "applicationId");
		}

		// the data is explicitly copied as this assures the security is considered when copying.
		ClientApplicationInfo original = context.getBean(request.getApplicationId(), ClientApplicationInfo.class);
		if (original == null) {
			throw new GeomajasException(ExceptionCode.APPLICATION_NOT_FOUND, request.getApplicationId());
		}
		ClientApplicationInfo client = new ClientApplicationInfo();
		client.setId(original.getId());
		if (!(original.getUserData() instanceof ServerSideOnlyInfo)) {
			client.setUserData(original.getUserData());
		}
		client.setWidgetInfo(mapConfigurationCommand.securityClone(original.getWidgetInfo()));
		client.setScreenDpi(original.getScreenDpi());
		List<ClientMapInfo> maps = new ArrayList<ClientMapInfo>();
		client.setMaps(maps);
		for (ClientMapInfo map : original.getMaps()) {
			maps.add(mapConfigurationCommand.securityClone(map));
		}
		response.setApplication(client);
	}
}
