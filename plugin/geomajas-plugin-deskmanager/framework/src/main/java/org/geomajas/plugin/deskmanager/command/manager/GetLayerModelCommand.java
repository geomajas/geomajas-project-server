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
 * TODO.
 * 
 * @author Jan De Moerloose
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

	public void execute(GetLayerModelRequest request, LayerModelResponse response) throws Exception {
		try {
			response.setLayerModel(dtoService.toDto(layerModelService.getLayerModelById(request.getId()), true));
		} catch (Exception e) {
			response.getErrorMessages().add("Fout bij ophalen datalaag: " + e.getMessage());
			log.error("fout bij ophalen datalaag.", e);
		}
	}

	public LayerModelResponse getEmptyCommandResponse() {
		return new LayerModelResponse();
	}
}
