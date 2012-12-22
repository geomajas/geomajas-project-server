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
import org.geomajas.plugin.deskmanager.command.manager.dto.DeleteGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that deletes a geodesk from the database.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(DeleteGeodeskRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteGeodeskCommand implements Command<DeleteGeodeskRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteGeodeskCommand.class);

	@Autowired
	private GeodeskService loketService;

	/** {@inheritDoc} */
	public void execute(DeleteGeodeskRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getUuid() == null) {
				response.getErrorMessages().add("Geen id opgegeven?");
			} else {

				Geodesk bp = loketService.getGeodeskById(request.getUuid());
				if (bp == null) {
					response.getErrorMessages().add("Geen loket gevonden met id: " + request.getUuid());
				} else {
					loketService.deleteGeodesk(bp);
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij verwijderen loket: " + e.getMessage());
			log.error("fout bij verwijderen loket.", e);
		}
	}

	/** {@inheritDoc} */
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
