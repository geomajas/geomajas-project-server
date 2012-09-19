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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayersRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetSystemLayersResponse;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Get a list of all allowed system layers.
 * 
 * @author Oliver May
 * 
 */
@Component(GetSystemLayersRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetSystemLayersCommand implements Command<GetSystemLayersRequest, GetSystemLayersResponse> {

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private DtoConverterService converterService;

	public void execute(GetSystemLayersRequest request, GetSystemLayersResponse response) throws Exception {
		for (LayerModel model : layerModelService.getLayerModels()) {
			LayerDto dto = new LayerDto();
			dto.setLayerModel(converterService.toDto(model, false));
			ClientLayerInfo cli = (ClientLayerInfo) applicationContext.getBean(model.getClientLayerId());
			dto.setCLientLayerInfo(cli);
			dto.setClientLayerId(model.getClientLayerId());
			response.getLayers().add(dto);
		}
	}

	public GetSystemLayersResponse getEmptyCommandResponse() {
		return new GetSystemLayersResponse();
	}

}
