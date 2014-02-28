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
import org.geomajas.plugin.deskmanager.command.security.dto.CreateGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.CreateGroupResponse;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for creating a new group.
 * 
 * @author Jan Venstermans
 * 
 */
@Transactional
@Component(CreateGroupRequest.COMMAND)
public class CreateGroupCommand implements Command<CreateGroupRequest, CreateGroupResponse> {

	private final Logger log = LoggerFactory.getLogger(CreateGroupCommand.class);

	@Autowired
	private GroupService groupService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private org.geomajas.service.DtoConverterService baseConverterService;

	@Override
	public CreateGroupResponse getEmptyCommandResponse() {
		return new CreateGroupResponse();
	}

	@Override
	public void execute(CreateGroupRequest request, CreateGroupResponse response) throws Exception {
		Territory group = groupService.createGroup(request.getName(), request.getKey(), request.getCrs(),
				baseConverterService.toInternal(request.getGeometry()), null);
		log.info("Created group " + group);
		response.setGroup(converterService.toDto(group, false, false));
	}

}
