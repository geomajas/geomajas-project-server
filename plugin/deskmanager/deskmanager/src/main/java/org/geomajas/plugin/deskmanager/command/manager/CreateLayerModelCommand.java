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
package org.geomajas.plugin.deskmanager.command.manager;

import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that creates a new layer model, based on the given configuration info.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(CreateLayerModelRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class CreateLayerModelCommand implements Command<CreateLayerModelRequest, LayerModelResponse> {

	private final Logger log = LoggerFactory.getLogger(CreateLayerModelCommand.class);

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private TerritoryService groupService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private DtoConverterService dtoService;

	@Override
	public void execute(CreateLayerModelRequest request, LayerModelResponse response) throws Exception {
		if (request.getConfiguration() == null || request.getConfiguration().getClientLayerInfo() == null
				|| request.getConfiguration().getClientLayerInfo().getUserData() == null) {
			Exception e = new IllegalArgumentException("Error while saving layermodel: configuration is required.");
			log.error(e.getLocalizedMessage());
			throw e;
		}

		DeskmanagerClientLayerInfo ud = (DeskmanagerClientLayerInfo) request.getConfiguration().getClientLayerInfo()
				.getUserData();
		ClientLayerInfo cvli = request.getConfiguration().getClientLayerInfo();
		LayerModel lm = new LayerModel();
		lm.setDynamicLayerConfiguration(request.getConfiguration());

		lm.setActive(ud.isActive());
		lm.setPublic(ud.isPublic());

		lm.setName(cvli.getLabel());
		lm.setClientLayerId(cvli.getId());
		lm.setDefaultVisible(cvli.isVisible());
		lm.setMaxScale(cvli.getMaximumScale());
		lm.setMinScale(cvli.getMinimumScale());
		lm.setLayerType(request.getConfiguration().getServerLayerInfo().getLayerType());

		Territory g = ((DeskmanagerSecurityContext) securityContext).getTerritory();
		if (g.getId() > 0) { // 0 = superuser
			lm.setOwner(groupService.getById(g.getId()));
		}
		layerModelService.saveOrUpdateLayerModel(lm);
		response.setLayerModel(dtoService.toDto(lm, false/* TODO: , request.getLocale() */));
	}

	@Override
	public LayerModelResponse getEmptyCommandResponse() {
		return new LayerModelResponse();
	}
}
