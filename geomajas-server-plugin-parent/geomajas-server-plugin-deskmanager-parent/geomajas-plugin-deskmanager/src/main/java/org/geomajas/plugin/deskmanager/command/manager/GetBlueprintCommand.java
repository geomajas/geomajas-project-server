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
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintRequest;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that fetches a blueprint from the database using a given uuid.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 *
 */
@Component(GetBlueprintRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetBlueprintCommand implements Command<GetBlueprintRequest, BlueprintResponse> {

	private final Logger log = LoggerFactory.getLogger(GetBlueprintCommand.class);

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private DtoConverterService dtoService;

	@Override
	public void execute(GetBlueprintRequest request, BlueprintResponse response) throws Exception {
		try {
			response.setBlueprint(dtoService.toDto(blueprintService.getBlueprintById(request.getBlueprintId()), true));
		} catch (Exception orig) {
			Exception e = new Exception("Unexpected error fetching blueprints.", orig);
			log.error(e.getLocalizedMessage(), orig);
			throw e;
		}
	}

	@Override
	public BlueprintResponse getEmptyCommandResponse() {
		return new BlueprintResponse();
	}
}
