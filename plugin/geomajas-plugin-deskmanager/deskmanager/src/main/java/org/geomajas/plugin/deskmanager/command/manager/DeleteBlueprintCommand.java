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
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to delete a blueprint from the database from a given ID.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 *
 */
@Component(DeleteBlueprintRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteBlueprintCommand implements Command<DeleteBlueprintRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteBlueprintCommand.class);

	@Autowired
	private BlueprintService blueprintService;

	/** {@inheritDoc} */
	public void execute(DeleteBlueprintRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getBlueprintId() == null) {
				//TODO: i18n
				response.getErrorMessages().add("No blueprint id was given!");
			} else {

				Blueprint bp = blueprintService.getBlueprintById(request.getBlueprintId());
				if (bp == null) {
					//TODO: i18n
					response.getErrorMessages().add("No blueprint was found with id: " + request.getBlueprintId());
				} else {
					blueprintService.deleteBlueprint(bp);
				}
			}
		} catch (Exception e) {
			//TODO: i18n
			response.getErrorMessages().add("Unexpected error while deleting blueprint: " + e.getMessage());
			log.error("Unexpected error while deleting blueprint.", e);
		}
	}

	/** {@inheritDoc} */
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
