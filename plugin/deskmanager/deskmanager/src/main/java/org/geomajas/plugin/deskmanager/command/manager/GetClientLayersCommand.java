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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetClientLayersResponse;
import org.geomajas.plugin.deskmanager.domain.ClientLayer;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Get an ordered list of all allowed layers for the current user, including layerModel and layerInfo. The purpose
 * of this command is to provide a list of layers that can be added to the layers of a specific geodesk or blueprint.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(GetClientLayersRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetClientLayersCommand implements Command<GetClientLayersRequest, GetClientLayersResponse> {

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private DtoConverterService converterService;

	@Override
	public void execute(GetClientLayersRequest request, GetClientLayersResponse response) throws Exception {
		for (LayerModel model : layerModelService.getLayerModels()) {
			ClientLayer layer = new ClientLayer();
			layer.setLayerModel(model);
			response.getLayers().add(converterService.toDto(layer));
		}
	}

	@Override
	public GetClientLayersResponse getEmptyCommandResponse() {
		return new GetClientLayersResponse();
	}

}
