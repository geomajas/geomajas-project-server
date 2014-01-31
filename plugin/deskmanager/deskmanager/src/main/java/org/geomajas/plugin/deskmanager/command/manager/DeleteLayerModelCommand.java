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
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(DeleteLayerModelRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteLayerModelCommand implements Command<DeleteLayerModelRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteLayerModelCommand.class);

	@Autowired
	private LayerModelService layerModelService;

	@Override
	public void execute(DeleteLayerModelRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getId() == null) {
				Exception e = new IllegalArgumentException("No layermodel id given.");
				log.error(e.getLocalizedMessage());
				throw e;
			} else {
				LayerModel bp = layerModelService.getLayerModelById(request.getId());
				if (bp == null) {
					Exception e = new IllegalArgumentException("No datalayer found with the given id: "
							+ request.getId());
					log.error(e.getLocalizedMessage());
					throw e;
				} else {
					layerModelService.deleteLayerModel(bp);
				}
			}
		} catch (Exception orig) {
			Exception e = new Exception("Unexpected error removing layer.", orig);
			log.error(e.getLocalizedMessage(), orig);
			throw e;
		}
	}

	@Override
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
