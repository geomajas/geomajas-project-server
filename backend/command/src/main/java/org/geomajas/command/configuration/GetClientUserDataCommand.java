/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.configuration;

import org.geomajas.command.Command;
import org.geomajas.command.dto.GetClientUserDataRequest;
import org.geomajas.command.dto.GetClientUserDataResponse;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This command requests a specific configuration object to be returned to the client. The object must implement the
 * {@link org.geomajas.configuration.client.ClientUserDataInfo} interface though.
 * 
 * @author Pieter De Graef
 */
@Component
public class GetClientUserDataCommand implements Command<GetClientUserDataRequest, GetClientUserDataResponse> {

	@Autowired
	private ApplicationContext context;

	public GetClientUserDataResponse getEmptyCommandResponse() {
		return new GetClientUserDataResponse();
	}

	public void execute(GetClientUserDataRequest request, GetClientUserDataResponse response) throws Exception {
		if (request != null && request.getClassName() != null && request.getIdentifier() != null) {
			Class<?> clazz = Class.forName(request.getClassName());
			Object bean = context.getBean(request.getIdentifier(), clazz);
			if (bean != null) {
				ClientUserDataInfo information = (ClientUserDataInfo) bean;
				response.setInformation(information);
			}
		}
	}
}