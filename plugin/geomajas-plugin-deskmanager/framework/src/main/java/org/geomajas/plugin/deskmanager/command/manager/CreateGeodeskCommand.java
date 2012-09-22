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
import org.geomajas.plugin.deskmanager.command.manager.dto.ReadApplicationResponse;
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
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Component(CreateGeodeskRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class CreateGeodeskCommand implements Command<CreateGeodeskRequest, ReadApplicationResponse> {

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

	public void execute(CreateGeodeskRequest request, ReadApplicationResponse response) throws Exception {
		try {
			if (request.getBlueprintId() == null || "".equals(request.getBlueprintId())) {
				response.getErrorMessages().add("Fout bij opslaan loket: BlueprintID is vereist.");
			} else {
				Blueprint bp = blueprintService.getBlueprintById(request.getBlueprintId());
				if (bp == null) {
					response.getErrorMessages().add(
							"Fout bij opslaan loket: Kon blueprint niet vinden? (" + request.getBlueprintId() + ")");
				}
				Geodesk l = new Geodesk();
				l.setName("[Nieuwe loket]");
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
				geodeskService.saveOrUpdateLoket(l);
				response.setLoket(dtoService.toDto(l, false));
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij opslaan loket: " + e.getMessage());
			log.error("fout bij opslaan loket.", e);
		}
	}

	public ReadApplicationResponse getEmptyCommandResponse() {
		return new ReadApplicationResponse();
	}
}
