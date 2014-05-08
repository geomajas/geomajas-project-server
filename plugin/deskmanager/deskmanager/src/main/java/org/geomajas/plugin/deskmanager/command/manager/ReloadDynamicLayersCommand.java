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
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.ReloadDynamicLayersRequest;
import org.geomajas.plugin.deskmanager.service.common.DynamicLayerLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver May
 */
@Component(ReloadDynamicLayersRequest.COMMAND)
@Transactional(rollbackFor = { Exception.class })
public class ReloadDynamicLayersCommand implements Command<EmptyCommandRequest, CommandResponse> {

	private final Logger log = LoggerFactory.getLogger(ReloadDynamicLayersCommand.class);

	@Autowired
	private DynamicLayerLoadService loadService;

	@Override
	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}

	@Override
	public void execute(EmptyCommandRequest request, CommandResponse response) throws Exception {
		try {
			loadService.loadDynamicLayers();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw e;
		}
	}
}
