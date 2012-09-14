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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGroupsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGroupsResponse;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Component(GetGroupsRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class GetGroupsCommand implements Command<GetGroupsRequest, GetGroupsResponse> {

	@Autowired
	private GroupService groupService;

	@Autowired
	private DtoConverterService converterService;

	public void execute(GetGroupsRequest request, GetGroupsResponse response) throws Exception {
		List<TerritoryDto> groups = new ArrayList<TerritoryDto>();
		response.setGroups(groups);
		List<Territory> gs = groupService.getGroups();

		for (Territory g : gs) {
			groups.add(converterService.toDto(g, false, false));
		}
	}

	public GetGroupsResponse getEmptyCommandResponse() {
		return new GetGroupsResponse();
	}

}
