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

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckLayerModelInUseResponse;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that checks if a layer model is in use. Typically this command is called to check if a (dynamic) layer that 
 * needs to be removed actually can be removed.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 */
@Component(CheckLayerModelInUseRequest.COMMAND)
@Transactional(readOnly = true)
public class CheckLayerModelInUseCommand implements Command<CheckLayerModelInUseRequest, CheckLayerModelInUseResponse> {

	private final Logger log = LoggerFactory.getLogger(CheckLayerModelInUseCommand.class);

	@Autowired
	private LayerModelService service;

	/** {@inheritDoc} */
	public void execute(CheckLayerModelInUseRequest request, CheckLayerModelInUseResponse response)
			throws Exception {
		if (request.getLayerModelId() == null || "".equals(request.getLayerModelId())) {
			Exception e = new IllegalArgumentException("No layermodel id given.");
			log.error(e.getLocalizedMessage());
			throw e;
		} else {
			response.setLayerModelInUse(service.isLayerModelInUse(request.getLayerModelId()));
		}
	}

	/** {@inheritDoc} */
	public CheckLayerModelInUseResponse getEmptyCommandResponse() {
		return new CheckLayerModelInUseResponse();
	}

}
