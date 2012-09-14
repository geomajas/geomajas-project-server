/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.manager;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetVectorCapabilitiesResponse;
import org.geomajas.plugin.deskmanager.service.manager.DiscoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Component(GetVectorCapabilitiesRequest.COMMAND)
public class GetVectorCapabilitiesCommand implements
		Command<GetVectorCapabilitiesRequest, GetVectorCapabilitiesResponse> {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(GetVectorCapabilitiesCommand.class);

	@Autowired
	private DiscoveryService discoServ;

	public void execute(GetVectorCapabilitiesRequest request, GetVectorCapabilitiesResponse response) throws Exception {
		response.setVectorCapabilities(discoServ.getVectorCapabilities(request.getConnectionProperties()));
	}

	public GetVectorCapabilitiesResponse getEmptyCommandResponse() {
		return new GetVectorCapabilitiesResponse();
	}
}
