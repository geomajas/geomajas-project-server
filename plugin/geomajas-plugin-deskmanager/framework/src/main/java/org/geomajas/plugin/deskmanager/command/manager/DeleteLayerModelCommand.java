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
import org.geomajas.command.CommandResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteLayerModelRequest;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Component(DeleteLayerModelRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteLayerModelCommand implements Command<DeleteLayerModelRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteLayerModelCommand.class);

	@Autowired
	private LayerModelService layerModelService;

	public void execute(DeleteLayerModelRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getId() == null) {
				response.getErrorMessages().add("Geen id opgegeven?");
			} else {
				LayerModel bp = layerModelService.getLayerModelById(request.getId());
				if (bp == null) {
					response.getErrorMessages().add("Geen datalaag gevonden met id: " + request.getId());
				} else {
					layerModelService.deleteLayerModel(bp);
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij verwijderen datalaag: " + e.getMessage());
			log.error("fout bij verwijderen datalaag.", e);
		}
	}

	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
