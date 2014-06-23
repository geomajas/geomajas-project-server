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
import org.geomajas.plugin.deskmanager.DeskmanagerException;
import org.geomajas.plugin.deskmanager.command.security.dto.GetGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.GetGroupResponse;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for getting a group.
 * 
 * @author Jan De Moerloose
 * 
 */
@Transactional
@Component(GetGroupRequest.COMMAND)
public class GetGroupCommand implements Command<GetGroupRequest, GetGroupResponse> {

	private final Logger log = LoggerFactory.getLogger(GetGroupCommand.class);

	@Autowired
	private GroupService groupService;

	@Autowired
	private DtoConverterService converterService;

	@Override
	public GetGroupResponse getEmptyCommandResponse() {
		return new GetGroupResponse();
	}

	@Override
	public void execute(GetGroupRequest request, GetGroupResponse response) throws Exception {
		try {
			Territory group = groupService.findById(request.getId());
			if (group != null) {
				response.setGroup(converterService.toDto(group, false, false, true));
			} else {
				throw new DeskmanagerException(DeskmanagerException.GROUP_NOT_FOUND, request.getId());
			}
		} catch (Exception e) {
			log.error("Unexpected error", e);
			throw e;
		}
	}

}
