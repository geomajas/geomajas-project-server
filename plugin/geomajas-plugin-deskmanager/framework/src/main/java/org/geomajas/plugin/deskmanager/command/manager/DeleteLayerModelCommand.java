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
 * Command to delete a layer model given a layer id.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(DeleteLayerModelRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteLayerModelCommand implements Command<DeleteLayerModelRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteLayerModelCommand.class);

	@Autowired
	private LayerModelService layerModelService;

	/** {@inheritDoc} */
	public void execute(DeleteLayerModelRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getId() == null) {
				//TODO: i18n
				response.getErrorMessages().add("No id given?");
			} else {
				LayerModel bp = layerModelService.getLayerModelById(request.getId());
				if (bp == null) {
					//TODO: i18n
					response.getErrorMessages().add("No datalayer found with the given id: " + request.getId());
				} else {
					layerModelService.deleteLayerModel(bp);
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Unexpected error removing layer: " + e.getMessage());
			log.error("Unexpected error removing layer.", e);
		}
	}

	/** {@inheritDoc} */
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
