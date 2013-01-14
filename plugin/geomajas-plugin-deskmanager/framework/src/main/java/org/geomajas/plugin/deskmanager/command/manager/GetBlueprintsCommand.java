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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetBlueprintsResponse;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that fetches a list of blueprints from the database where the current user has access to.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 *
 */
@Component(GetBlueprintsRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetBlueprintsCommand implements Command<GetBlueprintsRequest, GetBlueprintsResponse> {

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private DtoConverterService converterService;

	/** {@inheritDoc} */
	public void execute(GetBlueprintsRequest request, GetBlueprintsResponse response) throws Exception {

		List<BlueprintDto> blueprints = new ArrayList<BlueprintDto>();

		// no need to filter by group, this is done by security
		for (Blueprint bp : blueprintService.getBlueprints()) {
			blueprints.add(converterService.toDto(bp, false));
		}

		response.setBlueprints(blueprints);
	}

	/** {@inheritDoc} */
	public GetBlueprintsResponse getEmptyCommandResponse() {
		return new GetBlueprintsResponse();
	}

}
