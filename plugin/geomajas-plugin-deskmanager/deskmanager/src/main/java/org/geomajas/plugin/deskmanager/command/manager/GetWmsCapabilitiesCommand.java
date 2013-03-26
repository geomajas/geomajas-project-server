/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager;

import javax.annotation.Resource;

import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Retrieve capabilities from a WMS data source. Returns a list of raster layers in this
 * source. 
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 *
 */
@Component(GetWmsCapabilitiesRequest.COMMAND)
public class GetWmsCapabilitiesCommand implements
		Command<GetWmsCapabilitiesRequest, GetWmsCapabilitiesResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(GetWmsCapabilitiesCommand.class);

	@Autowired
	private DiscoveryService discoServ;
	
	@Resource(name = "dynamicLayersApplication")
	private ClientApplicationInfo defaultGeodesk;

	public void execute(GetWmsCapabilitiesRequest request, GetWmsCapabilitiesResponse response) throws Exception {
		response.setRasterCapabilities(discoServ.getRasterCapabilities(request.getConnectionProperties()));
		response.setDefaultCrs(defaultGeodesk.getMaps().get(0).getCrs());
	}

	public GetWmsCapabilitiesResponse getEmptyCommandResponse() {
		return new GetWmsCapabilitiesResponse();
	}
}
