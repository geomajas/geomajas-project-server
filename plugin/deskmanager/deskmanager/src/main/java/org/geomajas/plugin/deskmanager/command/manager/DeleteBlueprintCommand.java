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

	@Override
	public void execute(DeleteBlueprintRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getBlueprintId() == null) {
				Exception e = new IllegalArgumentException("No blueprint id given.");
				log.error(e.getLocalizedMessage());
				throw e;
			} else {

				Blueprint bp = blueprintService.getBlueprintById(request.getBlueprintId());
				if (bp == null) {
					Exception e = new IllegalArgumentException("No blueprint found with the given id: "
							+ request.getBlueprintId());
					log.error(e.getLocalizedMessage());
					throw e;
				} else {
					blueprintService.deleteBlueprint(bp);
				}
			}
		} catch (Exception orig) {
			Exception e = new Exception("Unexpected error removing blueprint.", orig);
			log.error(e.getLocalizedMessage(), orig);
			throw e;
		}
	}

	@Override
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
