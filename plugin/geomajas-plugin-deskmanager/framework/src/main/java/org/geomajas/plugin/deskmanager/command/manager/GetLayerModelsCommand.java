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
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsResponse;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that retrieves layermodels where a user has access to. Typically used from the managements interface to 
 * display a list of layers that a user can manage and/or use.
 * 
 * If you need to fetch the full layers including clientlayerinfo, use 
 * {@link org.geomajas.plugin.deskmanager.command.manager.GetLayersCommand}. 
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(GetLayerModelsRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetLayerModelsCommand implements Command<GetLayerModelsRequest, GetLayerModelsResponse> {

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private DtoConverterService converterService;

	/** {@inheritDoc} */
	public void execute(GetLayerModelsRequest request, GetLayerModelsResponse response) throws Exception {

		List<LayerModelDto> layerModels = new ArrayList<LayerModelDto>();

		// no need to filter by group, this is done by security
		for (LayerModel bp : layerModelService.getLayerModels()) {
			layerModels.add(converterService.toDto(bp, false));
		}

		response.setLayerModels(layerModels);
	}

	/** {@inheritDoc} */
	public GetLayerModelsResponse getEmptyCommandResponse() {
		return new GetLayerModelsResponse();
	}

}
