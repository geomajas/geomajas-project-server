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
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseResponse;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.security.GeomajasSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Component(CheckLayerModelInUseRequest.COMMAND)
@Transactional(readOnly = true)
public class CheckLayerModelInUseCommand implements Command<CheckLayerModelInUseRequest, CheckLayerModelInUseResponse> {

	@Autowired
	private LayerModelService service;

	public void execute(CheckLayerModelInUseRequest request, CheckLayerModelInUseResponse response)
			throws GeomajasSecurityException {
		if (request.getClientLayerId() == null || "".equals(request.getClientLayerId())) {
			response.getErrorMessages().add("Gelieve een clientlayerId op te geven.");
		} else {
			response.setLayerModelInUse(service.isLayerModelInUse(request.getClientLayerId()));
		}
	}

	public CheckLayerModelInUseResponse getEmptyCommandResponse() {
		return new CheckLayerModelInUseResponse();
	}

}
