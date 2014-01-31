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
package org.geomajas.plugin.deskmanager.command.manager;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetRasterLayerConfigRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetRasterLayerConfigResponse;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristof Heirwegh
 */
@Component(GetRasterLayerConfigRequest.COMMAND)
public class GetRasterLayerConfigCommand implements
		Command<GetRasterLayerConfigRequest, GetRasterLayerConfigResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(GetRasterLayerConfigCommand.class);

	@Autowired
	private DiscoveryService discoServ;

	public void execute(GetRasterLayerConfigRequest request, GetRasterLayerConfigResponse response)
			throws Exception {
		response.setRasterLayerConfiguration(discoServ.getRasterLayerConfiguration(request.getConnectionProperties(),
				request.getRasterCapabilitiesInfo()));
	}

	public GetRasterLayerConfigResponse getEmptyCommandResponse() {
		return new GetRasterLayerConfigResponse();
	}
}
