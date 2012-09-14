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
import org.geomajas.plugin.deskmanager.command.manager.dto.ReadApplicationRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.ReadApplicationResponse;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that reads the application dto for display and manipulation in the management module.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 * 
 */
@Component(ReadApplicationRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class ReadApplicationCommand implements Command<ReadApplicationRequest, ReadApplicationResponse> {

	private final Logger log = LoggerFactory.getLogger(ReadApplicationCommand.class);

	@Autowired
	private GeodeskService loketService;

	@Autowired
	private DtoConverterService dtoService;

	public void execute(ReadApplicationRequest request, ReadApplicationResponse response) throws Exception {
		try {
			response.setLoket(dtoService.toDto(loketService.getLoketById(request.getUuid()), true));
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij ophalen loket: " + e.getMessage());
			log.error("fout bij ophalen loket.", e);
		}
	}

	public ReadApplicationResponse getEmptyCommandResponse() {
		return new ReadApplicationResponse();
	}
}
