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
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
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
@Component(DeleteBlueprintRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteBlueprintCommand implements Command<DeleteBlueprintRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteBlueprintCommand.class);

	@Autowired
	private BlueprintService blueprintService;

	public void execute(DeleteBlueprintRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getUuid() == null) {
				response.getErrorMessages().add("Geen id opgegeven?");
			} else {

				Blueprint bp = blueprintService.getBlueprintById(request.getUuid());
				if (bp == null) {
					response.getErrorMessages().add("Geen blauwdruk gevonden met id: " + request.getUuid());
				} else {
					blueprintService.deleteBlueprint(bp);
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij verwijderen blauwdruk: " + e.getMessage());
			log.error("fout bij verwijderen blauwdruk.", e);
		}
	}

	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
