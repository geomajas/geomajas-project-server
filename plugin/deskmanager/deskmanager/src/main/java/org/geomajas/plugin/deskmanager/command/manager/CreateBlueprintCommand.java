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
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that creates a new empty blueprint in the database, based on a user application.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(CreateBlueprintRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class CreateBlueprintCommand implements Command<CreateBlueprintRequest, BlueprintResponse> {

	private final Logger log = LoggerFactory.getLogger(CreateBlueprintCommand.class);

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private DtoConverterService dtoService;

	@Override
	public void execute(CreateBlueprintRequest request, BlueprintResponse response) throws Exception {
		try {
			Blueprint bp = new Blueprint();
			bp.setName(request.getName());
			bp.setUserApplicationKey(request.getUserApplicationKey());
			bp.setPublic(request.isPublic());
			blueprintService.saveOrUpdateBlueprint(bp);
			response.setBlueprint(dtoService.toDto(bp, false));
		} catch (Exception orig) {
			Exception e = new Exception("Unexpected error creating blueprint.", orig);
			log.error(e.getLocalizedMessage(), orig);
			throw e;
		}
	}

	@Override
	public BlueprintResponse getEmptyCommandResponse() {
		return new BlueprintResponse();
	}
}
