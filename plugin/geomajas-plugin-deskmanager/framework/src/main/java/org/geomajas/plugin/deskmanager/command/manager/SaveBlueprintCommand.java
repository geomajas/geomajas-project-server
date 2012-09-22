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
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Oliver May
 * 
 */
@Component(SaveBlueprintRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class SaveBlueprintCommand implements Command<SaveBlueprintRequest, BlueprintResponse> {

	private final Logger log = LoggerFactory.getLogger(SaveBlueprintCommand.class);

	@Autowired
	private DtoConverterService dtoService;

	@Autowired
	private BlueprintService blueprintService;

	@Autowired
	private TerritoryService groupService;

	public void execute(SaveBlueprintRequest request, BlueprintResponse response) throws Exception {
		try {
			if (request.getBlueprint() == null) {
				response.getErrorMessages().add("Geen blauwdruk opgegeven ??");
			} else if (request.getSaveWhat() < 1) {
				response.getErrorMessages().add("Niets te bewaren ??");
			} else {
				Blueprint target = blueprintService.getBlueprintById(request.getBlueprint().getId());
				if (target == null) {
					response.getErrorMessages().add(
							"Geen blauwdruk gevonden voor id: " + request.getBlueprint().getId()
									+ " (Nieuwe blauwdruk?)");
				} else {
					Blueprint source = dtoService.fromDto(request.getBlueprint());

					if ((SaveBlueprintRequest.SAVE_SETTINGS & request.getSaveWhat()) > 0) {
						copySettings(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_GROUPS & request.getSaveWhat()) > 0) {
						copyGroups(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO & request.getSaveWhat()) > 0) {
						copyWidgetInfo(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_LAYERS & request.getSaveWhat()) > 0) {
						copyLayers(source, target);
					}

					blueprintService.saveOrUpdateBlueprint(target);
					response.setBlueprint(dtoService.toDto(target, false));
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij opslaan blauwdruk: " + e.getMessage());
			log.error("fout bij opslaan blauwdruk.", e);
		}
	}

	public BlueprintResponse getEmptyCommandResponse() {
		return new BlueprintResponse();
	}

	// ----------------------------------------------------------

	private void copySettings(Blueprint source, Blueprint target) throws Exception {
		target.setActive(source.isActive());
		target.setUserApplicationKey(source.getUserApplicationKey());
		target.setLimitToCreatorTerritory(source.isLimitToCreatorTerritory());
		target.setLimitToUserTerritory(source.isLimitToUserTerritory());
		target.setGeodesksActive(source.isGeodesksActive());
		target.setName(source.getName());
		target.setPublic(source.isPublic());
	}

	private void copyWidgetInfo(Blueprint source, Blueprint target) throws Exception {
		target.getApplicationClientWidgetInfos().putAll(source.getApplicationClientWidgetInfos());
		target.getMainMapClientWidgetInfos().putAll(source.getMainMapClientWidgetInfos());
		target.getOverviewMapClientWidgetInfos().putAll(source.getOverviewMapClientWidgetInfos());

	}

	private void copyGroups(Blueprint source, Blueprint target) throws Exception {
		List<Territory> toDelete = new ArrayList<Territory>();
		List<Territory> toAdd = new ArrayList<Territory>();
		for (Territory g : target.getTerritories()) {
			if (!source.getTerritories().contains(g)) {
				toDelete.add(g);
			}
		}
		if (toDelete.size() > 0) {
			target.getTerritories().removeAll(toDelete);
		}
		for (Territory g : source.getTerritories()) {
			if (!target.getTerritories().contains(g)) {
				toAdd.add(g);
			}
		}
		for (Territory discon : toAdd) {
			Territory conn = groupService.getById(discon.getId());
			if (conn != null) {
				target.getTerritories().add(conn);
			} else {
				log.warn("Territory not found !? (id: " + discon.getId());
			}
		}
	}

	private void copyLayers(Blueprint source, Blueprint target) throws Exception {
		target.getMainMapLayers().clear();
		target.getMainMapLayers().addAll(source.getMainMapLayers());

		target.getOverviewMapLayers().clear();
		target.getOverviewMapLayers().addAll(source.getOverviewMapLayers());
	}
}
