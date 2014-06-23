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
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteGroupRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.DeleteGroupResponse;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for deleting a user.
 * 
 * @author Jan Venstermans
 * 
 */
@Transactional
@Component(DeleteGroupRequest.COMMAND)
public class DeleteGroupCommand implements Command<DeleteGroupRequest, DeleteGroupResponse> {

	private final Logger log = LoggerFactory.getLogger(DeleteGroupCommand.class);

	@Autowired
	private GroupService groupService;

	@Override
	public DeleteGroupResponse getEmptyCommandResponse() {
		return new DeleteGroupResponse();
	}

	@Override
	public void execute(DeleteGroupRequest request, DeleteGroupResponse response) throws Exception {
		Territory territory = groupService.findById(request.getId());
		if (territory != null) {
			groupService.deleteGroup(request.getId());
			log.info("Deleted group " + territory.getName());
		} else {
			log.info("Group with id " + request.getId() + " does not exist");
		}
	}

}
