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
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckGeodeskIdExistsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.CheckGeodeskIdExistsResponse;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.geomajas.security.GeomajasSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command that checks if a geodesk id already exists in the database. Typically called when an administrator
 * tries to override a geodesk id in the management interface.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Component(/*CheckGeodeskIdExistsRequest.COMMAND*/)
@Transactional(readOnly = true)
public class CheckGeodeskIdExistsCommand implements Command<CheckGeodeskIdExistsRequest, CheckGeodeskIdExistsResponse> {

	@Autowired
	private GeodeskService geodeskService;

	/** {@inheritDoc} */
	public void execute(CheckGeodeskIdExistsRequest request, CheckGeodeskIdExistsResponse response)
			throws GeomajasSecurityException {
		response.setExists(geodeskService.geodeskIdExists(request.getGeodeskId()));
	}

	/** {@inheritDoc} */
	public CheckGeodeskIdExistsResponse getEmptyCommandResponse() {
		return new CheckGeodeskIdExistsResponse();
	}

}
