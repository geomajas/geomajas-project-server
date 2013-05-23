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
import org.geomajas.plugin.deskmanager.command.manager.dto.GeodeskResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveBlueprintRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveGeodeskRequest;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
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
 * Command that saves a geodesk from a given dto.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(SaveGeodeskRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class SaveGeodeskCommand implements Command<SaveGeodeskRequest, GeodeskResponse> {

	private final Logger log = LoggerFactory.getLogger(SaveGeodeskCommand.class);

	@Autowired
	private DtoConverterService dtoService;

	@Autowired
	private GeodeskService loketService;

	@Autowired
	private TerritoryService groupService;

	@Autowired
	private SecurityContext securityContext;

	@Override
	public void execute(SaveGeodeskRequest request, GeodeskResponse response) throws Exception {
		try {
			if (request.getLoket() == null) {
				response.getErrorMessages().add("Geen loket opgegeven ??");
			} else if (request.getSaveBitmask() < 1) {
				response.getErrorMessages().add("Niets te bewaren ??");
			} else {
				Geodesk target = loketService.getGeodeskById(request.getLoket().getId());
				if (target == null) {
					response.getErrorMessages().add(
							"Geen loket gevonden met id: " + request.getLoket().getId() + " (Nieuw loket?)");
				} else {
					Geodesk source = dtoService.fromDto(request.getLoket());

					if ((SaveGeodeskRequest.SAVE_SETTINGS & request.getSaveBitmask()) > 0) {
						copySettings(source, target);
					}
					if ((SaveGeodeskRequest.SAVE_TERRITORIES & request.getSaveBitmask()) > 0) {
						copyGroups(source, target);
					}
					if ((SaveGeodeskRequest.SAVE_CLIENTWIDGETINFO & request.getSaveBitmask()) > 0) {
						copyWidgetInfo(source, target);
					}
					if ((SaveBlueprintRequest.SAVE_LAYERS & request.getSaveBitmask()) > 0) {
						copyLayers(source, target);
					}

					loketService.saveOrUpdateGeodesk(target);
					response.setGeodesk(dtoService.toDto(target, false));
				}
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij opslaan loket: " + e.getMessage());
			log.error("fout bij opslaan loket.", e);
		}
	}

	@Override
	public GeodeskResponse getEmptyCommandResponse() {
		return new GeodeskResponse();
	}

	// ----------------------------------------------------------

	/**
	 * Helper method to copy all geodesk settings.
	 */
	private void copySettings(Geodesk source, Geodesk target) throws Exception {
		target.setActive(source.isActive());
		target.setLimitToCreatorTerritory(source.isLimitToCreatorTerritory());
		target.setLimitToUserTerritory(source.isLimitToUserTerritory());
		target.setName(source.getName());
		target.setPublic(source.isPublic());
		if (Role.ADMINISTRATOR.equals(((DeskmanagerSecurityContext) securityContext).getRole())) {
			target.setGeodeskId(source.getGeodeskId());
		}
	}

	/**
	 * Helper method to copy widget info.
	 */
	private void copyWidgetInfo(Geodesk source, Geodesk target) throws Exception {
		target.getApplicationClientWidgetInfos().clear();
		target.getApplicationClientWidgetInfos().putAll(source.getApplicationClientWidgetInfos());
		
		target.getMainMapClientWidgetInfos().clear();
		target.getMainMapClientWidgetInfos().putAll(source.getMainMapClientWidgetInfos());

		target.getOverviewMapClientWidgetInfos().clear();
		target.getOverviewMapClientWidgetInfos().putAll(source.getOverviewMapClientWidgetInfos());
		
	}

	/**
	 * Helper method to copy groups.
	 */
	private void copyGroups(Geodesk source, Geodesk target) throws Exception {
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

	/**
	 * Helper method to copy all layers.
	 */
	private void copyLayers(Geodesk source, Geodesk target) throws Exception {
		target.getMainMapLayers().clear();
		target.getMainMapLayers().addAll(source.getMainMapLayers());

		target.getOverviewMapLayers().clear();
		target.getOverviewMapLayers().addAll(source.getOverviewMapLayers());
	}
	
}
