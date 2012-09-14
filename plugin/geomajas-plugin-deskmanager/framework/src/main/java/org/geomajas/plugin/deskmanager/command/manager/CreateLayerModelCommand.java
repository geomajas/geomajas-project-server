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

import org.geomajas.command.Command;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.command.manager.dto.CreateLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.configuration.client.ExtraClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.DynamicLayerLoadService;
import org.geomajas.plugin.deskmanager.service.common.GroupService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kristof Heirwegh
 */
@Component(CreateLayerModelRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class CreateLayerModelCommand implements Command<CreateLayerModelRequest, LayerModelResponse> {

	private final Logger log = LoggerFactory.getLogger(CreateLayerModelCommand.class);

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private DynamicLayerLoadService loadService;

	@Autowired
	private DtoConverterService dtoService;

	public void execute(CreateLayerModelRequest request, LayerModelResponse response) throws Exception {
		try {
			if (request.getConfiguration() == null) {
				response.getErrorMessages().add("Fout bij opslaan loket: Configuratie is vereist.");
				return;
			}

			ExtraClientLayerInfo ud = (ExtraClientLayerInfo) request.getConfiguration().getClientLayerInfo()
					.getUserData();
			ClientLayerInfo cvli = request.getConfiguration().getClientLayerInfo();
			LayerModel lm = new LayerModel();
			lm.setLayerConfiguration(request.getConfiguration());
			lm.setName(cvli.getLabel());
			lm.setActive(ud.isActive());
			lm.setClientLayerId(cvli.getId());
			lm.setDefaultVisible(cvli.isVisible());
			lm.setMaxScale(cvli.getMaximumScale());
			lm.setMinScale(cvli.getMinimumScale());
			lm.setPublic(ud.isPublicLayer());
			lm.setShowInLegend(ud.isShowInLegend());
			LayerType layerType = request.getConfiguration().getServerLayerInfo().getLayerType();
			switch(layerType) {
				case RASTER:
					lm.setLayerType("Raster");
					break;
				default:
					lm.setLayerType(request.getConfiguration().getServerLayerInfo().getLayerType().getGeometryType());
					break;				
			}

			Territory g = ((DeskmanagerSecurityContext) securityContext).getTerritory();
			if (g.getId() > 0) { // 0 = superuser
				lm.setOwner(groupService.getById(g.getId()));
			}
			layerModelService.saveOrUpdateLayerModel(lm);
			response.setLayerModel(dtoService.toDto(lm, false));

			try {
				loadService.loadDynamicLayers();
				// loadService.loadDynamicLayer(lm.getLayerConfiguration());
			} catch (Exception e) {
				response.getErrorMessages().add("Fout bij initializeren datalaag in context: " + e.getMessage());
				log.error("Fout bij initializeren datalaag in context: ", e);
			}
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij opslaan datalaag: " + e.getMessage());
			log.error("fout bij opslaan datalaag.", e);
		}
	}

	public LayerModelResponse getEmptyCommandResponse() {
		return new LayerModelResponse();
	}
}
