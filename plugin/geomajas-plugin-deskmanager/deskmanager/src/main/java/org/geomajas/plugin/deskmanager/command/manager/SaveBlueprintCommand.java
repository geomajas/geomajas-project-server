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
import org.geomajas.plugin.deskmanager.command.manager.dto.BlueprintResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.domain.BaseGeodesk;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.geomajas.security.GeomajasSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that saves a geodesk from a given dto.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
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

	/** {@inheritDoc} */
	public void execute(SaveBlueprintRequest request, BlueprintResponse response) throws Exception {
		try {
			if (request.getBlueprint() == null) {
				Exception e = new IllegalArgumentException("No blueprint id given.");
				log.error(e.getLocalizedMessage());
				throw e;
			} else {
				Blueprint target = blueprintService.getBlueprintById(request.getBlueprint().getId());
				if (target == null) {
					Exception e = new IllegalArgumentException("No blueprint found for the given id: "
							+ request.getBlueprint());
					log.error(e.getLocalizedMessage());
					throw e;
				} else {
					Blueprint source = dtoService.fromDto(request.getBlueprint());

					if ((SaveBlueprintRequest.SAVE_SETTINGS & request.getSaveBitmask()) > 0) {
						copySettings(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_TERRITORIES & request.getSaveBitmask()) > 0) {
						copyGroups(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_CLIENTWIDGETINFO & request.getSaveBitmask()) > 0) {
						copyWidgetInfo(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_LAYERS & request.getSaveBitmask()) > 0) {
						copyLayers(source, target);
					}

					blueprintService.saveOrUpdateBlueprint(target);
					response.setBlueprint(dtoService.toDto(target, false));
				}
			}
		} catch (GeomajasSecurityException e) {
			throw e;
		} catch (Exception orig) {
			Exception e = new Exception("Unexpected error saving blueprint.", orig);
			log.error(e.getLocalizedMessage());
			throw e;
		}
	}

	/** {@inheritDoc} */
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

	private void copyWidgetInfo(BaseGeodesk source, BaseGeodesk target) throws Exception {
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

	private void copyLayers(BaseGeodesk source, BaseGeodesk target) throws Exception {
		target.getMainMapLayers().clear();
		target.getMainMapLayers().addAll(source.getMainMapLayers());

		target.getOverviewMapLayers().clear();
		target.getOverviewMapLayers().addAll(source.getOverviewMapLayers());
	}
}
