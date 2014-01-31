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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeotoolsVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeotoolsVectorCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Retrieve capabilities from a vector data source such as WFS or database. Returns a list of vector layers in this
 * source. This command works only for Geotools datasources.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 */
@Component(GetGeotoolsVectorCapabilitiesRequest.COMMAND)
public class GetGeotoolsVectorCapabilitiesCommand implements
		Command<GetGeotoolsVectorCapabilitiesRequest, GetGeotoolsVectorCapabilitiesResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(GetGeotoolsVectorCapabilitiesCommand.class);

	@Autowired
	private DiscoveryService discoServ;

	public void execute(GetGeotoolsVectorCapabilitiesRequest request, GetGeotoolsVectorCapabilitiesResponse response)
			throws Exception {
		response.setVectorCapabilities(discoServ.getVectorCapabilities(request.getConnectionProperties()));
	}

	public GetGeotoolsVectorCapabilitiesResponse getEmptyCommandResponse() {
		return new GetGeotoolsVectorCapabilitiesResponse();
	}
}
