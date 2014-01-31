/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.common;

import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.service.common.GeodeskConfigurationService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * GetConfigurationCommand for the deskmanager. 
 * Fetches from the {@link org.geomajas.plugin.deskmanager.service.common.GeodeskConfigurationService}.
 * 
 * @author Oliver May
 *
 */
public class GetMapConfigurationCommand extends org.geomajas.command.configuration.GetMapConfigurationCommand {

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private GeodeskConfigurationService configurationService;

	public GetMapConfigurationResponse getEmptyCommandResponse() {
		return new GetMapConfigurationResponse();
	}

	public void execute(GetMapConfigurationRequest request, GetMapConfigurationResponse response) throws Exception {
		if (null == request.getApplicationId()) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "applicationId");
		}
		String mapId = request.getMapId();
		if (null == mapId) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapId");
		}

		// this checks if geodesk is allowed
		Geodesk loket = geodeskService.getGeodeskByPublicId(request.getApplicationId()); 

		if (loket != null) {
			ClientApplicationInfo loketConfig = configurationService.createClonedGeodeskConfiguration(loket, true);
			for (ClientMapInfo mapInfo : loketConfig.getMaps()) {
				if (request.getMapId().equals(mapInfo.getId())) {
					response.setMapInfo(mapInfo);
				}
			}
		} else {
			throw new GeomajasException(ExceptionCode.APPLICATION_NOT_FOUND, request.getApplicationId());
		}
		
	}

}
