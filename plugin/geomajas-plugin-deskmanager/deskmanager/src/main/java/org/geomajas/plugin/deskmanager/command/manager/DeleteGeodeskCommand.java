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
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(DeleteGeodeskRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class DeleteGeodeskCommand implements Command<DeleteGeodeskRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteGeodeskCommand.class);

	@Autowired
	private GeodeskService loketService;

	@Override
	public void execute(DeleteGeodeskRequest request, CommandResponse response) throws Exception {
		try {
			if (request.getGeodeskId() == null) {
				Exception e = new IllegalArgumentException("No geodesk id given.");
				log.error(e.getLocalizedMessage());
				throw e;
			} else {

				Geodesk bp = loketService.getGeodeskById(request.getGeodeskId());
				if (bp == null) {
					Exception e = new IllegalArgumentException("No geodesk found with id : " + request.getGeodeskId());
					log.error(e.getLocalizedMessage());
					throw e;
				} else {
					loketService.deleteGeodesk(bp);
				}
			}
		} catch (Exception orig) {
			Exception e = new Exception("Unexpected error removing geodesk.", orig);
			log.error(e.getLocalizedMessage(), orig);
			throw e;
		}
	}

	@Override
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}
}
