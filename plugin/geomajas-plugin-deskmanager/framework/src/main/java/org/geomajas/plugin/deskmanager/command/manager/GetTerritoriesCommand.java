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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetTerritoriesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetTerritoriesResponse;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that fetches all territories from the database. Commonly used to display all territories in select lists in 
 * the management interface. It only contains the territory definition, not the mapped blueprints or geodesks.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(GetTerritoriesRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class GetTerritoriesCommand implements Command<GetTerritoriesRequest, GetTerritoriesResponse> {

	@Autowired
	private TerritoryService territoryService;

	@Autowired
	private DtoConverterService converterService;

	public void execute(GetTerritoriesRequest request, GetTerritoriesResponse response) throws Exception {
		List<TerritoryDto> groups = new ArrayList<TerritoryDto>();
		response.setTerritories(groups);
		List<Territory> gs = territoryService.getTerritories();

		for (Territory g : gs) {
			groups.add(converterService.toDto(g, false, false));
		}
	}

	public GetTerritoriesResponse getEmptyCommandResponse() {
		return new GetTerritoriesResponse();
	}

}
