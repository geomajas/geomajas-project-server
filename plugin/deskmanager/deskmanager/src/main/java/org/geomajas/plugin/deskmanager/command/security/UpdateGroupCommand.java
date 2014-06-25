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
package org.geomajas.plugin.deskmanager.command.security;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.UpdateGroupResponse;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for updating a group.
 * 
 * @author Jan De Moerloose
 * 
 */
@Transactional
@Component(UpdateGroupRequest.COMMAND)
public class UpdateGroupCommand implements Command<UpdateGroupRequest, UpdateGroupResponse> {

	@Autowired
	private GroupService groupService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private org.geomajas.service.DtoConverterService geomajasConverterService;

	private final Logger log = LoggerFactory.getLogger(UpdateGroupCommand.class);

	@Override
	public UpdateGroupResponse getEmptyCommandResponse() {
		return new UpdateGroupResponse();
	}

	@Override
	public void execute(UpdateGroupRequest request, UpdateGroupResponse response) throws Exception {
		TerritoryDto group = request.getGroup();
		groupService.updateGroup(group.getId(), group.getName(), group.getCode());
		Territory result = groupService.updateGroupGeometry(group.getId(), group.getCrs(),
				geomajasConverterService.toInternal(group.getGeometry()));
		log.info("Updated group " + group.getName());
		response.setGroup(converterService.toDto(result, false, false, true));
	}
}
