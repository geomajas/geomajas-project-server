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

import org.geomajas.command.Command;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that retrieves a layermodel where a user has access to.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 *
 */
@Component(GetLayerModelRequest.COMMAND)
@Transactional(readOnly = true, rollbackFor = { Exception.class })
public class GetLayerModelCommand implements Command<GetLayerModelRequest, LayerModelResponse> {

	private final Logger log = LoggerFactory.getLogger(GetLayerModelCommand.class);

	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private DtoConverterService dtoService;

	/** {@inheritDoc} */
	public void execute(GetLayerModelRequest request, LayerModelResponse response) throws Exception {
		try {
			response.setLayerModel(dtoService.toDto(layerModelService.getLayerModelById(request.getId()),
					true, request.getLocale()));
		} catch (Exception e) {
			response.getErrorMessages().add("Error while fetching layermodel: " + e.getMessage());
			log.error("Error while fetching layermodel.", e);
		}
	}

	/** {@inheritDoc} */
	public LayerModelResponse getEmptyCommandResponse() {
		return new LayerModelResponse();
	}
}
