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

import java.util.UUID;

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateGeodeskRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskResponse;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that creates a new empty geodesk in the database, based on a given blueprint.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(CreateGeodeskRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class CreateGeodeskCommand implements Command<CreateGeodeskRequest, GetGeodeskResponse> {

	private final Logger log = LoggerFactory.getLogger(CreateGeodeskCommand.class);

	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private TerritoryService groupService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private DtoConverterService dtoService;

	/** {@inheritDoc} */
	public void execute(CreateGeodeskRequest request, GetGeodeskResponse response) throws Exception {
		try {
			if (request.getBlueprintId() == null || "".equals(request.getBlueprintId())) {
				response.getErrorMessages().add("Error while saving geodesk: BlueprintID is required.");
			} else {
				Blueprint bp = blueprintService.getBlueprintById(request.getBlueprintId());
				if (bp == null) {
					response.getErrorMessages().add(
							"Error while saving geodesk: Could not find blueprint. (" + request.getBlueprintId() + ")");
				}
				Geodesk l = new Geodesk();
				l.setName(request.getName());
				l.setGeodeskId(UUID.randomUUID().toString());
				l.setBlueprint(bp);
				l.setPublic(bp.isPublic());
				l.setLimitToCreatorTerritory(bp.isLimitToCreatorTerritory());
				l.setLimitToUserTerritory(bp.isLimitToUserTerritory());
				Territory g = ((DeskmanagerSecurityContext) securityContext).getTerritory();
				if (g.getId() > 0) { // 0 = superuser
					l.setOwner(groupService.getById(g.getId()));

					// -- add self group if non-public loket
					if (!l.isPublic()) {
						Territory dbGrp = groupService.getById(g.getId());
						l.getTerritories().add(dbGrp);
					}
				}
				geodeskService.saveOrUpdateGeodesk(l);
				response.setGeodesk(dtoService.toDto(l, false));
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Unexpected error while saving geodesk: " + e.getMessage());
			log.error("Unexpected error while saving geodesk:", e);
		}
	}

	/** {@inheritDoc} */
	public GetGeodeskResponse getEmptyCommandResponse() {
		return new GetGeodeskResponse();
	}
}
